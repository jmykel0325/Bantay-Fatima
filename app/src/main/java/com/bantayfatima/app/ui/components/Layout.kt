package com.bantayfatima.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bantayfatima.app.data.remote.isGoogleSignInConfigured
import com.bantayfatima.app.ui.theme.Spacing

/**
 * Widest a column of content is allowed to become.
 *
 * On a phone this never applies. On a tablet or an unfolded device it stops text
 * lines from running the full width of the display, which is uncomfortable to
 * read and makes the layout look stretched rather than designed.
 */
val MaxContentWidth: Dp = 640.dp

/**
 * Rule with a centred label, used to separate the Google route from the form.
 *
 * Hidden along with the Google buttons when they are not available, so an
 * unconfigured build does not show a divider with nothing above it.
 */
@Composable
fun LabelledDivider(label: String, modifier: Modifier = Modifier) {
    if (!isGoogleSignInConfigured) return
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        HorizontalDivider(Modifier.weight(1f), color = MaterialTheme.colorScheme.outline)
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HorizontalDivider(Modifier.weight(1f), color = MaterialTheme.colorScheme.outline)
    }
}

/** Caps and centres a scrolling column so it stays readable on large screens. */
@Composable
fun ResponsiveContainer(
    modifier: Modifier = Modifier,
    maxWidth: Dp = MaxContentWidth,
    content: @Composable BoxScope.(Modifier) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        content(Modifier.widthIn(max = maxWidth))
    }
}
