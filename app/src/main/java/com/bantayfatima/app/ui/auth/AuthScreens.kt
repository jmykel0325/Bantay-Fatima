@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.bantayfatima.app.ui.auth

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bantayfatima.app.data.model.RegistrationRequest
import com.bantayfatima.app.data.remote.isGoogleSignInConfigured
import com.bantayfatima.app.ui.components.*
import com.bantayfatima.app.ui.theme.Spacing

private const val TOTAL_STEPS = 3

/** Progress rail shown at the top of every registration step. */
@Composable
private fun StepIndicator(step: Int, label: String, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = "$step of $TOTAL_STEPS",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
            repeat(TOTAL_STEPS) { index ->
                val done = index < step
                val weight by animateFloatAsState(if (done) 1f else 1f, label = "stepWeight")
                Box(
                    Modifier
                        .weight(weight)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            if (done) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant
                        ),
                )
            }
        }
    }
}

/** Shared vertical frame for the authentication screens. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthScaffold(
    title: String,
    onBack: (() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { ScreenTopBar(title = title, onBack = onBack) },
    ) { padding ->
        ResponsiveContainer(Modifier.padding(padding)) { widthModifier ->
            Column(
                widthModifier
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    // Keeps the focused field above the keyboard.
                    .imePadding()
                    .padding(horizontal = Spacing.screen)
                    .padding(top = Spacing.xs, bottom = Spacing.xxl),
                content = content,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Login
// ---------------------------------------------------------------------------

@Composable
fun LoginScreen(
    state: AuthUiState,
    onBack: () -> Unit,
    onLogin: (String, String, Boolean) -> Unit,
    onGoogle: () -> Unit,
    onRegister: () -> Unit,
    onForgot: () -> Unit,
    onDismissError: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var keepSignedIn by rememberSaveable { mutableStateOf(true) }
    // The address is only judged once the resident has moved on from the field.
    // Complaining about "j" while someone types "juan@..." is noise, not help.
    var emailFocused by remember { mutableStateOf(false) }
    var emailVisited by rememberSaveable { mutableStateOf(false) }

    val emailValid = email.trim().contains("@")
    val emailError = if (emailVisited && !emailFocused && email.isNotBlank() && !emailValid) {
        "Enter a valid email address."
    } else {
        null
    }
    val canSubmit = email.isNotBlank() && password.isNotBlank() && !state.googleBusy
    val submit = { if (canSubmit) onLogin(email, password, keepSignedIn) }

    AuthScaffold(title = "Sign In", onBack = onBack) {
        Spacer(Modifier.height(Spacing.xs))
        BantayFatimaLogo(size = 64.dp, modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(Modifier.height(Spacing.md))
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { heading() },
        )
        Spacer(Modifier.height(Spacing.xxs))
        Text(
            text = "Sign in to access your Bantay Fatima account.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(Spacing.lg))
        ErrorBanner(state.error, onDismissError)
        if (state.error != null) Spacer(Modifier.height(Spacing.sm))

        // Offered first: a resident who registered with Google is returned to their
        // account in one tap, with no password to recall.
        GoogleAuthButton(
            text = "Continue with Google",
            loading = state.googleBusy,
            enabled = !state.busy,
            onClick = onGoogle,
        )
        if (isGoogleSignInConfigured) {
            Spacer(Modifier.height(Spacing.md))
            LabelledDivider("or sign in with email")
            Spacer(Modifier.height(Spacing.md))
        }

        AppTextField(
            value = email,
            onValueChange = { email = it },
            label = "Gmail Address",
            leadingIcon = Icons.Filled.Email,
            errorMessage = emailError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            modifier = Modifier.onFocusChanged { focus ->
                if (focus.hasFocus) emailVisited = true
                emailFocused = focus.hasFocus
            },
        )
        Spacer(Modifier.height(Spacing.sm))
        PasswordField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(onDone = { submit() }),
        )

        Spacer(Modifier.height(Spacing.xs))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { keepSignedIn = !keepSignedIn }
                    .padding(end = Spacing.xs),
            ) {
                Checkbox(checked = keepSignedIn, onCheckedChange = { keepSignedIn = it })
                Text("Keep me signed in", style = MaterialTheme.typography.bodySmall)
            }
            TextLink("Forgot Password", onClick = onForgot)
        }

        Spacer(Modifier.height(Spacing.md))
        PrimaryButton(
            text = "Sign In",
            loading = state.busy,
            enabled = canSubmit,
            onClick = submit,
        )

        Spacer(Modifier.height(Spacing.lg))
        Text(
            text = "New to Bantay Fatima?",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(Modifier.height(Spacing.xs))
        SecondaryButton(
            text = "Create a Resident Account",
            icon = Icons.Filled.PersonAdd,
            enabled = !state.busy && !state.googleBusy,
            onClick = onRegister,
        )

        Spacer(Modifier.height(Spacing.lg))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            TextLink("Terms and Conditions") {}
            TextLink("Privacy Policy") {}
        }
    }
}

// ---------------------------------------------------------------------------
// Registration: step 1 (personal details) and step 2 (security)
// ---------------------------------------------------------------------------

/**
 * Resident registration.
 *
 * Split into two on-screen steps so neither is a wall of inputs. The API contract
 * is unchanged: everything is submitted together via [onSubmit] once step 2 is
 * valid, and the server then issues the verification code.
 */
@Composable
fun RegistrationScreen(
    state: AuthUiState,
    onBack: () -> Unit,
    onSubmit: (RegistrationRequest) -> Unit,
    onGoogle: () -> Unit,
    onDismissError: () -> Unit,
) {
    var step by rememberSaveable { mutableIntStateOf(1) }
    var first by rememberSaveable { mutableStateOf("") }
    var middle by rememberSaveable { mutableStateOf("") }
    var last by rememberSaveable { mutableStateOf("") }
    var suffix by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    var terms by rememberSaveable { mutableStateOf(false) }
    var privacy by rememberSaveable { mutableStateOf(false) }

    val gmailValid = email.trim().lowercase().matches(Regex("^[^@\\s]+@gmail\\.com$"))
    val phoneDigits = phoneNumber.filter(Char::isDigit)
    val phoneValid = phoneDigits.matches(Regex("^(09\\d{9}|639\\d{9})$"))
    val stepOneValid = first.isNotBlank() && last.isNotBlank() && gmailValid && phoneValid
    val passwordValid = password.length >= 8 && password.any(Char::isUpperCase) &&
        password.any(Char::isLowerCase) && password.any(Char::isDigit)
    val stepTwoValid = passwordValid && password == confirm && terms && privacy

    AuthScaffold(
        title = if (step == 1) "Create Your Account" else "Secure Your Account",
        onBack = { if (step == 2) step = 1 else onBack() },
    ) {
        StepIndicator(step = step, label = if (step == 1) "Personal information" else "Account security")

        Spacer(Modifier.height(Spacing.lg))
        ErrorBanner(state.error, onDismissError)
        if (state.error != null) Spacer(Modifier.height(Spacing.sm))

        if (step == 1) {
            Text(
                text = "This takes about a minute. Use your name as it is registered with the barangay.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(Spacing.lg))
            FieldGroupLabel("Your name")
            Spacer(Modifier.height(Spacing.sm))

            AppTextField(first, { first = it }, "First Name", leadingIcon = Icons.Filled.Person)
            Spacer(Modifier.height(Spacing.sm))
            AppTextField(middle, { middle = it }, "Middle Name (optional)")
            Spacer(Modifier.height(Spacing.sm))
            // Paired on one row because a suffix is short and belongs with the family
            // name; it also keeps the form from reading as six identical boxes.
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                AppTextField(last, { last = it }, "Last Name", modifier = Modifier.weight(1.6f))
                AppTextField(suffix, { suffix = it }, "Suffix", modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(Spacing.lg))
            FieldGroupLabel("How we reach you")
            Spacer(Modifier.height(Spacing.sm))
            AppTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = "Mobile Number",
                leadingIcon = Icons.Filled.Phone,
                errorMessage = if (phoneNumber.isNotBlank() && !phoneValid) "Enter a valid Philippine mobile number, such as 0917 123 4567." else null,
                helperText = "Kept on your resident profile. Codes are never sent by text.",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
            )
            Spacer(Modifier.height(Spacing.sm))
            AppTextField(
                value = email,
                onValueChange = { email = it },
                label = "Gmail Address",
                leadingIcon = Icons.Filled.Email,
                errorMessage = if (email.isNotBlank() && !gmailValid) "Use a valid Gmail address ending in @gmail.com." else null,
                helperText = "Your verification code is sent here.",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
            )

            Spacer(Modifier.height(Spacing.xl))
            PrimaryButton("Continue", enabled = stepOneValid && !state.googleBusy) { step = 2 }

            if (isGoogleSignInConfigured) {
                Spacer(Modifier.height(Spacing.md))
                LabelledDivider("or")
                Spacer(Modifier.height(Spacing.md))
                GoogleAuthButton(
                    text = "Sign up with Google",
                    loading = state.googleBusy,
                    enabled = !state.busy,
                    onClick = onGoogle,
                )
                Spacer(Modifier.height(Spacing.xs))
                Text(
                    text = "Google confirms your Gmail address for us, so there is no form and no code to enter. If you already have an account, this signs you into it.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        } else {
            Text(
                text = "Choose a password to protect your Bantay Fatima account.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(Spacing.md))

            PasswordField(password, { password = it }, "Password")
            Spacer(Modifier.height(Spacing.sm))
            PasswordStrengthMeter(password)
            Spacer(Modifier.height(Spacing.md))
            PasswordField(
                value = confirm,
                onValueChange = { confirm = it },
                label = "Confirm Password",
                errorMessage = if (confirm.isNotEmpty() && confirm != password) "Passwords do not match." else null,
                imeAction = ImeAction.Done,
            )

            Spacer(Modifier.height(Spacing.md))
            ConsentRow(terms, { terms = it }, "I agree to the Terms and Conditions.")
            ConsentRow(privacy, { privacy = it }, "I acknowledge the Privacy Policy.")

            Spacer(Modifier.height(Spacing.lg))
            PrimaryButton(
                text = "Send Verification Code",
                loading = state.busy,
                enabled = stepTwoValid,
            ) {
                onSubmit(
                    RegistrationRequest(
                        firstName = first.trim(),
                        middleName = middle.trim().ifBlank { null },
                        lastName = last.trim(),
                        suffix = suffix.trim().ifBlank { null },
                        email = email.trim(),
                        phoneNumber = phoneNumber.trim(),
                        password = password,
                        passwordConfirmation = confirm,
                        terms = true,
                    )
                )
            }
            Spacer(Modifier.height(Spacing.xs))
            TextLink("Back to personal information", modifier = Modifier.align(Alignment.CenterHorizontally)) { step = 1 }
        }
    }
}

/** Section heading inside a form, so a long list of inputs reads as groups. */
@Composable
private fun FieldGroupLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun ConsentRow(checked: Boolean, onChange: (Boolean) -> Unit, label: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onChange(!checked) }
            .padding(end = Spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = checked, onCheckedChange = onChange)
        Text(label, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
    }
}

// ---------------------------------------------------------------------------
// Step 3: e-mail verification
// ---------------------------------------------------------------------------

@Composable
fun VerificationScreen(
    state: AuthUiState,
    onBack: () -> Unit,
    onVerify: (String) -> Unit,
    onResend: () -> Unit,
    onDismissError: () -> Unit,
) {
    var code by rememberSaveable { mutableStateOf("") }

    AuthScaffold(title = "Verify Your Email", onBack = onBack) {
        StepIndicator(step = 3, label = "Email verification")

        Spacer(Modifier.height(Spacing.xl))
        IconBadge(
            icon = Icons.Filled.MarkEmailRead,
            size = 84.dp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(Modifier.height(Spacing.lg))
        Text(
            text = "Check Your Email",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { heading() },
        )
        Spacer(Modifier.height(Spacing.xs))
        Text(
            text = "Enter the six-digit code sent to ${state.maskedEmail ?: "your Gmail address"}.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(Spacing.lg))
        ErrorBanner(state.error, onDismissError)
        if (state.error != null) Spacer(Modifier.height(Spacing.sm))

        OtpInput(
            value = code,
            onValueChange = { code = it },
            isError = state.error != null,
            enabled = !state.busy,
            onFilled = onVerify,
        )

        Spacer(Modifier.height(Spacing.lg))
        PrimaryButton(
            text = "Verify Email",
            loading = state.busy,
            enabled = code.length == 6,
        ) { onVerify(code) }

        Spacer(Modifier.height(Spacing.sm))
        SecondaryButton(
            text = if (state.resendSeconds > 0) "Resend Code in ${state.resendSeconds}s" else "Resend Code",
            icon = Icons.Filled.Refresh,
            enabled = !state.busy && state.resendSeconds == 0,
            onClick = onResend,
        )
        Spacer(Modifier.height(Spacing.xs))
        TextLink("Change Email", modifier = Modifier.align(Alignment.CenterHorizontally), onClick = onBack)
    }
}

// ---------------------------------------------------------------------------
// Forgot password
// ---------------------------------------------------------------------------

@Composable
fun ForgotPasswordScreen(onBack: () -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    val valid = email.trim().lowercase().matches(Regex("^[^@\\s]+@gmail\\.com$"))

    AuthScaffold(title = "Reset Password", onBack = onBack) {
        Spacer(Modifier.height(Spacing.xs))
        IconBadge(
            icon = Icons.Filled.LockReset,
            size = 78.dp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(Modifier.height(Spacing.lg))
        Text(
            text = "Forgot your password?",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.semantics { heading() },
        )
        Spacer(Modifier.height(Spacing.xxs))
        Text(
            text = "Enter your Gmail address. If it is registered, a verification code will be sent.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(Spacing.lg))
        AppTextField(
            value = email,
            onValueChange = { email = it },
            label = "Gmail Address",
            leadingIcon = Icons.Filled.Email,
            errorMessage = if (email.isNotBlank() && !valid) "Use a valid Gmail address ending in @gmail.com." else null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
        )

        Spacer(Modifier.height(Spacing.lg))
        PrimaryButton("Send Reset Code", enabled = valid) {}

        Spacer(Modifier.height(Spacing.lg))
        InfoCard(
            text = "Password reset uses the same secure email-code service as registration.",
            icon = Icons.Filled.Info,
        )
    }
}
