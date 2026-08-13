package com.bantayfatima.app.ui.public

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Emergency
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bantayfatima.app.data.model.PublicUpdate
import com.bantayfatima.app.data.repository.PublicContentRepository
import com.bantayfatima.app.ui.components.*
import com.bantayfatima.app.ui.theme.BantayBackground

/**
 * Public home for a guest.
 *
 * Built from the shared civic components so it and the signed-in dashboard are the
 * same screen with different content: the tiles here lead to the sign-in sheet
 * wherever an account is required.
 */
@Composable
fun LandingPageScreen(
    onProtected: () -> Unit,
    onAbout: () -> Unit,
    onUpdates: () -> Unit,
    onEmergency: () -> Unit,
    onAccount: () -> Unit,
    repository: PublicContentRepository = remember { PublicContentRepository() },
) {
    var updates by remember { mutableStateOf<Result<List<PublicUpdate>>?>(null) }
    LaunchedEffect(Unit) { updates = repository.updates() }

    val actions = listOf(
        CivicAction("Report\nConcern", Icons.Outlined.EditNote, CivicTilePrimary, onProtected),
        CivicAction("My\nReports", Icons.AutoMirrored.Outlined.Assignment, CivicTileNavy, onProtected),
        CivicAction("Announcements", Icons.Outlined.Campaign, CivicTileInfo, onUpdates),
        CivicAction("Emergency", Icons.Outlined.Emergency, CivicTileEmergency, onEmergency),
        CivicAction("How It\nWorks", Icons.AutoMirrored.Outlined.HelpOutline, CivicTileSky, onAbout),
        CivicAction("Assistant", Icons.Outlined.SupportAgent, CivicTileIndigo, onProtected),
        CivicAction("Support", Icons.Outlined.HeadsetMic, CivicTileNeutral, onAbout),
        CivicAction("View All", Icons.Outlined.Apps, CivicTileMuted, onAbout),
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(BantayBackground),
        contentPadding = PaddingValues(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            CivicHeader(
                greeting = "Good day!",
                subtitle = "How can Bantay Fatima assist you?",
                onAccount = onAccount,
            )
        }
        item { CivicActionGrid(actions) }
        item { CivicUpdatesSection(updates, onUpdates) }
        item { CivicNotice(onEmergency) }
    }
}
