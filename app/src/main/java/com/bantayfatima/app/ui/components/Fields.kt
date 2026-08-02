package com.bantayfatima.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bantayfatima.app.ui.theme.FieldHeight
import com.bantayfatima.app.ui.theme.FieldShape
import com.bantayfatima.app.ui.theme.Spacing

/**
 * Standard text input.
 *
 * Errors are announced as text under the field rather than by the red outline
 * alone, so the reason is available to screen readers and to anyone who cannot
 * distinguish the border colour.
 */
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    errorMessage: String? = null,
    helperText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
) {
    Column(modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = leadingIcon?.let { { Icon(it, contentDescription = null) } },
            trailingIcon = trailingIcon,
            isError = errorMessage != null,
            enabled = enabled,
            singleLine = true,
            shape = FieldShape,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = FieldHeight),
        )
        val support = errorMessage ?: helperText
        if (support != null) {
            Text(
                text = support,
                style = MaterialTheme.typography.labelMedium,
                color = if (errorMessage != null) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = Spacing.md, top = Spacing.xxs),
            )
        }
    }
}

/** Password input with a visibility toggle that reports its state to accessibility. */
@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    helperText: String? = null,
    imeAction: androidx.compose.ui.text.input.ImeAction = androidx.compose.ui.text.input.ImeAction.Next,
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        leadingIcon = Icons.Filled.Lock,
        errorMessage = errorMessage,
        helperText = helperText,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector = if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (visible) "Hide password" else "Show password",
                )
            }
        },
    )
}

/** Four-segment password strength meter. */
@Composable
fun PasswordStrengthMeter(password: String, modifier: Modifier = Modifier) {
    val status = com.bantayfatima.app.ui.theme.AppTheme.status
    val score = listOf(
        password.length >= 8,
        password.any(Char::isUpperCase),
        password.any(Char::isLowerCase),
        password.any(Char::isDigit),
    ).count { it }

    val label = when {
        password.isEmpty() -> "Use at least 8 characters with uppercase, lowercase, and a number."
        score <= 1 -> "Weak password"
        score == 2 -> "Fair password"
        score == 3 -> "Good password"
        else -> "Strong password"
    }
    val colour = when {
        password.isEmpty() -> MaterialTheme.colorScheme.outline
        score <= 1 -> status.urgent
        score <= 3 -> status.pending
        else -> status.resolved
    }

    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
            repeat(4) { index ->
                val filled = index < score
                val barColour by animateColorAsState(
                    if (filled) colour else MaterialTheme.colorScheme.surfaceVariant,
                    label = "strengthBar",
                )
                Box(
                    Modifier
                        .weight(1f)
                        .height(5.dp)
                        .clip(FieldShape)
                        .background(barColour),
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (password.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else colour,
        )
    }
}

/**
 * Six-box verification code input.
 *
 * A single hidden field owns the text so that pasting a whole code, autofill from
 * the SMS/e-mail suggestion strip, and backspace all behave normally. The boxes
 * are decoration drawn over it, with the active box highlighted.
 */
@Composable
fun OtpInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6,
    isError: Boolean = false,
    enabled: Boolean = true,
    onFilled: (String) -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    var fieldValue by remember(value) {
        mutableStateOf(TextFieldValue(value, TextRange(value.length)))
    }

    BasicTextField(
        value = fieldValue,
        onValueChange = { input ->
            val digits = input.text.filter(Char::isDigit).take(length)
            fieldValue = TextFieldValue(digits, TextRange(digits.length))
            if (digits != value) onValueChange(digits)
            if (digits.length == length) {
                keyboard?.hide()
                onFilled(digits)
            }
        },
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        interactionSource = remember { MutableInteractionSource() },
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .semantics { contentDescription = "Six-digit verification code" },
        decorationBox = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                repeat(length) { index ->
                    val char = value.getOrNull(index)
                    val active = index == value.length.coerceAtMost(length - 1) && value.length < length
                    val border by animateColorAsState(
                        when {
                            isError -> MaterialTheme.colorScheme.error
                            active -> MaterialTheme.colorScheme.primary
                            char != null -> MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
                            else -> MaterialTheme.colorScheme.outline
                        },
                        label = "otpBorder",
                    )
                    Box(
                        Modifier
                            .weight(1f)
                            .height(58.dp)
                            .clip(FieldShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(if (active || char != null) 2.dp else 1.dp, border, FieldShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = char?.toString() ?: "",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        },
    )

    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}
