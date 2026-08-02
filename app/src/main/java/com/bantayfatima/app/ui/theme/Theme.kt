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
    primary = BantayGreen,
    onPrimary = BantayWhite,
    primaryContainer = BantayGreenSoft,
    onPrimaryContainer = Color(0xFF06342D),
    secondary = BantayNavy,
    onSecondary = BantayWhite,
    secondaryContainer = BantayNavySoft,
    onSecondaryContainer = Color(0xFF06192B),
    tertiary = StatusInProgress,
    onTertiary = BantayWhite,
    background = BantayBackground,
    onBackground = BantayNavy,
    surface = BantayWhite,
    onSurface = BantayNavy,
    surfaceVariant = StatusNeutralSoft,
    onSurfaceVariant = BantayMuted,
    outline = BantayBorder,
    outlineVariant = Color(0xFFE9EFF4),
    error = StatusUrgent,
    onError = BantayWhite,
    errorContainer = StatusUrgentSoft,
    onErrorContainer = StatusUrgentText,
    scrim = Color(0x800B2D4D),
)

private val DarkColors = darkColorScheme(
    primary = BantayGreenLight,
    onPrimary = Color(0xFF00352C),
    primaryContainer = Color(0xFF0C5F53),
    onPrimaryContainer = Color(0xFFD5F0EA),
    secondary = BantayNavyLight,
    onSecondary = Color(0xFF06192B),
    secondaryContainer = Color(0xFF1B4266),
    onSecondaryContainer = Color(0xFFDCE8F3),
    tertiary = Color(0xFF8FBEF5),
    onTertiary = Color(0xFF0A2B4D),
    background = BantayDarkBackground,
    onBackground = Color(0xFFE6EEF5),
    surface = BantayDarkSurface,
    onSurface = Color(0xFFE6EEF5),
    surfaceVariant = BantayDarkSurfaceHigh,
    onSurfaceVariant = BantayDarkMuted,
    outline = BantayDarkBorder,
    outlineVariant = Color(0xFF1B3B57),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF5C1512),
    errorContainer = Color(0xFF7A2020),
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
    inProgressText = Color(0xFFB9D6FA),
    inProgressContainer = Color(0xFF17375E),
    resolved = StatusResolved,
    resolvedText = Color(0xFFA9E5CB),
    resolvedContainer = Color(0xFF10402F),
    urgent = StatusUrgent,
    urgentText = Color(0xFFFFC4C4),
    urgentContainer = Color(0xFF4E1D1D),
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
