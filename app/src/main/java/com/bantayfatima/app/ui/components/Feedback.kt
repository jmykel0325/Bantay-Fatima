package com.bantayfatima.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bantayfatima.app.ui.theme.AppTheme
import com.bantayfatima.app.ui.theme.BadgeShape
import com.bantayfatima.app.ui.theme.CardShape
import com.bantayfatima.app.ui.theme.Spacing

/** Meaning carried by a [StatusBadge]. */
enum class StatusTone { Pending, InProgress, Resolved, Urgent, Neutral }

/**
 * Report or announcement status.
 *
 * Always renders the status word, so meaning never depends on colour alone.
 */
@Composable
fun StatusBadge(text: String, tone: StatusTone, modifier: Modifier = Modifier) {
    val status = AppTheme.status
    val (content, container) = when (tone) {
        StatusTone.Pending -> status.pendingText to status.pendingContainer
        StatusTone.InProgress -> status.inProgressText to status.inProgressContainer
        StatusTone.Resolved -> status.resolvedText to status.resolvedContainer
        StatusTone.Urgent -> status.urgentText to status.urgentContainer
        StatusTone.Neutral -> MaterialTheme.colorScheme.onSurfaceVariant to status.neutralContainer
    }
    Box(
        modifier = modifier
            .clip(BadgeShape)
            .background(container)
            .padding(horizontal = Spacing.xs, vertical = Spacing.xxs),
    ) {
        Text(text.uppercase(), style = MaterialTheme.typography.labelSmall, color = content)
    }
}

/** Placeholder for a list that has loaded but has no records. */
@Composable
fun EmptyState(
    title: String,
    description: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.lg, vertical = Spacing.xxl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Box(
                Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(27.dp),
                )
            }
            Text(title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            if (actionLabel != null && onAction != null) {
                Spacer(Modifier.height(Spacing.xxs))
                TonalButton(actionLabel, onClick = onAction)
            }
        }
    }
}

/** Recoverable failure, with a retry affordance. */
@Composable
fun ErrorState(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
) {
    val status = AppTheme.status
    AppCard(modifier = modifier.fillMaxWidth(), container = status.urgentContainer) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(Spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Icon(
                Icons.Filled.ErrorOutline,
                contentDescription = null,
                tint = status.urgent,
                modifier = Modifier.size(30.dp),
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = status.urgentText,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = status.urgentText.copy(alpha = 0.92f),
                textAlign = TextAlign.Center,
            )
            if (onRetry != null) {
                Spacer(Modifier.height(Spacing.xxs))
                TonalButton("Try Again", onClick = onRetry)
            }
        }
    }
}

/** Inline progress indicator with a spoken label. */
@Composable
fun LoadingState(label: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.xl),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.5.dp)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
        )
    }
}

/** Dismissible form-level error. */
@Composable
fun ErrorBanner(message: String?, onDismiss: () -> Unit, modifier: Modifier = Modifier) {
    if (message == null) return
    val status = AppTheme.status
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardShape)
            .background(status.urgentContainer)
            .padding(start = Spacing.md, top = Spacing.sm, bottom = Spacing.sm, end = Spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Filled.ErrorOutline,
            contentDescription = null,
            tint = status.urgent,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = message,
            modifier = Modifier
                .weight(1f)
                .semantics { liveRegion = LiveRegionMode.Assertive },
            style = MaterialTheme.typography.bodySmall,
            color = status.urgentText,
        )
        IconButton(onClick = onDismiss) {
            Icon(
                Icons.Filled.Close,
                contentDescription = "Dismiss error",
                tint = status.urgentText,
                modifier = Modifier.size(19.dp),
            )
        }
    }
}

/**
 * Shown when a guest taps a feature that needs an account.
 *
 * A sheet rather than a silent redirect, so the resident understands why the screen
 * changed and can decline. It states what an account is actually for before asking
 * for one, and offers Google first: that route needs no form and no e-mail code, and
 * it resolves by itself to a sign-in for a resident who already registered.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationRequiredSheet(
    onDismiss: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onGoogle: () -> Unit,
    googleBusy: Boolean = false,
    error: String? = null,
    onDismissError: () -> Unit = {},
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        // Opened at full height, and scrollable, so the actions are never below the
        // fold waiting to be dragged into view on a short screen.
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.lg)
                .padding(bottom = Spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Box(
                Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(29.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(26.dp),
                )
            }
            Text(
                text = "Sign in required",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = "A resident account lets the barangay reach you about what you send.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(Spacing.xxs))
            SheetBenefit(Icons.Filled.PhotoCamera, "Submit a concern with photos and location")
            SheetBenefit(Icons.Filled.Timeline, "Follow every report you file until it is resolved")
            SheetBenefit(Icons.Filled.Notifications, "Get replies from barangay staff")

            if (error != null) {
                Spacer(Modifier.height(Spacing.xxs))
                ErrorBanner(error, onDismissError)
            }

            Spacer(Modifier.height(Spacing.xxs))
            GoogleAuthButton("Continue with Google", loading = googleBusy, onClick = onGoogle)
            LabelledDivider("or use your email")
            PrimaryButton("Log In", enabled = !googleBusy, onClick = onLogin)
            SecondaryButton("Create Account", enabled = !googleBusy, onClick = onRegister)
            TextLink("Not Now", modifier = Modifier.align(Alignment.CenterHorizontally), onClick = onDismiss)
        }
    }
}

/** One reason-to-register line inside [AuthenticationRequiredSheet]. */
@Composable
private fun SheetBenefit(icon: ImageVector, text: String) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Small circular icon badge used across headers and lists. */
@Composable
fun IconBadge(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 44.dp,
    tint: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    container: Color = MaterialTheme.colorScheme.primaryContainer,
    contentDescription: String? = null,
) {
    Box(
        modifier
            .size(size)
            .clip(RoundedCornerShape(size / 3))
            .background(container),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = contentDescription, tint = tint, modifier = Modifier.size(size / 2))
    }
}
