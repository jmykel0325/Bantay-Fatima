@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.bantayfatima.app.ui.staff

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun StaffDashboardScreen(user: UserDto) {
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
                        SummaryCard("Total Assigned", "0", Icons.Filled.AssignmentInd, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondaryContainer, Modifier.weight(1f))
                        SummaryCard("Pending", "0", Icons.Filled.Schedule, status.pending, status.pendingContainer, Modifier.weight(1f))
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                    ) {
                        SummaryCard("In Progress", "0", Icons.Filled.Sync, status.inProgress, status.inProgressContainer, Modifier.weight(1f))
                        SummaryCard("Completed", "0", Icons.Filled.CheckCircle, status.resolved, status.resolvedContainer, Modifier.weight(1f))
                    }
                    SummaryCard(
                        label = "Awaiting Confirmation",
                        value = "0",
                        icon = Icons.AutoMirrored.Filled.FactCheck,
                        accent = status.inProgress,
                        accentContainer = status.inProgressContainer,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    PublicSectionHeader("Urgent Assignments")
                    EmptyState(
                        title = "No urgent assignments",
                        description = "Reports flagged as urgent will be highlighted here.",
                        icon = Icons.Filled.Warning,
                    )
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    PublicSectionHeader("Newly Assigned Reports")
                    EmptyState(
                        title = "No assignments to display",
                        description = "Reports assigned to your staff account will appear here.",
                        icon = Icons.AutoMirrored.Filled.Assignment,
                    )
                }
            }

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
fun AssignedReportsScreen() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { ScreenTopBar(title = "Assigned Reports", subtitle = "Your active caseload") },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Spacing.screen)
                .padding(top = Spacing.xs),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            InfoCard(
                text = "Only reports assigned to your staff account are shown. Progress updates you submit are recorded against your account.",
                icon = Icons.Filled.VerifiedUser,
            )
            EmptyState(
                title = "No assignments to display",
                description = "Newly assigned community reports will be listed here.",
                icon = Icons.Filled.AssignmentInd,
            )
        }
    }
}

@Composable
fun StaffNotificationsScreen() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { ScreenTopBar(title = "Notifications", subtitle = "Assignments and updates") },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Spacing.screen)
                .padding(top = Spacing.xs),
        ) {
            EmptyState(
                title = "No notifications",
                description = "Assignment and progress notifications will appear here.",
                icon = Icons.Filled.Notifications,
            )
        }
    }
}
