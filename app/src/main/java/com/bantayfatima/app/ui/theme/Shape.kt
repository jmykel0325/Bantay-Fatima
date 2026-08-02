package com.bantayfatima.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

/** Standard card corner. */
val CardShape = RoundedCornerShape(20.dp)

/** Hero and other feature surfaces. */
val HeroShape = RoundedCornerShape(24.dp)

/** Buttons and compact controls. */
val ButtonShape = RoundedCornerShape(14.dp)

/** Text inputs. */
val FieldShape = RoundedCornerShape(14.dp)

/** Status pills and small chips. */
val BadgeShape = RoundedCornerShape(8.dp)
