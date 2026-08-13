package com.bantayfatima.app.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bantayfatima.app.data.local.AppPreferences
import com.bantayfatima.app.data.model.UserDto
import com.bantayfatima.app.ui.auth.*
import com.bantayfatima.app.ui.components.AuthenticationRequiredSheet
import com.bantayfatima.app.ui.components.BantayFatimaBottomNavigation
import com.bantayfatima.app.ui.components.NavDestination
import com.bantayfatima.app.ui.onboarding.OnboardingScreen
import com.bantayfatima.app.ui.public.*
import com.bantayfatima.app.ui.resident.*
import com.bantayfatima.app.ui.splash.SplashScreen
import com.bantayfatima.app.ui.staff.*

private object Routes {
    const val HOME = "public/home"
    const val UPDATES = "public/updates"
    const val EMERGENCY = "public/emergency"
    const val ABOUT = "public/about"
    const val ASSISTANT = "public/assistant"
    const val ACCOUNT = "public/account"

    const val LOGIN = "auth/login"
    const val REGISTER = "auth/register"
    const val VERIFY = "auth/verify"
    const val FORGOT = "auth/forgot"

    const val RESIDENT_HOME = "resident/home"
    const val REPORT = "resident/report"
    const val MY_REPORTS = "resident/reports"
    const val RESIDENT_PROFILE = "resident/profile"

    const val STAFF_HOME = "staff/home"
    const val ASSIGNED = "staff/assigned"
    const val STAFF_NOTIFICATIONS = "staff/notifications"
    const val STAFF_PROFILE = "staff/profile"
}

/**
 * Application root.
 *
 * Opening order is splash, then first-run onboarding, then public content. A
 * guest is never forced to authenticate: protected features raise a sheet that
 * explains why an account is needed and can be declined.
 */
@Composable
fun BantayFatimaApp(modifier: Modifier = Modifier, authViewModel: AuthViewModel = viewModel()) {
    val state by authViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val preferences = remember { AppPreferences(context) }
    var onboardingDone by rememberSaveable { mutableStateOf(preferences.isOnboardingComplete()) }
    // Launch-only state: never save this flag across activity/process recreation,
    // otherwise Android could restore it as true and skip the next splash.
    var minimumSplashElapsed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Start the branded duration after Compose presents its first frame. This
        // prevents a slow cold start from consuming the timer behind Android's
        // platform splash before the tagline and location can be seen.
        withFrameNanos { }
        delay(1800L)
        minimumSplashElapsed = true
    }

    Surface(modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        val session = state.session

        when {
            session is SessionState.Loading || !minimumSplashElapsed -> SplashScreen()

            !onboardingDone && session is SessionState.Guest -> OnboardingScreen(
                onFinish = {
                    preferences.setOnboardingComplete()
                    onboardingDone = true
                },
            )

            session is SessionState.Guest -> GuestApp(state, authViewModel)
            session is SessionState.Resident -> ResidentApp(session.user, authViewModel)
            session is SessionState.Staff -> StaffApp(session.user, authViewModel)
        }
    }
}

// ---------------------------------------------------------------------------
// Guest
// ---------------------------------------------------------------------------

@Composable
private fun GuestApp(state: AuthUiState, auth: AuthViewModel) {
    val nav = rememberNavController()
    var showAuthSheet by remember { mutableStateOf(false) }
    // Credential Manager draws the account chooser over the hosting activity, so the
    // activity context is what it needs, not the application context.
    val activity = LocalContext.current
    // A successful Google exchange flips the session to Resident, which swaps this
    // whole subtree for ResidentApp; nothing else has to be navigated.
    val signInWithGoogle: () -> Unit = { auth.signInWithGoogle(activity) { showAuthSheet = false } }

    val destinations = listOf(
        NavDestination(Routes.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
        NavDestination(Routes.UPDATES, "Updates", Icons.Filled.Campaign, Icons.Outlined.Campaign),
        NavDestination(Routes.REPORT, "Report a Problem", Icons.Filled.Add, Icons.Outlined.Add, emphasized = true),
        NavDestination(Routes.ASSISTANT, "Assistant", Icons.Filled.SupportAgent, Icons.Outlined.SupportAgent),
        NavDestination(Routes.ACCOUNT, "Account", Icons.Filled.Person, Icons.Outlined.Person),
    )
    val current = nav.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = destinations.any { it.route == current }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                BantayFatimaBottomNavigation(destinations, current) { destination ->
                    when (destination.route) {
                        Routes.REPORT, Routes.ASSISTANT -> showAuthSheet = true
                        else -> nav.navigateTop(destination.route)
                    }
                }
            }
        },
    ) { padding ->
        NavHost(nav, Routes.HOME, Modifier.padding(padding)) {
            composable(Routes.HOME) {
                LandingPageScreen(
                    onProtected = { showAuthSheet = true },
                    onAbout = { nav.navigateTop(Routes.ABOUT) },
                    onUpdates = { nav.navigateTop(Routes.UPDATES) },
                    onEmergency = { nav.navigateTop(Routes.EMERGENCY) },
                    onAccount = { nav.navigateTop(Routes.ACCOUNT) },
                )
            }
            composable(Routes.UPDATES) { PublicUpdatesScreen() }
            composable(Routes.EMERGENCY) { PublicEmergencyScreen() }
            composable(Routes.ABOUT) { PublicAboutScreen() }
            composable(Routes.ACCOUNT) {
                AccountGatewayScreen(
                    onLogin = { nav.navigate(Routes.LOGIN) },
                    onRegister = { nav.navigate(Routes.REGISTER) },
                )
            }
            composable(Routes.LOGIN) {
                LoginScreen(
                    state = state,
                    onBack = nav::popBackStack,
                    onLogin = { email, password, remember -> auth.login(email, password, remember) {} },
                    onGoogle = signInWithGoogle,
                    onRegister = { nav.navigate(Routes.REGISTER) },
                    onForgot = { nav.navigate(Routes.FORGOT) },
                    onDismissError = auth::clearError,
                )
            }
            composable(Routes.REGISTER) {
                RegistrationScreen(
                    state = state,
                    onBack = nav::popBackStack,
                    onSubmit = { request -> auth.requestRegistration(request) { nav.navigate(Routes.VERIFY) } },
                    onGoogle = signInWithGoogle,
                    onDismissError = auth::clearError,
                )
            }
            composable(Routes.VERIFY) {
                VerificationScreen(
                    state = state,
                    onBack = nav::popBackStack,
                    onVerify = { auth.verify(it) {} },
                    onResend = auth::resend,
                    onDismissError = auth::clearError,
                )
            }
            composable(Routes.FORGOT) { ForgotPasswordScreen(nav::popBackStack) }
        }
    }

    if (showAuthSheet) {
        AuthenticationRequiredSheet(
            onDismiss = { showAuthSheet = false },
            onLogin = {
                showAuthSheet = false
                nav.navigate(Routes.LOGIN)
            },
            onRegister = {
                showAuthSheet = false
                nav.navigate(Routes.REGISTER)
            },
            onGoogle = signInWithGoogle,
            googleBusy = state.googleBusy,
            error = state.error,
            onDismissError = auth::clearError,
        )
    }
}

// ---------------------------------------------------------------------------
// Resident
// ---------------------------------------------------------------------------

@Composable
private fun ResidentApp(user: UserDto, auth: AuthViewModel) {
    val nav = rememberNavController()
    // Report is placed centre and emphasised: it is the one action the whole
    // application exists for, and the other four are places you go afterwards.
    val destinations = listOf(
        NavDestination(Routes.RESIDENT_HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
        NavDestination(Routes.MY_REPORTS, "My Reports", Icons.Filled.Description, Icons.Outlined.Description),
        NavDestination(Routes.REPORT, "Report", Icons.Filled.Add, Icons.Outlined.Add, emphasized = true),
        NavDestination(Routes.UPDATES, "Updates", Icons.Filled.Campaign, Icons.Outlined.Campaign),
        NavDestination(Routes.RESIDENT_PROFILE, "Profile", Icons.Filled.Person, Icons.Outlined.Person),
    )
    val current = nav.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BantayFatimaBottomNavigation(destinations, current) { nav.navigateTop(it.route) } },
    ) { padding ->
        NavHost(nav, Routes.RESIDENT_HOME, Modifier.padding(padding)) {
            composable(Routes.RESIDENT_HOME) {
                ResidentDashboardScreen(
                    user = user,
                    onCreateReport = { nav.navigateTop(Routes.REPORT) },
                    onMyReports = { nav.navigateTop(Routes.MY_REPORTS) },
                    onUpdates = { nav.navigateTop(Routes.UPDATES) },
                    onEmergency = { nav.navigate(Routes.EMERGENCY) },
                    onProfile = { nav.navigateTop(Routes.RESIDENT_PROFILE) },
                    onAssistant = { nav.navigate(Routes.ASSISTANT) },
                    onHowItWorks = { nav.navigate(Routes.ABOUT) },
                    onSupport = { nav.navigate(Routes.ABOUT) },
                )
            }
            composable(Routes.REPORT) {
                ReportProblemScreen(
                    onBack = nav::popBackStack,
                    onHome = { nav.navigateTop(Routes.RESIDENT_HOME) },
                    onMyReports = { nav.navigateTop(Routes.MY_REPORTS) },
                )
            }
            composable(Routes.MY_REPORTS) { MyReportsScreen() }
            composable(Routes.UPDATES) { PublicUpdatesScreen() }
            // Reachable from the dashboard's Emergency tile and its advisory strip.
            composable(Routes.EMERGENCY) { PublicEmergencyScreen() }
            composable(Routes.ABOUT) { PublicAboutScreen() }
            composable(Routes.ASSISTANT) { AssistantScreen(nav::popBackStack) }
            composable(Routes.RESIDENT_PROFILE) { ProfileScreen(user, auth::logout) }
        }
    }
}

// ---------------------------------------------------------------------------
// Staff
// ---------------------------------------------------------------------------

@Composable
private fun StaffApp(user: UserDto, auth: AuthViewModel) {
    val nav = rememberNavController()
    val destinations = listOf(
        NavDestination(Routes.STAFF_HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
        NavDestination(Routes.ASSIGNED, "Assigned", Icons.Filled.AssignmentInd, Icons.Outlined.AssignmentInd),
        NavDestination(Routes.UPDATES, "Updates", Icons.Filled.Campaign, Icons.Outlined.Campaign),
        NavDestination(Routes.STAFF_NOTIFICATIONS, "Notifications", Icons.Filled.Notifications, Icons.Outlined.Notifications),
        NavDestination(Routes.STAFF_PROFILE, "Profile", Icons.Filled.Person, Icons.Outlined.Person),
    )
    val current = nav.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BantayFatimaBottomNavigation(destinations, current) { nav.navigateTop(it.route) } },
    ) { padding ->
        NavHost(nav, Routes.STAFF_HOME, Modifier.padding(padding)) {
            composable(Routes.STAFF_HOME) { StaffDashboardScreen(user) }
            composable(Routes.ASSIGNED) { AssignedReportsScreen() }
            composable(Routes.UPDATES) { PublicUpdatesScreen() }
            composable(Routes.STAFF_NOTIFICATIONS) { StaffNotificationsScreen() }
            composable(Routes.STAFF_PROFILE) { ProfileScreen(user, auth::logout) }
        }
    }
}

private fun NavHostController.navigateTop(route: String) = navigate(route) {
    popUpTo(graph.findStartDestination().id) { saveState = true }
    launchSingleTop = true
    restoreState = true
}
