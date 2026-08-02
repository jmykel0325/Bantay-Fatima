package com.bantayfatima.app.ui.public

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bantayfatima.app.data.model.EmergencyInfo
import com.bantayfatima.app.data.model.PublicUpdate
import com.bantayfatima.app.data.repository.PublicContentRepository
import com.bantayfatima.app.ui.components.*
import com.bantayfatima.app.ui.theme.AppTheme
import com.bantayfatima.app.ui.theme.Spacing

// ---------------------------------------------------------------------------
// Public home
// ---------------------------------------------------------------------------

private data class QuickAction(
    val label: String,
    val icon: ImageVector,
    val locked: Boolean = false,
)

private data class PublicService(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val locked: Boolean = false,
)

private val publicServices = listOf(
    PublicService("Geo-Tagged Reporting", "Send an accurate location with a community concern.", Icons.Filled.LocationOn, true),
    PublicService("Report Status Tracking", "Follow action taken on your submitted reports.", Icons.AutoMirrored.Filled.Assignment, true),
    PublicService("Barangay Announcements", "Read published announcements and advisories.", Icons.Filled.Campaign),
    PublicService("Emergency Information", "Find official emergency contact details.", Icons.Filled.LocalHospital),
    PublicService("Bantay Fatima Assistant", "Get guidance about using barangay services.", Icons.Filled.SmartToy, true),
)

/**
 * Landing screen for guests.
 *
 * Public information comes first; anything needing an account is marked with a
 * lock and routed through [onProtected] rather than a silent redirect.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicHomeScreen(
    onProtected: () -> Unit,
    onAbout: () -> Unit,
    onUpdates: () -> Unit = {},
    onEmergency: () -> Unit = {},
    onAccount: () -> Unit = {},
    repository: PublicContentRepository = remember { PublicContentRepository() },
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var updates by remember { mutableStateOf<Result<List<PublicUpdate>>?>(null) }
    LaunchedEffect(Unit) { updates = repository.updates() }

    val quickActions = listOf(
        QuickAction("Report a Concern", Icons.Filled.AddCircle, locked = true),
        QuickAction("View Updates", Icons.Filled.Campaign),
        QuickAction("Emergency Hotlines", Icons.Filled.LocalHospital),
        QuickAction("My Reports", Icons.Filled.Description, locked = true),
        QuickAction("How It Works", Icons.Filled.Info),
        QuickAction("Ask Bantay Fatima", Icons.Filled.SmartToy, locked = true),
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BantayFatimaTopBar(
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = onAccount, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Account")
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .widthIn(max = MaxContentWidth),
            contentPadding = PaddingValues(
                start = Spacing.screen,
                end = Spacing.screen,
                top = Spacing.xs,
                bottom = Spacing.xxl,
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing.section),
        ) {
            item {
                HeroCard(
                    title = "A Safer and More Connected Barangay Fatima",
                    description = "Report community concerns, receive official barangay updates, and follow the progress of submitted reports through Bantay Fatima.",
                    primaryLabel = "Report a Concern",
                    secondaryLabel = "Learn How It Works",
                    onPrimary = onProtected,
                    onSecondary = onAbout,
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    PublicSectionHeader("Quick Actions")
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                        contentPadding = PaddingValues(end = Spacing.xxs),
                    ) {
                        items(quickActions) { action ->
                            QuickActionCard(
                                label = action.label,
                                icon = action.icon,
                                locked = action.locked,
                                onClick = {
                                    when {
                                        action.locked -> onProtected()
                                        action.label == "View Updates" -> onUpdates()
                                        action.label == "Emergency Hotlines" -> onEmergency()
                                        else -> onAbout()
                                    }
                                },
                            )
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    PublicSectionHeader("Available Services")
                    publicServices.forEach { service ->
                        ServiceFeatureCard(
                            title = service.title,
                            description = service.description,
                            icon = service.icon,
                            locked = service.locked,
                            onClick = when {
                                service.locked -> onProtected
                                service.title.contains("Announcements") -> onUpdates
                                service.title.contains("Emergency") -> onEmergency
                                else -> onAbout
                            },
                        )
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    PublicSectionHeader(
                        title = "Latest Barangay Updates",
                        actionLabel = "View All",
                        onAction = onUpdates,
                    )
                    when {
                        updates == null -> LoadingState("Loading barangay updates")
                        updates?.isFailure == true -> ErrorState(
                            title = "Unable to load updates",
                            description = "Please check your connection and try again.",
                            onRetry = { updates = null },
                        )
                        updates?.getOrNull().isNullOrEmpty() -> EmptyState(
                            title = "No published updates",
                            description = "New barangay announcements will appear here.",
                            icon = Icons.AutoMirrored.Filled.Article,
                        )
                        // Preview only the three most recent; the rest live on the Updates tab.
                        else -> updates!!.getOrThrow().take(3).forEach { UpdateCard(it) }
                    }
                }
            }

            item {
                EmergencyCard(
                    icon = Icons.Filled.LocalHospital,
                    onAction = onEmergency,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Updates
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicUpdatesScreen(repository: PublicContentRepository = remember { PublicContentRepository() }) {
    var state by remember { mutableStateOf<Result<List<PublicUpdate>>?>(null) }
    var reloadKey by remember { mutableIntStateOf(0) }
    LaunchedEffect(reloadKey) {
        state = null
        state = repository.updates()
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ScreenTopBar(
                title = "Barangay Updates",
                subtitle = "Announcements and advisories",
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .widthIn(max = MaxContentWidth),
            contentPadding = PaddingValues(
                start = Spacing.screen,
                end = Spacing.screen,
                top = Spacing.xs,
                bottom = Spacing.xxl,
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            item {
                Text(
                    text = "Published announcements, advisories, and urgent community notices from the barangay office.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = Spacing.xs),
                )
            }
            when {
                state == null -> item { LoadingState("Loading barangay updates") }
                state?.isFailure == true -> item {
                    ErrorState(
                        title = "Unable to load updates",
                        description = "Please check your connection and try again.",
                        onRetry = { reloadKey++ },
                    )
                }
                state?.getOrNull().isNullOrEmpty() -> item {
                    EmptyState(
                        title = "No published updates",
                        description = "New barangay announcements will appear here.",
                        icon = Icons.AutoMirrored.Filled.Article,
                    )
                }
                else -> items(state!!.getOrThrow()) { UpdateCard(it) }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Emergency information
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicEmergencyScreen(repository: PublicContentRepository = remember { PublicContentRepository() }) {
    var state by remember { mutableStateOf<Result<List<EmergencyInfo>>?>(null) }
    var reloadKey by remember { mutableIntStateOf(0) }
    LaunchedEffect(reloadKey) {
        state = null
        state = repository.emergency()
    }
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ScreenTopBar(
                title = "Emergency Information",
                subtitle = "Official contact directory",
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .widthIn(max = MaxContentWidth),
            contentPadding = PaddingValues(
                start = Spacing.screen,
                end = Spacing.screen,
                top = Spacing.xs,
                bottom = Spacing.xxl,
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            item {
                InfoCard(
                    title = "Important",
                    text = "Bantay Fatima is a community reporting platform. It does not replace police, fire, ambulance, hospital, or other official emergency-response services.",
                    icon = Icons.Filled.Warning,
                    accent = AppTheme.status.urgent,
                    container = AppTheme.status.urgentContainer,
                    onContainer = AppTheme.status.urgentText,
                )
            }
            when {
                state == null -> item { LoadingState("Loading emergency contacts") }
                state?.isFailure == true -> item {
                    ErrorState(
                        title = "Unable to load contacts",
                        description = "Please check your connection and try again.",
                        onRetry = { reloadKey++ },
                    )
                }
                state?.getOrNull().isNullOrEmpty() -> item {
                    EmptyState(
                        title = "No emergency contacts published",
                        description = "Contact the barangay office for assistance.",
                        icon = Icons.Filled.Phone,
                    )
                }
                else -> items(state!!.getOrThrow()) { info ->
                    EmergencyContactCard(info) { number ->
                        context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
                    }
                }
            }
        }
    }
}

@Composable
private fun EmergencyContactCard(info: EmergencyInfo, onDial: (String) -> Unit) {
    val number = info.hotlineNumber ?: info.number
    AppCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconBadge(Icons.Filled.LocalHospital, size = 42.dp)
                Column(Modifier.weight(1f)) {
                    Text(
                        text = info.organization ?: info.name ?: "Emergency Service",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    info.category?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            info.address?.let { DetailLine(Icons.Filled.Place, it) }
            info.availability?.let { DetailLine(Icons.Filled.Schedule, it) }
            Spacer(Modifier.height(Spacing.xxs))
            PrimaryButton(
                text = number ?: "Number unavailable",
                icon = Icons.Filled.Call,
                enabled = !number.isNullOrBlank(),
                onClick = { number?.let(onDial) },
            )
        }
    }
}

@Composable
private fun DetailLine(icon: ImageVector, text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs), verticalAlignment = Alignment.Top) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(17.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ---------------------------------------------------------------------------
// About
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicAboutScreen() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ScreenTopBar(
                title = "About",
                subtitle = "How Bantay Fatima works",
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .widthIn(max = MaxContentWidth),
            contentPadding = PaddingValues(
                start = Spacing.screen,
                end = Spacing.screen,
                top = Spacing.xs,
                bottom = Spacing.xxl,
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            item {
                AppCard(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(Spacing.lg),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                    ) {
                        AuthorityLockup(sealSize = 52.dp)
                        Text(
                            text = "Bantay Fatima is the official community reporting and public-information platform for Barangay Fatima residents and authorised personnel.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            item { PublicSectionHeader("Who Uses It") }
            item { ServiceFeatureCard("Residents", "Submit reports, follow their status, and read barangay information.", Icons.Filled.Person) }
            item { ServiceFeatureCard("Barangay Staff", "Review assigned concerns and provide accountable progress updates.", Icons.Filled.Badge) }
            item { ServiceFeatureCard("Administrators", "Manage users, reports, and official content through the secure web portal.", Icons.Filled.AdminPanelSettings) }
            item { PublicSectionHeader("Your Responsibility") }
            item {
                InfoCard(
                    title = "Responsible reporting",
                    text = "Provide truthful details, appropriate evidence, and accurate locations. Emergency situations must be directed to official responders.",
                    icon = Icons.AutoMirrored.Filled.FactCheck,
                    accent = MaterialTheme.colorScheme.primary,
                    container = MaterialTheme.colorScheme.primaryContainer,
                    onContainer = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            item {
                Text(
                    text = "The application continues to be improved in coordination with Barangay Fatima. Contact the barangay office for support.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Account gateway
// ---------------------------------------------------------------------------

@Composable
fun AccountGatewayScreen(onLogin: () -> Unit, onRegister: () -> Unit) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.screen)
                .padding(top = Spacing.xxl, bottom = Spacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BantayFatimaLogo(size = 88.dp)
            Spacer(Modifier.height(Spacing.lg))
            Text(
                text = "Access More Bantay Fatima Services",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { heading() },
            )
            Spacer(Modifier.height(Spacing.xs))
            Text(
                text = "Sign in to submit reports, track updates, and manage your account.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(Spacing.xl))
            PrimaryButton("Log In", icon = Icons.AutoMirrored.Filled.Login, onClick = onLogin)
            Spacer(Modifier.height(Spacing.sm))
            SecondaryButton("Create Resident Account", icon = Icons.Filled.PersonAdd, onClick = onRegister)

            Spacer(Modifier.height(Spacing.lg))
            InfoCard(
                title = "Staff accounts",
                text = "Staff accounts are created by an administrator. Staff members use the same login screen as residents.",
                icon = Icons.Filled.Badge,
            )

            Spacer(Modifier.height(Spacing.xl))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Spacer(Modifier.height(Spacing.xs))
            GatewayLink("Terms and Conditions", Icons.Filled.Description)
            GatewayLink("Privacy Policy", Icons.Filled.PrivacyTip)
            GatewayLink("Support", Icons.Filled.SupportAgent)
            GatewayLink("About the App", Icons.Filled.Info)
        }
    }
}

@Composable
private fun GatewayLink(label: String, icon: ImageVector) {
    TextButton(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(19.dp))
        Spacer(Modifier.width(Spacing.sm))
        Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
    }
}
