package com.bantayfatima.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Widest a column of content is allowed to become.
 *
 * On a phone this never applies. On a tablet or an unfolded device it stops text
 * lines from running the full width of the display, which is uncomfortable to
 * read and makes the layout look stretched rather than designed.
 */
val MaxContentWidth: Dp = 640.dp

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
