# Bantay Fatima Pre-Proposal Compliance Audit

> Historical baseline: this audit records the gaps found before the August 14 implementation pass. Those gaps drove the completed work now present in the working trees: staff field operations, resident report history, OpenStreetMap views, admin advisory/hotline/knowledge management, approved-source OpenRouter RAG, in-app notifications, print view, Google API authentication, and website APK delivery. See `deployment-configuration.md` for the remaining environment credentials.

Audit date: August 14, 2026

Source of truth: `Castillo_Title.pdf` (4 pages), supplied by the project proponent.

Repositories reviewed:

- Laravel web platform and REST API: `C:\Users\ADMIN\Herd\bantay-fatima`
- Android application: `C:\Users\ADMIN\AndroidStudioProjects\BantayFatima`

## Executive conclusion

The project is **partially compliant and directionally aligned**, but it does not yet implement the complete approved scope. The architecture, technology choices, public informational site, administrator report workflow, Android authentication, and resident geo-tagged reporting foundation match the proposal. The largest promised capabilities that remain incomplete are the staff field-response workflow, real RAG/vector retrieval, administrator management of announcements/hotlines/knowledge sources, push notifications, report-location maps in the administrator portal, report-detail tracking on Android, and Android download access on the website.

This is therefore suitable to describe as an **in-progress implementation of the approved proposal**, not yet as a feature-complete capstone system.

## Compliance matrix

| Proposal requirement | Assessment | Implementation evidence and gap |
|---|---|---|
| Responsive informational website | Mostly implemented | The public landing page describes the purpose, features, roles, reporting workflow, emergency disclaimer, and support. It correctly does not provide resident web reporting. The APK download remains `Android App Coming Soon`, and several support links are placeholders. |
| Secure web administrator dashboard | Implemented foundation | Admin-only middleware, login, dashboard statistics, report listing/filtering, report validation/rejection, priority, assignment, status updates, notes, photo evidence, user management, and audit logging exist. |
| Admin announcement/advisory publishing | Missing | `Advisory` models and read APIs exist, but there are no administrator routes, controllers, or screens to create, edit, publish, or archive advisories. |
| Admin emergency-hotline management | Missing | `EmergencyHotline` and read APIs exist, but no administrator CRUD workflow exists. |
| Admin knowledge-base management | Missing | No administrator document upload/indexing workflow, source management, embeddings, or vector index is implemented. |
| Admin summary dashboards | Implemented foundation | Counts, recent reports, average resolution time, residents, and staff are calculated. Some values such as notifications remain hard-coded. |
| Printable reports | Partially implemented | Filtered CSV export exists. A formatted print view or PDF report is not implemented. |
| Admin report-location map | Missing | Coordinates are displayed as text on the report page, but no map is rendered in the administrator portal. |
| Android resident and staff role modes | Partially implemented | Authentication routes users into separate resident and staff navigation shells. The resident mode is functional; the staff mode is mostly static UI. |
| Resident categorized report submission | Implemented foundation | Category, title, description, purok, landmark, declaration, draft, review, and multipart submission exist. |
| Resident report photos | Implemented foundation | Camera/gallery selection, preview/removal, compression, five-photo limit, and Laravel storage exist. The proposal says “photo”; permitting multiple photos is a compatible enhancement. |
| Resident geo-tagged location | Implemented | GPS, reverse geocoding, OpenStreetMap tiles, map tapping, draggable marker, coordinates, accuracy, and Laravel latitude/longitude persistence exist. |
| Resident status tracking | Partially implemented | Dashboard counts and My Reports list statuses from the API. Individual report cards have an empty click action, so detailed status history and resident-visible updates cannot currently be opened in Android. The proposal's explicit “submitted/verified/in-progress/resolved” tracking is therefore incomplete. |
| Announcements/advisories on Android | Read path implemented | Public and resident APIs/screens can read published content, but administrators cannot publish it through the promised web workflow. |
| Emergency hotlines on Android | Read path implemented | The screen and API exist, with the correct disclaimer that the app does not replace emergency services. Content administration is missing. |
| Staff receives assigned reports | Backend only / UI placeholder | Laravel exposes staff dashboard, assigned-list, and detail GET endpoints. Android does not call these endpoints; dashboard counts are literal zero and assignment screens always show empty states. |
| Staff views reported location on a map | Missing | No functional assigned-report detail screen is connected in Android. |
| Staff updates response status | Missing | No staff update endpoint, repository method, or functional Android action exists. |
| Staff uploads proof-of-action photo | Missing | No staff proof upload endpoint or Android capture/upload flow exists. The data model has a resolution photo field but no complete workflow. |
| RAG assistant in Android | UI/API shell only | Android can submit questions and display sources. Laravel binds `ResidentKnowledgeService` to `UnavailableResidentKnowledgeService`, which always returns no approved answer. There is no document ingestion, retrieval, embedding, vector index, or grounded generation service. |
| Role-based authentication | Mostly implemented | Laravel Sanctum, role middleware, resident/staff/admin roles, encrypted Android token storage, email verification, login, and registration are present. Android declares `auth/google`, but Laravel currently has no matching `/api/auth/google` route, so Google authentication is inconsistent. |
| Token-based Android API access | Implemented | Sanctum tokens are attached as Bearer tokens by the Android HTTP client. |
| One shared database accessed through Laravel API | Aligned | Android contains no database credentials and communicates through Retrofit/Laravel. Laravel uses its configured relational database. Deployment must ensure MySQL/MariaDB rather than SQLite if strict adherence is required. |
| Push and in-app notifications | Partially implemented | Laravel database notifications and resident notification endpoints exist. Android notification screens are not connected, and no FCM/push provider or device-token workflow exists. |
| Android 8 / API 26 minimum | Compliant | `minSdk = 26`. |
| Android 15 / API 35 target | Minor variance | The app targets API 36 rather than API 35. This is newer, but it differs from the proposal's exact specification and should be documented or changed. |
| Android-only mobile platform | Compliant | Only an Android project is present. |
| Website restricted to information/admin, with resident transactions on Android | Compliant | Public web routes are informational/legal; resident and staff web transaction routes are intentionally unavailable. |
| Barangay Fatima-only scope and emergency limitations | Compliant in wording and data model | Branding and content are Barangay Fatima-specific, and emergency disclaimers are present. Geographic enforcement is not implemented, so coordinates outside Barangay Fatima can still be submitted. |
| Pilot testing with residents and personnel | Not verifiable in source code | The proposal makes this subject to client approval. No pilot test protocol/results or acceptance artifact was found in either repository. |
| Agile methodology | Not verifiable from product code alone | Git history and incremental implementation are compatible with Agile, but no backlog, sprint records, retrospectives, or client acceptance evidence were found in the reviewed repositories. |

## High-priority gaps before claiming full compliance

1. Implement the complete staff field workflow end to end: live assigned reports, detail screen, map, allowed status transitions, notes, and proof-of-action photo upload.
2. Implement a real approved-source RAG pipeline: admin document/advisory ingestion, chunking, embeddings/vector index, retrieval, grounded answer generation, citations, and safe unavailable behavior.
3. Add administrator CRUD/publishing screens for advisories/announcements and emergency hotlines.
4. Add a map to the administrator report detail screen using stored coordinates.
5. Complete Android report details and status history, including resident-visible notes and verified/rejected states.
6. Connect Android in-app notifications and implement push delivery if retained in the final scope.
7. Provide actual Android installation/download instructions and an APK/download link on the informational site.
8. Replace CSV-only export with a print-optimized report page or generated PDF if “printable reports” is interpreted literally by the panel/client.
9. Resolve the Android/Laravel Google authentication endpoint mismatch or remove the nonfunctional option.
10. Enforce or explicitly document Barangay Fatima geographic boundaries for submitted coordinates.

## Quality and verification findings

- Laravel test suite: **31 passed, 1 failed, 120 assertions**. The failure is `ResidentPortalTest::test_public_landing_page_has_no_visible_authentication_links`; its expected landing-page wording no longer matches the current page. This appears to be a stale test, but it must be reconciled rather than ignored.
- Android unit tests: **build successful**.
- Android lint: **not completed** because AGP's `lintDebug` output task expected a missing intermediate text report. This is a build-tool/task configuration issue and leaves static-analysis compliance unverified.
- Web production asset build: **successful**. Vite emitted only an optional font-fallback optimization warning.
- Android debug installation and launch had already succeeded during the map/report implementation work.
- The Android release API URL remains a placeholder (`https://your-production-domain.example/api/`), so the current release configuration is not deployment-ready.
- Both repositories contain uncommitted implementation changes; conclusions include the working trees, not only the latest commits.

## Overall assessment

The current system strongly follows the proposal's **concept, platform split, technology stack, security model, administrator report workflow, and resident geo-tagged reporting core**. It does not yet follow the proposal completely at the functional level. The proposal's distinguishing features—field responder operations and retrieval-augmented generation—are currently the two most substantial incomplete areas.

Recommended presentation wording: “The project currently implements the core resident reporting and administrator management foundation described in the approved pre-proposal. Staff field operations, RAG indexing, content administration, push notifications, and deployment/download features remain under active development.”
