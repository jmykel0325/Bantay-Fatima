package com.bantayfatima.app.ui.theme

import androidx.compose.ui.graphics.Color

// ---------------------------------------------------------------------------
// Bantay Fatima brand palette.
//
// Blue carries hierarchy, interaction and emphasis only. The application stays
// mostly white and light grey with deep navy text; a screen that reads as blue
// overall has used these tokens too freely.
//
// Every colour in the application comes from this file. Screens and components
// read tokens by name and never repeat a hex value of their own.
// ---------------------------------------------------------------------------

/** Deep Navy. App header, headings, strong text, institutional emphasis. */
val BantayNavy = Color(0xFF071A2F)

/** Indigo. Secondary dark accents, selected tabs, dark badges. */
val BantayIndigo = Color(0xFF0D3155)

/** Medium Blue. Primary buttons, active navigation, links, focus, progress. */
val BantayPrimary = Color(0xFF447FC2)

/** Light Blue. Informational icons, secondary actions, supporting accents. */
val BantayLightBlue = Color(0xFF5C9FD0)

/** Soft Sky Blue. Light card backgrounds, selected tiles, information panels. */
val BantaySky = Color(0xFF73B1DA)

// Neutrals. These, not the blues, are what most of the interface is made of.
val BantayBackground = Color(0xFFF5F8FA)
val BantayWhite = Color(0xFFFFFFFF)
val BantayBorder = Color(0xFFDCE5EB)

/** Primary reading text. */
val BantayText = Color(0xFF10233D)

/** Secondary and supporting text. */
val BantayMuted = Color(0xFF687D92)

// Derived tones ------------------------------------------------------------
// Pale fills for containers, and the deeper navy the splash gradient closes on.
val BantayPrimarySoft = Color(0xFFE6EFF8)
val BantayIndigoSoft = Color(0xFFE6EEF5)
val BantaySkySoft = Color(0xFFEAF3FA)
val BantayNavySoft = Color(0xFFE3EAF1)
val BantayNavyDeep = Color(0xFF03101E)
val BantayPrimaryLight = Color(0xFF9CC2E4)
val BantayNavyLight = Color(0xFF9AB6D0)

/** Header subtitle over the navy masthead. */
val BantayOnNavyMuted = Color(0xFFC3D2DF)

/** Neutral slate used where a tile needs to stay unaccented. */
val BantaySlate = Color(0xFF4B5C6B)
val BantaySlateSoft = Color(0xFFEFF2F5)

// ---------------------------------------------------------------------------
// Report status palette.
//
// Kept separate from the brand blues on purpose: a status must not read as a
// brand accent, and urgent states must never be recoloured to blue. Every status
// is expressed by colour *and* text, so meaning survives for colour-blind
// residents.
// ---------------------------------------------------------------------------
val StatusPending = Color(0xFFF2A120)
val StatusInProgress = Color(0xFF447FC2)

/** Muted blue-teal rather than green, so "resolved" stays inside the palette. */
val StatusResolved = Color(0xFF2F7D9C)
val StatusUrgent = Color(0xFFD9383D)

// Readable text tones for use on the soft containers below.
val StatusPendingText = Color(0xFF8A5A00)
val StatusInProgressText = Color(0xFF1F4C80)
val StatusResolvedText = Color(0xFF1B4E62)
val StatusUrgentText = Color(0xFF8F2023)

val StatusPendingSoft = Color(0xFFFDF1DC)
val StatusInProgressSoft = Color(0xFFE6EFF8)
val StatusResolvedSoft = Color(0xFFE2EFF4)
val StatusUrgentSoft = Color(0xFFFBE9E9)
val StatusNeutralSoft = Color(0xFFEDF2F6)

// ---------------------------------------------------------------------------
// Dark theme surfaces
// ---------------------------------------------------------------------------
val BantayDarkBackground = Color(0xFF04111F)
val BantayDarkSurface = Color(0xFF0A2035)
val BantayDarkSurfaceHigh = Color(0xFF10304D)
val BantayDarkBorder = Color(0xFF274A68)
val BantayDarkMuted = Color(0xFFA9BCCB)
