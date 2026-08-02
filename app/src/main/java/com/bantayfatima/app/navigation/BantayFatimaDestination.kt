package com.bantayfatima.app.navigation

/**
 * Navigation routes for the application.
 *
 * Only [Connection] is reachable today. The remaining entries are placeholders that
 * record the planned structure so screens can be slotted in without reshaping
 * navigation later. Administrator features are deliberately absent: administrators
 * use the Laravel web portal, never this application.
 */
enum class BantayFatimaDestination(val route: String) {

    /** Temporary first screen: API connectivity check. */
    Connection("connection"),

    // --- Planned: shared ---
    Login("login"),

    // --- Planned: resident ---
    Register("register"),
    ResidentHome("resident/home"),
    SubmitReport("resident/reports/new"),
    MyReports("resident/reports"),
    ReportDetail("resident/reports/{reportId}"),
    Announcements("resident/announcements"),
    EmergencyInformation("resident/emergency"),
    Assistant("resident/assistant"),

    // --- Planned: staff ---
    StaffHome("staff/home"),
    AssignedReports("staff/reports"),
    ReportProgress("staff/reports/{reportId}/progress"),

    // --- Planned: shared ---
    Notifications("notifications"),
    Profile("profile"),
}
