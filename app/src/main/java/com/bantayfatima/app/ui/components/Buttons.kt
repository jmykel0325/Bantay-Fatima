package com.bantayfatima.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.bantayfatima.app.ui.theme.ButtonShape
import com.bantayfatima.app.ui.theme.ControlHeight
import com.bantayfatima.app.ui.theme.Spacing

/**
 * Primary call to action.
 *
 * Handles its own loading state so callers never swap the label for a spinner and
 * lose the button's width. Press feedback is a small scale, which reads as
 * responsive without being distracting.
 */
@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    loading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.98f else 1f, label = "primaryButtonScale")

    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        shape = ButtonShape,
        interactionSource = interaction,
        contentPadding = PaddingValues(horizontal = Spacing.lg),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = ControlHeight)
            .scale(scale),
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Spacer(Modifier.width(Spacing.sm))
        } else if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(Spacing.xs))
        }
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

/** Secondary action, matched to [PrimaryButton]'s proportions. */
@Composable
fun SecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.98f else 1f, label = "secondaryButtonScale")

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = ButtonShape,
        interactionSource = interaction,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
        contentPadding = PaddingValues(horizontal = Spacing.lg),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = ControlHeight)
            .scale(scale),
    ) {
        icon?.let {
            Icon(it, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(Spacing.xs))
        }
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

/** Quiet tonal action used inside cards and sheets. */
@Composable
fun TonalButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    FilledTonalButton(
        onClick = onClick,
        enabled = enabled,
        shape = ButtonShape,
        contentPadding = PaddingValues(horizontal = Spacing.lg),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = ControlHeight),
    ) {
        icon?.let {
            Icon(it, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(Spacing.xs))
        }
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

/** Inline text action. Keeps a 48dp touch target despite the small label. */
@Composable
fun TextLink(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        shape = ButtonShape,
        modifier = modifier.heightIn(min = 48.dp),
    ) {
        Text(text, style = MaterialTheme.typography.titleSmall)
    }
}
