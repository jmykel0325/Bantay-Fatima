# Bantay Fatima Admin Panel — Stage One

## Included

- Responsive, mobile-drawer admin shell with active navigation and protected logout.
- Database-driven dashboard statistics, recent reports, monthly report activity, and category distribution.
- Community report search, filters, pagination, detail view, CSV export, assignments, priorities, statuses, resolution fields, notes, and histories.
- User search and filters, secure staff-account creation, account status management, last-active-admin protection, and self-status protection.
- Role-specific login destinations for administrators, staff, and residents.
- Audit records for report changes, report notes, staff creation, and user status changes.

## Commands

```powershell
composer install
npm install
php artisan migrate
npm run build
php artisan test
```

No storage link is required in stage one because report photo upload is not yet enabled. No optional test seeder is required; the migration installs the approved purok list and initial report categories as system reference data.

## Manual verification

1. Sign in with an active administrator and confirm redirection to `/admin/dashboard`.
2. Resize the browser and confirm the sidebar becomes a mobile drawer.
3. Visit Community Reports; test search, status/priority/category/purok/date filters, pagination, and CSV export.
4. Open a report; assign staff, change priority/status, add an internal note, and confirm its history and audit record.
5. Resolve a report and confirm resolution date and notes are required.
6. Visit User Management; search/filter accounts and create a staff account with a unique email and Philippine phone number.
7. Confirm the new staff number is stored in `+63` E.164 form and `must_change_password` is true.
8. Confirm administrators cannot suspend themselves and the final active administrator cannot be disabled.
9. Sign in as staff and resident accounts and confirm both receive 403 responses for `/admin/*` routes.

## Environment work remaining

- None for stage one beyond the existing MySQL and Gmail settings.
- Future photo/document uploads will require `php artisan storage:link`.
- Map tiles, backup tooling, PDF export, document processing, and Android push notification configuration belong to later stages.
