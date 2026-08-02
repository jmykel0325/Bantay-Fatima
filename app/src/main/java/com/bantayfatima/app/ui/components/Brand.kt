package com.bantayfatima.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bantayfatima.app.R
import com.bantayfatima.app.ui.theme.OverlineLabel
import com.bantayfatima.app.ui.theme.Spacing

/**
 * Approved brand marks, copied from the Bantay Fatima Laravel application so the
 * mobile and web services present the same identity.
 */

/** Official seal of Barangay Fatima, General Santos City. */
@Composable
fun BarangaySeal(size: Dp, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.barangay_seal),
        contentDescription = "Official seal of Barangay Fatima, General Santos City",
        modifier = modifier.size(size),
    )
}

/**
 * Bantay Fatima wordmark.
 *
 * Pass a null [contentDescription] wherever the application name already appears
 * as text beside it, so screen readers do not announce it twice.
 */
@Composable
fun BantayFatimaLogo(size: Dp, modifier: Modifier = Modifier, contentDescription: String? = null) {
    Image(
        painter = painterResource(R.drawable.bantay_fatima_logo),
        contentDescription = contentDescription,
        modifier = modifier.size(size),
    )
}

/**
 * Institutional lockup: the seal beside the issuing authority.
 *
 * @param onDark true when placed on a navy surface.
 */
@Composable
fun AuthorityLockup(
    modifier: Modifier = Modifier,
    sealSize: Dp = 44.dp,
    onDark: Boolean = false,
) {
    val title = if (onDark) Color.White else MaterialTheme.colorScheme.secondary
    val subtitle = if (onDark) Color.White.copy(alpha = 0.74f) else MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        BarangaySeal(sealSize)
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("BARANGAY FATIMA", style = OverlineLabel, color = title)
            Text(
                text = "General Santos City",
                style = MaterialTheme.typography.labelMedium,
                color = subtitle,
            )
        }
    }
}
