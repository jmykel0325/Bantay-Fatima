@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.bantayfatima.app.ui.resident

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bantayfatima.app.data.model.UserDto
import com.bantayfatima.app.ui.components.*
import com.bantayfatima.app.ui.theme.AppTheme
import com.bantayfatima.app.ui.theme.Spacing

/**
 * Greeting row shown at the top of a signed-in dashboard.
 *
 * Light rather than a navy slab: once a resident is inside the app the branding
 * has already been established, so the space belongs to their information.
 */
@Composable
internal fun DashboardGreeting(
    user: UserDto,
    roleLabel: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StatusBadge(roleLabel, StatusTone.InProgress)
            }
            Text(
                text = "Good day, ${user.firstName}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        AccountAvatar(user)
    }
}

@Composable
private fun AccountAvatar(user: UserDto, size: Dp = 48.dp) {
    Box(
        Modifier
            .size(size)
            .clip(RoundedCornerShape(size / 2))
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials(user),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}

private fun initials(user: UserDto): String =
    "${user.firstName.firstOrNull() ?: ' '}${user.lastName.firstOrNull() ?: ' '}".trim().uppercase()

@Composable
fun ResidentDashboardScreen(user: UserDto, onCreateReport: () -> Unit) {
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
                    roleLabel = "Resident",
                    message = "Report community concerns and follow the barangay's response.",
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    PublicSectionHeader("Your Reports")
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                    ) {
                        SummaryCard("Total", "0", Icons.Filled.Description, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondaryContainer, Modifier.weight(1f))
                        SummaryCard("Pending", "0", Icons.Filled.Schedule, status.pending, status.pendingContainer, Modifier.weight(1f))
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                    ) {
                        SummaryCard("In Progress", "0", Icons.Filled.Sync, status.inProgress, status.inProgressContainer, Modifier.weight(1f))
                        SummaryCard("Resolved", "0", Icons.Filled.CheckCircle, status.resolved, status.resolvedContainer, Modifier.weight(1f))
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    PublicSectionHeader("Submit a Concern")
                    ServiceFeatureCard(
                        title = "Report a Community Concern",
                        description = "Send a description, supporting photo, and exact location to Barangay Fatima.",
                        icon = Icons.Filled.AddCircle,
                        onClick = onCreateReport,
                    )
                    PrimaryButton("Create Report", icon = Icons.Filled.AddCircle, onClick = onCreateReport)
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    PublicSectionHeader("Recent Reports")
                    EmptyState(
                        title = "No submitted reports",
                        description = "Reports you submit will be listed here with their current status.",
                        icon = Icons.AutoMirrored.Filled.Assignment,
                    )
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    PublicSectionHeader("Barangay Information")
                    ServiceFeatureCard("Latest Barangay Updates", "View published announcements and advisories.", Icons.Filled.Campaign)
                    ServiceFeatureCard("Bantay Fatima Assistant", "Get guidance about using barangay services.", Icons.Filled.SmartToy)
                }
            }

            item {
                EmergencyCard(icon = Icons.Filled.LocalHospital, onAction = {})
            }
        }
    }
}

@Composable
fun CreateReportPlaceholder(onBack: () -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { ScreenTopBar(title = "Create Report", subtitle = "Community concern", onBack = onBack) },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.screen)
                .padding(top = Spacing.xs, bottom = Spacing.xxl),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            InfoCard(
                title = "Form under construction",
                text = "Category, description, supporting photos, and location will be submitted securely to Barangay Fatima.",
                icon = Icons.Filled.Report,
            )
            EmptyState(
                title = "Reporting form not yet available",
                description = "This screen will be connected to the barangay reporting service in the next stage.",
                icon = Icons.Filled.EditNote,
            )
        }
    }
}

@Composable
fun MyReportsPlaceholder() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { ScreenTopBar(title = "My Reports", subtitle = "Submission history") },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Spacing.screen)
                .padding(top = Spacing.xs),
        ) {
            EmptyState(
                title = "No reports to display",
                description = "Submitted community reports will appear here with their status and updates.",
                icon = Icons.Filled.Description,
            )
        }
    }
}

@Composable
fun ProfileScreen(user: UserDto, onLogout: () -> Unit) {
    val roleLabel = user.role.replaceFirstChar(Char::uppercase)
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { ScreenTopBar(title = "Profile", subtitle = "Account details") },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.screen)
                .padding(top = Spacing.xs, bottom = Spacing.xxl),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            AppCard(Modifier.fillMaxWidth()) {
                Row(
                    Modifier.padding(Spacing.lg),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AccountAvatar(user, size = 60.dp)
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                        Text("${user.firstName} ${user.lastName}", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                            StatusBadge(roleLabel, StatusTone.InProgress)
                            StatusBadge(
                                text = user.status,
                                tone = if (user.status == "active") StatusTone.Resolved else StatusTone.Pending,
                            )
                        }
                    }
                }
            }

            PublicSectionHeader("Account")
            ServiceFeatureCard("Personal Information", "Update the details registered with the barangay.", Icons.Filled.Badge)
            ServiceFeatureCard("Notification Settings", "Choose which barangay notifications you receive.", Icons.Filled.Notifications)

            Spacer(Modifier.height(Spacing.xs))
            SecondaryButton("Log Out", icon = Icons.AutoMirrored.Filled.Logout, onClick = onLogout)
        }
    }
}
