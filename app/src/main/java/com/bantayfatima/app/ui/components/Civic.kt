package com.bantayfatima.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bantayfatima.app.R
import com.bantayfatima.app.data.model.PublicUpdate
import com.bantayfatima.app.ui.theme.BantayBackground
import com.bantayfatima.app.ui.theme.BantayBorder
import com.bantayfatima.app.ui.theme.BantayIndigo
import com.bantayfatima.app.ui.theme.BantayIndigoSoft
import com.bantayfatima.app.ui.theme.BantayLightBlue
import com.bantayfatima.app.ui.theme.BantayOnNavyMuted
import com.bantayfatima.app.ui.theme.BantayNavySoft
import com.bantayfatima.app.ui.theme.BantayPrimarySoft
import com.bantayfatima.app.ui.theme.BantaySky
import com.bantayfatima.app.ui.theme.BantaySkySoft
import com.bantayfatima.app.ui.theme.BantaySlate
import com.bantayfatima.app.ui.theme.BantaySlateSoft
import com.bantayfatima.app.ui.theme.StatusNeutralSoft
import com.bantayfatima.app.ui.theme.StatusUrgent
import com.bantayfatima.app.ui.theme.StatusUrgentSoft
import com.bantayfatima.app.ui.theme.BantayPrimary
import com.bantayfatima.app.ui.theme.BantayMuted
import com.bantayfatima.app.ui.theme.BantayNavy
import com.bantayfatima.app.ui.theme.BantayWhite

/**
 * The shared "civic home" language.
 *
 * A resident sees one of these screens before signing in and a different one after,
 * and the two must read as the same application: the same navy header, the same
 * search bar, the same tile grid and the same white section cards. Everything a home
 * screen is built from lives here so the guest landing page and the signed-in
 * dashboard cannot drift apart again — what changes between them is the content,
 * never the shell.
 */

// Accent pairs for the tiles: icon colour, then the pale background behind it.
//
// Variation comes from moving through the blue range rather than from separate
// hues, which keeps eight tiles distinguishable without the grid turning into a
// colour chart. Emergency is the one exception and stays red — an urgent action
// recoloured to match everything else would no longer read as urgent.
val CivicTilePrimary = BantayPrimary to BantayPrimarySoft
val CivicTileIndigo = BantayIndigo to BantayIndigoSoft
val CivicTileInfo = BantayLightBlue to BantaySkySoft
val CivicTileEmergency = StatusUrgent to StatusUrgentSoft
val CivicTileSky = BantaySky to BantaySkySoft
val CivicTileNavy = BantayNavy to BantayNavySoft
val CivicTileNeutral = BantaySlate to BantaySlateSoft
val CivicTileMuted = BantayMuted to StatusNeutralSoft

/** One tile in the [CivicActionGrid]. */
data class CivicAction(
    val label: String,
    val icon: ImageVector,
    val accent: Pair<Color, Color>,
    val onClick: () -> Unit,
)

/**
 * Navy masthead with the greeting, and a search bar straddling its lower edge.
 *
 * [greeting] is the only part that differs between guest and resident — "Good day!"
 * before signing in, the resident's own name after.
 */
@Composable
fun CivicHeader(
    greeting: String,
    subtitle: String,
    onAccount: () -> Unit,
    modifier: Modifier = Modifier,
    onNotifications: () -> Unit = {},
    trailing: (@Composable () -> Unit)? = null,
) {
    Box(modifier.fillMaxWidth().height(198.dp)) {
        Column(
            Modifier
                .fillMaxWidth()
                .height(166.dp)
                .background(
                    color = BantayNavy,
                    shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
                )
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.bantay_fatima_logo),
                    contentDescription = "Bantay Fatima logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)),
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Bantay Fatima",
                    color = BantayWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onNotifications, modifier = Modifier.size(42.dp)) {
                    Icon(Icons.Outlined.Notifications, "Notifications", tint = BantayWhite)
                }
                if (trailing != null) trailing() else {
                    IconButton(onClick = onAccount, modifier = Modifier.size(42.dp)) {
                        Icon(Icons.Outlined.AccountCircle, "Account", tint = BantayWhite)
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = BantayPrimary, modifier = Modifier.size(38.dp)) {
                    Icon(Icons.Outlined.Campaign, null, tint = BantayWhite, modifier = Modifier.padding(8.dp))
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = greeting,
                        color = BantayWhite,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = subtitle,
                        color = BantayOnNavyMuted,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BantayWhite,
            shadowElevation = 4.dp,
            border = BorderStroke(1.dp, BantayBorder),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(58.dp),
        ) {
            Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Search, "Search", tint = BantayMuted, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Search services, announcements, reports...",
                    color = BantayMuted,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

/** White card that every civic section sits in. */
@Composable
fun CivicCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        color = BantayWhite,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, BantayBorder),
        shadowElevation = 1.dp,
        modifier = modifier.padding(horizontal = 16.dp).fillMaxWidth(),
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp), content = content)
    }
}

/** Four-per-row grid of service tiles. */
@Composable
fun CivicActionGrid(actions: List<CivicAction>, modifier: Modifier = Modifier) {
    CivicCard(modifier) {
        actions.chunked(4).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                row.forEach { action -> CivicTile(action, Modifier.weight(1f)) }
                // Keeps a short final row aligned with the rows above it.
                repeat(4 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun CivicTile(action: CivicAction, modifier: Modifier = Modifier) {
    Card(
        onClick = action.onClick,
        modifier = modifier.aspectRatio(.8f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = action.accent.second),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(
            Modifier.fillMaxSize().padding(horizontal = 3.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(action.icon, action.label.replace("\n", " "), tint = action.accent.first, modifier = Modifier.size(30.dp))
            Text(
                text = action.label,
                color = BantayNavy,
                fontSize = 8.sp,
                lineHeight = 9.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
            )
        }
    }
}

/** Section card with a title row and an optional trailing action. */
@Composable
fun CivicSection(
    title: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        color = BantayWhite,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BantayBorder),
        shadowElevation = 1.dp,
        modifier = modifier.padding(horizontal = 16.dp).fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    color = BantayNavy,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                if (actionLabel != null && onAction != null) {
                    OutlinedButton(
                        onClick = onAction,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    ) { Text(actionLabel, fontSize = 11.sp) }
                }
            }
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

/** Count tile used by the signed-in dashboard, drawn to match [CivicTile]. */
@Composable
fun CivicStatTile(
    label: String,
    value: String,
    icon: ImageVector,
    accent: Pair<Color, Color>,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = accent.second,
        modifier = modifier,
    ) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(icon, null, tint = accent.first, modifier = Modifier.size(22.dp))
            Text(value, color = BantayNavy, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(label, color = BantayMuted, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

/** "Latest Barangay Updates", identical on both home screens. */
@Composable
fun CivicUpdatesSection(
    updates: Result<List<PublicUpdate>>?,
    onUpdates: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CivicSection("Latest Barangay Updates", modifier, actionLabel = "View All", onAction = onUpdates) {
        when {
            updates == null -> LinearProgressIndicator(Modifier.fillMaxWidth())
            updates.isFailure -> CivicMessageRow(Icons.Outlined.CloudOff, "Updates are temporarily unavailable.")
            updates.getOrNull().isNullOrEmpty() ->
                CivicMessageRow(Icons.AutoMirrored.Outlined.Article, "No published updates at this time.")
            else -> {
                val items = updates.getOrDefault(emptyList()).take(3)
                items.forEachIndexed { index, update ->
                    CivicUpdateRow(update)
                    if (index < items.lastIndex) HorizontalDivider(color = BantayBorder)
                }
            }
        }
    }
}

@Composable
private fun CivicUpdateRow(update: PublicUpdate) {
    Row(Modifier.fillMaxWidth().padding(vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = RoundedCornerShape(8.dp), color = BantayPrimarySoft, modifier = Modifier.size(38.dp)) {
            Icon(
                imageVector = if (update.isUrgent) Icons.Outlined.Warning else Icons.AutoMirrored.Outlined.Article,
                contentDescription = if (update.isUrgent) "Urgent update" else null,
                tint = if (update.isUrgent) MaterialTheme.colorScheme.error else BantayPrimary,
                modifier = Modifier.padding(8.dp),
            )
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = update.title,
                color = BantayNavy,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(update.type ?: "Barangay update", color = BantayMuted, fontSize = 11.sp, maxLines = 1)
        }
        update.publishedAt?.let { Text(it.take(10), color = BantayMuted, fontSize = 10.sp) }
    }
}

@Composable
fun CivicMessageRow(icon: ImageVector, message: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = BantayMuted)
        Spacer(Modifier.width(10.dp))
        Text(message, color = BantayMuted, style = MaterialTheme.typography.bodySmall)
    }
}

/** Closing advisory strip. */
@Composable
fun CivicNotice(onEmergency: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        color = BantayBackground,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, BantayBorder),
        modifier = modifier.padding(horizontal = 16.dp).fillMaxWidth(),
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Info, "Information", tint = BantayPrimary)
            Spacer(Modifier.width(10.dp))
            Text(
                text = "For urgent situations, contact the appropriate emergency service.",
                color = BantayNavy,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = onEmergency) { Text("Hotlines", fontSize = 11.sp) }
        }
    }
}
