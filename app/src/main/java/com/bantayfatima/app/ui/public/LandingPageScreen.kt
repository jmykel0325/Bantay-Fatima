package com.bantayfatima.app.ui.public

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.bantayfatima.app.data.repository.PublicContentRepository

private val CivicNavy = Color(0xFF0B2D4D)
private val CivicGreen = Color(0xFF0F8877)
private val CivicBackground = Color(0xFFF4F7F9)
private val CivicBorder = Color(0xFFDCE4EA)
private val CivicMuted = Color(0xFF6B8196)

private data class CivicService(
    val label: String,
    val icon: ImageVector,
    val tint: Color,
    val background: Color,
    val onClick: () -> Unit,
)

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

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(CivicBackground),
        contentPadding = PaddingValues(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { CivicHeader(onAccount) }
        item { ServicesSection(onProtected, onAbout, onUpdates, onEmergency) }
        item { LatestUpdatesSection(updates, onUpdates) }
        item { PublicNotice(onEmergency) }
    }
}

@Composable
private fun CivicHeader(onAccount: () -> Unit) {
    Box(Modifier.fillMaxWidth().height(198.dp)) {
        Column(
            Modifier.fillMaxWidth().height(166.dp).background(CivicNavy).padding(horizontal = 20.dp, vertical = 18.dp),
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
                Text("Bantay Fatima", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                IconButton(onClick = {}, modifier = Modifier.size(42.dp)) { Icon(Icons.Outlined.Notifications, "Notifications", tint = Color.White) }
                IconButton(onClick = onAccount, modifier = Modifier.size(42.dp)) { Icon(Icons.Outlined.AccountCircle, "Account", tint = Color.White) }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = CivicGreen, modifier = Modifier.size(38.dp)) {
                    Icon(Icons.Outlined.Campaign, null, tint = Color.White, modifier = Modifier.padding(8.dp))
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Good day!", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("How can Bantay Fatima assist you?", color = Color(0xFFD6E5F1), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        Surface(
            shape = RoundedCornerShape(16.dp), color = Color.White,
            shadowElevation = 4.dp, border = BorderStroke(1.dp, CivicBorder),
            modifier = Modifier.align(Alignment.BottomCenter).padding(horizontal = 20.dp).fillMaxWidth().height(58.dp),
        ) {
            Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Search, "Search", tint = CivicMuted, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(10.dp))
                Text("Search services, announcements, reports...", color = CivicMuted, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun ServicesSection(onProtected: () -> Unit, onAbout: () -> Unit, onUpdates: () -> Unit, onEmergency: () -> Unit) {
    val services = listOf(
        CivicService("Report\nConcern", Icons.Outlined.EditNote, CivicGreen, Color(0xFFE4F7F4), onProtected),
        CivicService("My\nReports", Icons.AutoMirrored.Outlined.Assignment, Color(0xFF087483), Color(0xFFEAF7F7), onProtected),
        CivicService("Announcements", Icons.Outlined.Campaign, Color(0xFF1876D2), Color(0xFFE9F2FD), onUpdates),
        CivicService("Emergency", Icons.Outlined.Emergency, Color(0xFFE53935), Color(0xFFFFECE9), onEmergency),
        CivicService("How It\nWorks", Icons.AutoMirrored.Outlined.HelpOutline, Color(0xFFB87500), Color(0xFFFFF5E4), onAbout),
        CivicService("Assistant", Icons.Outlined.SupportAgent, Color(0xFF1876D2), Color(0xFFE8F2FF), onProtected),
        CivicService("Support", Icons.Outlined.HeadsetMic, CivicNavy, Color(0xFFE8F3FF), onAbout),
        CivicService("View All", Icons.Outlined.Apps, Color(0xFF4B5264), Color(0xFFF0F1F3), onAbout),
    )
    Surface(
        color = Color.White, shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, CivicBorder), shadowElevation = 1.dp,
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            services.chunked(4).forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    row.forEach { service -> CivicServiceItem(service, Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
private fun CivicServiceItem(service: CivicService, modifier: Modifier = Modifier) {
    Card(
        onClick = service.onClick,
        modifier = modifier.aspectRatio(.8f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = service.background),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(
            Modifier.fillMaxSize().padding(horizontal = 3.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(service.icon, service.label.replace("\n", " "), tint = service.tint, modifier = Modifier.size(30.dp))
            Text(service.label, color = CivicNavy, fontSize = 8.sp, lineHeight = 9.sp, fontWeight = FontWeight.Medium, maxLines = 2)
        }
    }
}

@Composable
private fun LatestUpdatesSection(updates: Result<List<PublicUpdate>>?, onUpdates: () -> Unit) {
    Surface(
        color = Color.White, shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, CivicBorder), shadowElevation = 1.dp,
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Latest Barangay Updates", color = CivicNavy, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                OutlinedButton(onClick = onUpdates, shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)) { Text("View All", fontSize = 11.sp) }
            }
            Spacer(Modifier.height(8.dp))
            when {
                updates == null -> LinearProgressIndicator(Modifier.fillMaxWidth())
                updates.isFailure -> CompactEmptyRow(Icons.Outlined.CloudOff, "Updates are temporarily unavailable.")
                updates.getOrNull().isNullOrEmpty() -> CompactEmptyRow(Icons.Outlined.Article, "No published updates at this time.")
                else -> updates.getOrThrow().take(3).forEachIndexed { index, update ->
                    CompactUpdateRow(update)
                    if (index < minOf(2, updates.getOrThrow().lastIndex)) HorizontalDivider(color = CivicBorder)
                }
            }
        }
    }
}

@Composable
private fun CompactUpdateRow(update: PublicUpdate) {
    Row(Modifier.fillMaxWidth().padding(vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFE8F4F2), modifier = Modifier.size(38.dp)) {
            Icon(if (update.isUrgent) Icons.Outlined.Warning else Icons.Outlined.Article, if (update.isUrgent) "Urgent update" else null, tint = if (update.isUrgent) MaterialTheme.colorScheme.error else CivicGreen, modifier = Modifier.padding(8.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(update.title, color = CivicNavy, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(update.type ?: "Barangay update", color = CivicMuted, fontSize = 11.sp, maxLines = 1)
        }
        update.publishedAt?.let { Text(it.take(10), color = CivicMuted, fontSize = 10.sp) }
    }
}

@Composable
private fun CompactEmptyRow(icon: ImageVector, message: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = CivicMuted); Spacer(Modifier.width(10.dp)); Text(message, color = CivicMuted, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun PublicNotice(onEmergency: () -> Unit) {
    Surface(
        color = Color(0xFFF9FBFC), shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, CivicBorder),
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Info, "Information", tint = CivicGreen)
            Spacer(Modifier.width(10.dp))
            Text("For urgent situations, contact the appropriate emergency service.", color = CivicNavy, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
            TextButton(onClick = onEmergency) { Text("Hotlines", fontSize = 11.sp) }
        }
    }
}
