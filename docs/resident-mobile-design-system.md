# Bantay Fatima Resident Web and Android Design System

## Foundation tokens

| Token | Value | Use |
|---|---:|---|
| Primary green | `#0F8877` | Primary actions and active navigation |
| Dark navy | `#0B2D4D` | Headings and important text |
| Light background | `#F4F7F9` | Screen background |
| Card | `#FFFFFF` | Cards, sheets, dialogs |
| Border | `#DCE4EA` | Inputs and separators |
| Muted text | `#6B8196` | Supporting labels |
| Pending | `#F5A623` | Pending state plus text label |
| In Progress | `#2F80ED` | Active work plus text label |
| Resolved | `#15966A` | Completed state plus text label |
| Urgent | `#D64545` | Urgent warning plus text label |

Typography uses Instrument Sans/system sans. Recommended sizes: 12, 14, 16, 18, 24, and 30 px; weights 400, 500, 600, and 700. Spacing uses a 4 px base scale: 4, 8, 12, 16, 20, 24, 32, and 40 px. Corners: 12 px controls, 16 px cards, pill badges. Standard buttons and inputs are at least 48 px high. Icons: 20–24 px, or 28 px for emphasized actions. Cards use white, a 1 px border, 16 px radius, and subtle navy shadow. Mobile bottom navigation is 72 px high.

## Components and states

- Primary button: green background, white semibold text, 48 px minimum height.
- Secondary button: white background, navy border/text.
- Destructive/emergency: urgent red, always paired with explicit text.
- Inputs: 48 px minimum height, 12 px radius, visible focus outline, inline error below.
- Status badges always include the status text; color is supplementary.
- Loading: disable the action and replace its label with an explicit progress phrase.
- Empty state: short explanation plus the most useful next action.
- Error state: explain what failed and how to recover; never show server traces.
- Bottom navigation: Home, Report, My Reports, Updates, Profile; Report is emphasized.

## Mobile screen mapping

| Laravel Web | Android screen | Structure |
|---|---|---|
| `/resident/dashboard` | `ResidentHomeScreen` | App bar, four counts, create-report card, recent reports, updates/emergency |
| `/resident/reports/create` | `CreateReportScreen` | Details, photo, draggable map, review/confirmation |
| `/resident/reports` | `MyReportsScreen` | Search/filters, paginated report cards |
| `/resident/reports/{id}` | `ReportDetailsScreen` | Header badges, evidence, map, timeline, public updates |
| `/resident/updates` | `UpdatesScreen` | Type filters and published update cards |
| `/resident/emergency` | `EmergencyInformationScreen` | Warning and grouped call cards |
| `/resident/assistant` | `AssistantScreen` | Approved-source query interface and cited sources/unavailable state |
| `/resident/notifications` | `NotificationsScreen` | Read state, timestamp, related-record action |
| `/resident/profile` | `ResidentProfileScreen` | Profile fields, verification state, password security |

## API mapping

All resident endpoints use `Authorization: Bearer <Sanctum token>`, `Accept: application/json`, and `/api/resident/*`. Dashboard, reports, categories, puroks, updates, emergency information, notifications, profile, password, and assistant endpoints mirror the web screens. Lists return `data` and pagination `meta`; single records return `data`. Android must never send role, report owner, priority, assignment, validation state, or official status as trusted values.

## Validation parity

- Title: 5–150 characters.
- Description: 30–3000 characters.
- Photos: 1–5 images; JPG/JPEG/PNG/WebP; 5 MB each.
- Location: latitude `-90…90`, longitude `-180…180`.
- Phone display uses Philippine E.164; changes require a dedicated verification flow.
- Password: current password plus at least 8 characters containing letters and numbers.
- Errors appear inline and in a screen-level alert; preserve server-provided field messages.

## Accessibility and interaction

Use 48 px touch targets, semantic labels, keyboard focus, sufficient contrast, descriptive image text, and text labels for every state. Do not auto-submit after geolocation. Confirm destructive or irreversible actions. The map marker remains adjustable after location capture.
