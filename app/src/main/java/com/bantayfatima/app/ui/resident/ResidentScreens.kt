@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.bantayfatima.app.ui.resident

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Emergency
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bantayfatima.app.data.model.PublicUpdate
import com.bantayfatima.app.data.model.ReportCounts
import com.bantayfatima.app.data.model.ReportDto
import com.bantayfatima.app.data.model.ResidentDashboard
import com.bantayfatima.app.data.model.UserDto
import com.bantayfatima.app.data.repository.AssistantReply
import com.bantayfatima.app.data.repository.PublicContentRepository
import com.bantayfatima.app.data.repository.ResidentRepository
import com.bantayfatima.app.ui.components.*
import com.bantayfatima.app.ui.theme.AppTheme
import com.bantayfatima.app.ui.theme.BantayBackground
import com.bantayfatima.app.ui.theme.BantayBorder
import com.bantayfatima.app.ui.theme.BantayPrimary
import com.bantayfatima.app.ui.theme.BantayPrimarySoft
import com.bantayfatima.app.ui.theme.BantayMuted
import com.bantayfatima.app.ui.theme.BantayNavy
import com.bantayfatima.app.ui.theme.Spacing
import kotlinx.coroutines.launch

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

/**
 * Home for a signed-in resident.
 *
 * The same screen as the guest landing page, not a second design: the navy header,
 * the search bar, the tile grid, the updates section and the advisory strip are the
 * shared civic components, used here exactly as the public home uses them.
 *
 * Signing in changes content and access, not layout. The greeting carries the
 * resident's name, the last tile becomes My Profile now that there is a profile to
 * open, the tiles that raised the sign-in sheet for a guest now go straight through,
 * and two sections appear that only make sense with an account: their report counts
 * and their latest reports. Nothing that already exists in the grid or in the bottom
 * bar is repeated here as a standalone card.
 */
@Composable
fun ResidentDashboardScreen(
    user: UserDto,
    onCreateReport: () -> Unit,
    onMyReports: () -> Unit,
    onUpdates: () -> Unit,
    onEmergency: () -> Unit,
    onProfile: () -> Unit,
    onAssistant: () -> Unit,
    onHowItWorks: () -> Unit,
    onSupport: () -> Unit,
    publicContent: PublicContentRepository = remember { PublicContentRepository() },
    residentRepository: ResidentRepository = remember { ResidentRepository() },
) {
    var updates by remember { mutableStateOf<Result<List<PublicUpdate>>?>(null) }
    var dashboard by remember { mutableStateOf<Result<ResidentDashboard>?>(null) }
    LaunchedEffect(Unit) {
        dashboard = residentRepository.dashboard()
        updates = publicContent.updates()
    }

    val actions = listOf(
        CivicAction("Report\nConcern", Icons.Outlined.EditNote, CivicTilePrimary, onCreateReport),
        CivicAction("My\nReports", Icons.AutoMirrored.Outlined.Assignment, CivicTileNavy, onMyReports),
        CivicAction("Announcements", Icons.Outlined.Campaign, CivicTileInfo, onUpdates),
        CivicAction("Emergency", Icons.Outlined.Emergency, CivicTileEmergency, onEmergency),
        CivicAction("How It\nWorks", Icons.AutoMirrored.Outlined.HelpOutline, CivicTileSky, onHowItWorks),
        CivicAction("Assistant", Icons.Outlined.SupportAgent, CivicTileIndigo, onAssistant),
        CivicAction("Support", Icons.Outlined.HeadsetMic, CivicTileNeutral, onSupport),
        CivicAction("My\nProfile", Icons.Outlined.Person, CivicTileMuted, onProfile),
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(BantayBackground),
        contentPadding = PaddingValues(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            CivicHeader(
                greeting = "Good day, ${user.firstName}!",
                subtitle = "Resident account · Barangay Fatima",
                onAccount = onProfile,
                onNotifications = onProfile,
            )
        }
        item { CivicActionGrid(actions) }
        item { YourReportsSection(dashboard, onMyReports) }
        item { RecentReportsSection(dashboard, onMyReports) }
        item { CivicUpdatesSection(updates, onUpdates) }
        item { CivicNotice(onEmergency) }
    }
}

/**
 * Report counts, from the API.
 *
 * One card holding four short indicators rather than four cards. It carries no
 * action of its own beyond "View All" — creating a report is the grid's first tile
 * and the raised button in the bottom bar, and does not need a third entrance here.
 */
@Composable
private fun YourReportsSection(dashboard: Result<ResidentDashboard>?, onMyReports: () -> Unit) {
    val status = AppTheme.status
    val counts = dashboard?.getOrNull()?.counts

    CivicSection("Your Reports", actionLabel = "View All", onAction = onMyReports) {
        if (dashboard?.isFailure == true) {
            CivicMessageRow(Icons.Outlined.CloudOff, "Your report summary is temporarily unavailable.")
        } else {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                StatIndicator("Total", counts?.total, Icons.Outlined.Description, MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
                StatIndicator("Pending", counts?.pending, Icons.Outlined.Schedule, status.pending, Modifier.weight(1f))
                StatIndicator("In Progress", counts?.inProgress, Icons.Outlined.Sync, status.inProgress, Modifier.weight(1f))
                StatIndicator("Resolved", counts?.resolved, Icons.Outlined.CheckCircle, status.resolved, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StatIndicator(
    label: String,
    value: Int?,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(vertical = Spacing.xxs), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
        Text(
            // A dash until the API answers, so the card never reports zero before the
            // server has been asked.
            text = value?.toString() ?: "—",
            style = MaterialTheme.typography.titleMedium,
            color = BantayNavy,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, lineHeight = 12.sp),
            color = BantayMuted,
            maxLines = 2,
        )
    }
}

/** The two most recent reports. Information, not another way to navigate. */
@Composable
private fun RecentReportsSection(dashboard: Result<ResidentDashboard>?, onMyReports: () -> Unit) {
    val recent = dashboard?.getOrNull()?.recentReports.orEmpty().take(2)

    CivicSection(
        title = "Recent Reports",
        actionLabel = if (recent.isNotEmpty()) "View All" else null,
        onAction = onMyReports,
    ) {
        when {
            dashboard == null -> CivicMessageRow(Icons.Outlined.Schedule, "Loading your reports...")
            dashboard.isFailure ->
                CivicMessageRow(Icons.Outlined.CloudOff, "Your reports could not be loaded right now.")
            recent.isEmpty() -> CivicMessageRow(
                Icons.AutoMirrored.Outlined.Assignment,
                "You have not submitted any reports yet.",
            )
            else -> Column {
                recent.forEachIndexed { index, report ->
                    ReportRow(report, onMyReports)
                    if (index < recent.lastIndex) HorizontalDivider(color = BantayBorder)
                }
            }
        }
    }
}

@Composable
private fun ReportRow(report: ReportDto, onOpen: () -> Unit) {
    val (tone, statusLabel) = reportStatusTone(report.status)
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen)
            .padding(vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Photos arrive as storage paths and the project carries no image loader, so
        // the thumbnail slot holds a glyph and the row keeps its rhythm either way.
        Surface(shape = RoundedCornerShape(8.dp), color = BantayPrimarySoft, modifier = Modifier.size(38.dp)) {
            Icon(
                Icons.Outlined.Description,
                contentDescription = null,
                tint = BantayPrimary,
                modifier = Modifier.padding(9.dp),
            )
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = report.title ?: report.category?.name ?: "Community report",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = BantayNavy,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = listOfNotNull(
                    report.referenceNumber,
                    // Puroks are stored by number alone ("1"), which reads as noise
                    // between the reference and the date without the word in front.
                    report.purok?.name?.let { if (it.all(Char::isDigit)) "Purok $it" else it },
                    (report.updatedAt ?: report.createdAt)?.take(10),
                ).joinToString(" · "),
                style = MaterialTheme.typography.labelSmall,
                color = BantayMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            StatusBadge(statusLabel, tone)
        }
        Icon(
            Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = BantayMuted,
            modifier = Modifier.size(20.dp),
        )
    }
}

private fun reportStatusTone(status: String?): Pair<StatusTone, String> = when (status) {
    "pending" -> StatusTone.Pending to "Pending"
    "in_progress" -> StatusTone.InProgress to "In Progress"
    "resolved" -> StatusTone.Resolved to "Resolved"
    "rejected" -> StatusTone.Urgent to "Rejected"
    else -> StatusTone.Neutral to (status?.replace('_', ' ') ?: "Unknown")
}

/**
 * Barangay knowledge assistant.
 *
 * Posts to `resident/assistant/query` and shows the answer with the documents it was
 * drawn from. The sources are part of the answer, not decoration: a resident acting on
 * barangay guidance should be able to see where it came from. When the knowledge base
 * is unavailable the API replies 503 with an explanation, which surfaces here as the
 * error rather than as an empty answer.
 */
@Composable
fun AssistantScreen(
    onBack: () -> Unit,
    repository: ResidentRepository = remember { ResidentRepository() },
) {
    var question by rememberSaveable { mutableStateOf("") }
    var reply by remember { mutableStateOf<AssistantReply?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var busy by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val ask = {
        if (question.trim().length >= 3 && !busy) {
            scope.launch {
                busy = true
                error = null
                repository.askAssistant(question).fold(
                    { reply = it },
                    { reply = null; error = it.message ?: "The assistant is unavailable." },
                )
                busy = false
            }
            Unit
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { ScreenTopBar(title = "Bantay Fatima Assistant", subtitle = "Barangay information", onBack = onBack) },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = Spacing.screen)
                .padding(top = Spacing.xs, bottom = Spacing.xxl),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            Text(
                text = "Answers are drawn from approved Barangay Fatima documents, advisories, and emergency information.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            AppTextField(
                value = question,
                onValueChange = { question = it },
                label = "Your question",
                leadingIcon = Icons.Outlined.Forum,
                helperText = "For example: how do I request a barangay clearance?",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { ask() }),
            )
            PrimaryButton(
                text = "Ask Assistant",
                loading = busy,
                enabled = question.trim().length >= 3,
                onClick = ask,
            )

            ErrorBanner(error, onDismiss = { error = null })

            reply?.let { answer ->
                AppCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                        Text(answer.answer, style = MaterialTheme.typography.bodyMedium)
                        if (answer.sources.isNotEmpty()) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                            Text(
                                text = "Sources",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            answer.sources.forEach {
                                Text(
                                    text = "• $it",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyReportsScreen(repository: ResidentRepository = remember { ResidentRepository() }) {
    var reports by remember { mutableStateOf<Result<List<ReportDto>>?>(null) }
    LaunchedEffect(Unit) { reports = repository.reports() }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { ScreenTopBar(title = "My Reports", subtitle = "Submission history") },
    ) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Spacing.screen),
            contentPadding = PaddingValues(top = Spacing.xs, bottom = Spacing.xxl),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            when {
                reports == null -> item { CivicMessageRow(Icons.Outlined.Schedule, "Loading your reports...") }
                reports?.isFailure == true -> item { CivicMessageRow(Icons.Outlined.CloudOff, "Your reports could not be loaded right now.") }
                reports?.getOrNull().isNullOrEmpty() -> item { EmptyState("No reports to display", "You have not submitted any community reports yet.", Icons.Filled.Description) }
                else -> items(reports?.getOrNull().orEmpty().size) { index ->
                    AppCard(Modifier.fillMaxWidth()) { ReportRow(reports?.getOrNull().orEmpty()[index], onOpen = {}) }
                }
            }
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
