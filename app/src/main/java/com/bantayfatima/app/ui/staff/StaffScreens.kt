@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.bantayfatima.app.ui.staff

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import android.net.Uri
import com.bantayfatima.app.data.model.ReportDto
import com.bantayfatima.app.data.model.StaffDashboard
import com.bantayfatima.app.data.model.StaffNotification
import com.bantayfatima.app.data.repository.StaffRepository
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bantayfatima.app.data.model.UserDto
import com.bantayfatima.app.ui.components.*
import com.bantayfatima.app.ui.resident.DashboardGreeting
import com.bantayfatima.app.ui.theme.AppTheme
import com.bantayfatima.app.ui.theme.Spacing

@Composable
fun StaffDashboardScreen(user: UserDto, onReport: (Long) -> Unit) {
    val repository = remember { StaffRepository() }
    var dashboard by remember { mutableStateOf<Result<StaffDashboard>?>(null) }
    var retry by remember { mutableIntStateOf(0) }
    LaunchedEffect(user.id, retry) { dashboard = repository.dashboard() }
    val counts = dashboard?.getOrNull()?.counts
    val recent = dashboard?.getOrNull()?.newlyAssigned.orEmpty()
    val status = AppTheme.status
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .widthIn(max = MaxContentWidth),
            contentPadding = PaddingValues(
                start = Spacing.screen,
                end = Spacing.screen,
                top = Spacing.md,
                bottom = Spacing.xxl,
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing.section),
        ) {
            item {
                DashboardGreeting(
                    user = user,
                    roleLabel = "Staff",
                    message = "Review assigned concerns and provide timely response updates.",
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    PublicSectionHeader("Assignment Summary")
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                    ) {
                        SummaryCard("Total Assigned", counts?.total?.toString() ?: "—", Icons.Filled.AssignmentInd, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondaryContainer, Modifier.weight(1f))
                        SummaryCard("Pending", counts?.pending?.toString() ?: "—", Icons.Filled.Schedule, status.pending, status.pendingContainer, Modifier.weight(1f))
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                    ) {
                        SummaryCard("In Progress", counts?.inProgress?.toString() ?: "—", Icons.Filled.Sync, status.inProgress, status.inProgressContainer, Modifier.weight(1f))
                        SummaryCard("Completed", counts?.completed?.toString() ?: "—", Icons.Filled.CheckCircle, status.resolved, status.resolvedContainer, Modifier.weight(1f))
                    }
                    SummaryCard(
                        label = "Awaiting Confirmation",
                        value = counts?.awaitingConfirmation?.toString() ?: "—",
                        icon = Icons.AutoMirrored.Filled.FactCheck,
                        accent = status.inProgress,
                        accentContainer = status.inProgressContainer,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            item {
                PublicSectionHeader("Newly Assigned Reports")
                FetchState(dashboard, recent.isEmpty(), "No assignments to display") {
                    dashboard = null
                    retry++
                }
            }
            items(recent, key = { it.id }) { StaffReportRow(it, onReport) }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    PublicSectionHeader("Reference")
                    ServiceFeatureCard("Barangay Updates", "Read published announcements and advisories.", Icons.Filled.Campaign)
                    ServiceFeatureCard("Emergency Information", "Open official emergency contact information.", Icons.Filled.LocalHospital)
                }
            }
        }
    }
}

/**
 * Assigned-report row.
 *
 * Reference number, priority and status are all rendered as text so the record
 * stays readable without relying on the badge colours.
 */
@Composable
fun AssignedReportCard(
    reference: String,
    title: String,
    purok: String,
    priority: String,
    statusLabel: String,
    assignedDate: String,
    modifier: Modifier = Modifier,
    onViewDetails: () -> Unit = {},
) {
    AppCard(modifier = modifier.fillMaxWidth(), onClick = onViewDetails) {
        Column(Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = reference,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.weight(1f))
                StatusBadge(priority, if (priority.equals("urgent", true)) StatusTone.Urgent else StatusTone.Neutral)
                StatusBadge(statusLabel, StatusTone.InProgress)
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MetaLine(Icons.Filled.Place, purok)
                MetaLine(Icons.Filled.Schedule, assignedDate)
            }
        }
    }
}

@Composable
private fun MetaLine(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xxs), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(15.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun AssignedReportsScreen(onReport: (Long) -> Unit) {
    val repository = remember { StaffRepository() }
    var reports by remember { mutableStateOf<List<ReportDto>>(emptyList()) }
    var page by remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    var retry by remember { mutableIntStateOf(0) }
    var result by remember { mutableStateOf<Result<Unit>?>(null) }
    LaunchedEffect(page, retry) {
        result = repository.assigned(page).map { response ->
            reports = (reports + response.data.orEmpty()).distinctBy { it.id }
            lastPage = response.meta?.lastPage ?: page
        }
    }
    Scaffold(topBar = { ScreenTopBar(title = "Assigned Reports") }) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(Spacing.screen),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            items(reports, key = { it.id }) { StaffReportRow(it, onReport) }
            item {
                FetchState(result, reports.isEmpty(), "No assignments to display") {
                    result = null
                    retry++
                }
                if (result?.isSuccess == true && page < lastPage) {
                    OutlinedButton(onClick = {
                        result = null
                        page++
                    }) { Text("Load more reports") }
                }
            }
        }
    }
}

@Composable
private fun StaffReportRow(report: ReportDto, onReport: (Long) -> Unit) {
    AssignedReportCard(
        reference = report.referenceNumber ?: "#" + report.id,
        title = report.title ?: "Report",
        purok = report.purok?.name.orEmpty(),
        priority = report.priority.orEmpty(),
        statusLabel = report.status.orEmpty().replace('_', ' '),
        assignedDate = report.updatedAt?.take(10).orEmpty(),
        onViewDetails = { onReport(report.id) },
    )
}

@Composable
private fun FetchState(result: Result<*>?, empty: Boolean, emptyMessage: String, retry: () -> Unit) {
    when {
        result == null -> CircularProgressIndicator(Modifier.size(24.dp))
        result.isFailure -> {
            Text(result.exceptionOrNull()?.message ?: "Unable to load.", color = MaterialTheme.colorScheme.error)
            OutlinedButton(onClick = retry) { Text("Retry") }
        }
        empty -> Text(emptyMessage, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun StaffNotificationsScreen() {
    val repository = remember { StaffRepository() }
    val scope = rememberCoroutineScope()
    var result by remember { mutableStateOf<Result<List<StaffNotification>>?>(null) }
    var retry by remember { mutableIntStateOf(0) }
    var marking by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(retry) { result = repository.notifications() }
    val notifications = result?.getOrNull().orEmpty()
    Scaffold(topBar = { ScreenTopBar(title = "Notifications") }) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(Spacing.screen),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            item {
                FetchState(result, notifications.isEmpty(), "No notifications") {
                    result = null
                    retry++
                }
                if (notifications.any { it.readAt == null }) {
                    OutlinedButton(enabled = !marking, onClick = {
                        marking = true
                        error = null
                        scope.launch {
                            try {
                                repository.markNotificationsRead().fold(
                                    { result = repository.notifications() },
                                    { error = it.message ?: "Could not mark notifications as read." },
                                )
                            } finally {
                                marking = false
                            }
                        }
                    }) { Text("Mark all as read") }
                }
                ErrorBanner(error, onDismiss = { error = null })
            }
            items(notifications, key = { it.id }) { notice ->
                AppCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        Text(notice.data?.title ?: "Notification", style = MaterialTheme.typography.titleMedium)
                        notice.data?.message?.let { Text(it) }
                        if (notice.readAt == null) Text("Unread", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
fun StaffReportDetailScreen(id: Long, onBack: () -> Unit) {
    val repository = remember { StaffRepository() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var result by remember { mutableStateOf<Result<ReportDto>?>(null) }
    var retry by remember { mutableIntStateOf(0) }
    var status by rememberSaveable { mutableStateOf("in_progress") }
    var notes by rememberSaveable { mutableStateOf("") }
    var photo by remember { mutableStateOf<Uri?>(null) }
    var busy by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var saved by remember { mutableStateOf(false) }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) photo = uri
    }
    LaunchedEffect(id, retry) { result = repository.report(id) }
    Scaffold(topBar = { ScreenTopBar(title = "Report Details", onBack = onBack) }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(Spacing.screen),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            FetchState(result, false, "") { result = null; retry++ }
            result?.getOrNull()?.let { report ->
                Text(report.title ?: "Report", style = MaterialTheme.typography.headlineSmall)
                report.referenceNumber?.let { Text(it) }
                report.description?.let { Text(it) }
                report.status?.let { Text(it.replace('_', ' '), style = MaterialTheme.typography.labelLarge) }
                if (saved) Text("Field update saved.")
                ErrorBanner(error, onDismiss = { error = null })
                if (report.status !in listOf("resolved", "completed", "rejected")) {
                    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                        FilterChip(status == "in_progress", { status = "in_progress" }, { Text("In progress") }, enabled = !busy)
                        FilterChip(status == "resolved", { status = "resolved" }, { Text("Resolved") }, enabled = !busy)
                    }
                    OutlinedTextField(
                        notes, { notes = it.take(5000) },
                        label = { Text("Progress notes") },
                        modifier = Modifier.fillMaxWidth(), enabled = !busy,
                    )
                    if (status == "resolved") {
                        OutlinedButton(enabled = !busy, onClick = { picker.launch("image/*") }) {
                            Text(if (photo == null) "Choose required proof photo" else "Change proof photo")
                        }
                    }
                    PrimaryButton(
                        text = "Save field update",
                        loading = busy,
                        enabled = notes.trim().length >= 5 && (status != "resolved" || photo != null),
                        onClick = {
                            if (busy) return@PrimaryButton
                            busy = true
                            error = null
                            saved = false
                            scope.launch {
                                try {
                                    val proof = if (status == "resolved") photo else null
                                    repository.progress(context, id, status, notes, proof).fold(
                                        {
                                            result = Result.success(it)
                                            notes = ""
                                            photo = null
                                            saved = true
                                        },
                                        { error = it.message ?: "Could not save the field update." },
                                    )
                                } finally {
                                    busy = false
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}
