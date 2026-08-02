package com.bantayfatima.app.ui.theme

import androidx.compose.ui.unit.dp

/**
 * The only spacing values used in the application.
 *
 * Screens keep at least [Spacing.screen] of horizontal padding so nothing sits
 * against the display edge, and major sections are separated by [Spacing.section].
 */
object Spacing {
    val xxs = 4.dp
    val xs = 8.dp
    val sm = 12.dp
    val md = 16.dp
    val lg = 20.dp
    val xl = 24.dp
    val xxl = 32.dp

    /** Minimum horizontal padding for any screen. */
    val screen = 16.dp

    /** Vertical gap between major sections of a screen. */
    val section = 28.dp
}

/** Minimum height for primary controls, comfortably above the 48dp touch target. */
val ControlHeight = 54.dp

/** Height for text inputs. */
val FieldHeight = 56.dp
