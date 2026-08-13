package com.bantayfatima.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = BantayPrimary,
    onPrimary = BantayWhite,
    primaryContainer = BantayPrimarySoft,
    onPrimaryContainer = Color(0xFF12365E),
    secondary = BantayNavy,
    onSecondary = BantayWhite,
    secondaryContainer = BantayNavySoft,
    onSecondaryContainer = Color(0xFF1A1C4A),
    tertiary = BantayLightBlue,
    onTertiary = BantayWhite,
    background = BantayBackground,
    onBackground = BantayText,
    surface = BantayWhite,
    onSurface = BantayText,
    surfaceVariant = StatusNeutralSoft,
    onSurfaceVariant = BantayMuted,
    outline = BantayBorder,
    outlineVariant = Color(0xFFEAEFF4),
    error = StatusUrgent,
    onError = BantayWhite,
    errorContainer = StatusUrgentSoft,
    onErrorContainer = StatusUrgentText,
    scrim = Color(0x80071A2F),
)

private val DarkColors = darkColorScheme(
    primary = BantaySky,
    onPrimary = Color(0xFF0B2647),
    primaryContainer = Color(0xFF2C5C8F),
    onPrimaryContainer = Color(0xFFD9E8F6),
    secondary = BantayNavyLight,
    onSecondary = Color(0xFF041524),
    secondaryContainer = Color(0xFF123B60),
    onSecondaryContainer = Color(0xFFD8E8F4),
    tertiary = Color(0xFF9CC2E4),
    onTertiary = Color(0xFF10233D),
    background = BantayDarkBackground,
    onBackground = Color(0xFFE7E9F5),
    surface = BantayDarkSurface,
    onSurface = Color(0xFFE7E9F5),
    surfaceVariant = BantayDarkSurfaceHigh,
    onSurfaceVariant = BantayDarkMuted,
    outline = BantayDarkBorder,
    outlineVariant = Color(0xFF183C5B),
    error = Color(0xFFFFB3B4),
    onError = Color(0xFF5C1416),
    errorContainer = Color(0xFF7A2124),
    onErrorContainer = Color(0xFFFFDAD6),
    scrim = Color(0xB3000000),
)

/**
 * Status colours are not part of the Material scheme, so they travel through the
 * theme in their own holder. Screens read them from here instead of hardcoding.
 */
data class StatusColors(
    val pending: Color,
    val pendingText: Color,
    val pendingContainer: Color,
    val inProgress: Color,
    val inProgressText: Color,
    val inProgressContainer: Color,
    val resolved: Color,
    val resolvedText: Color,
    val resolvedContainer: Color,
    val urgent: Color,
    val urgentText: Color,
    val urgentContainer: Color,
    val neutralContainer: Color,
)

private val LightStatusColors = StatusColors(
    pending = StatusPending,
    pendingText = StatusPendingText,
    pendingContainer = StatusPendingSoft,
    inProgress = StatusInProgress,
    inProgressText = StatusInProgressText,
    inProgressContainer = StatusInProgressSoft,
    resolved = StatusResolved,
    resolvedText = StatusResolvedText,
    resolvedContainer = StatusResolvedSoft,
    urgent = StatusUrgent,
    urgentText = StatusUrgentText,
    urgentContainer = StatusUrgentSoft,
    neutralContainer = StatusNeutralSoft,
)

private val DarkStatusColors = StatusColors(
    pending = StatusPending,
    pendingText = Color(0xFFFFD9A0),
    pendingContainer = Color(0xFF4A3411),
    inProgress = StatusInProgress,
    inProgressText = Color(0xFFBBD5EF),
    inProgressContainer = Color(0xFF1F3F63),
    resolved = StatusResolved,
    resolvedText = Color(0xFFB5DCE9),
    resolvedContainer = Color(0xFF17414F),
    urgent = StatusUrgent,
    urgentText = Color(0xFFFFC5C6),
    urgentContainer = Color(0xFF4E1E1F),
    neutralContainer = BantayDarkSurfaceHigh,
)

val LocalStatusColors = staticCompositionLocalOf { LightStatusColors }

/** Convenience accessor: `AppTheme.status.pending`. */
object AppTheme {
    val status: StatusColors
        @Composable @ReadOnlyComposable get() = LocalStatusColors.current
}

/**
 * Bantay Fatima theme.
 *
 * Dynamic (wallpaper) colour is deliberately not used: this is an official
 * barangay service, so the palette must match the Laravel web application on
 * every device rather than adapt to personal settings.
 */
@Composable
fun BantayFatimaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalStatusColors provides if (darkTheme) DarkStatusColors else LightStatusColors,
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColors else LightColors,
            typography = Typography,
            shapes = AppShapes,
            content = content,
        )
    }
}
