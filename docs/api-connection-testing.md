# Bantay Fatima — Android to Laravel API connection

How the Android application reaches the Laravel backend, and how to test it on an
emulator and on a physical phone.

## Architecture

```
Android application  ──HTTP(S)──>  Laravel REST API  ──>  MySQL (bantay_fatima)
```

The Android application **never** connects to MySQL. It has no database driver, no
database credentials, and never opens port 3306. Every piece of data it shows comes
from a Laravel endpoint under `/api/`.

Administrators are not part of the mobile application. They keep using the Laravel web
portal at `/admin/login`. The mobile application is for residents and authorised staff
only, and it contains no administrator URL, route or login.

## Endpoint added in this stage

`GET /api/health` — public, unauthenticated, returns:

```json
{
    "success": true,
    "message": "Bantay Fatima API is connected.",
    "data": {
        "application": "Bantay Fatima",
        "environment": "local",
        "server_time": "2026-08-01T20:14:44+00:00"
    }
}
```

`server_time` is rendered in the timezone configured in `config/app.php` (currently
`UTC`). The endpoint returns no configuration values, no paths, no versions and no
`.env` data.

## 1. Start the Laravel server

### For the Android emulator

```powershell
cd C:\Users\ADMIN\Herd\bantay-fatima
php artisan serve --host=127.0.0.1 --port=8000
```

Verify in the computer's browser: <http://127.0.0.1:8000/api/health>

The emulator reaches the host machine through the special alias **`10.0.2.2`**, so the
app's base URL is:

```
http://10.0.2.2:8000/api/
```

`localhost` and `127.0.0.1` mean *the emulator itself*, not your computer — they will
never work as the Android base URL.

### For a physical Android phone

The server has to listen on all interfaces, not just loopback:

```powershell
cd C:\Users\ADMIN\Herd\bantay-fatima
php artisan serve --host=0.0.0.0 --port=8000
```

Then find your computer's LAN IPv4 address:

```powershell
ipconfig
```

Look at the adapter you are actually connected with (usually **Wireless LAN adapter
Wi-Fi**) and take the **IPv4 Address** line, e.g. `192.168.1.5` or `192.168.254.107`.
It normally starts with `192.168.`, `10.` or `172.16–31.`. Ignore `127.0.0.1` and any
address on a virtual adapter (VirtualBox, WSL, Hyper-V, VMware).

Your base URL is that address:

```
http://<YOUR-IPV4>:8000/api/
```

This address changes when you reconnect or move networks — do not treat any example
here as final. Confirm it with `ipconfig` each session.

**Requirements for a physical phone:**

1. Phone and computer are on the **same Wi-Fi network** (not mobile data, and not a
   guest network that isolates clients).
2. Windows Firewall allows inbound TCP on port 8000. If the phone times out but the
   computer's own browser works, this is almost always the cause. As an Administrator:

   ```powershell
   New-NetFirewallRule -DisplayName "Laravel dev server 8000" -Direction Inbound -Protocol TCP -LocalPort 8000 -Action Allow -Profile Private
   ```

   Use `-Profile Private` only, and remove the rule when you are done:

   ```powershell
   Remove-NetFirewallRule -DisplayName "Laravel dev server 8000"
   ```

3. Sanity-check from the phone's browser first: open `http://<YOUR-IPV4>:8000/api/health`.
   If the browser cannot load it, the app will not either.

## 2. Point the app at the right address

The debug base URL is a `BuildConfig` field set by Gradle. The default is the emulator
alias. To target a physical phone, add one line to **`local.properties`** (untracked by
git, so nothing leaks into version control):

```properties
bantayfatima.debugApiBaseUrl=http://192.168.1.5:8000/api/
```

Replace the address with your own from `ipconfig`, then **Sync Project with Gradle
Files** and rebuild. Remove or comment the line to go back to the emulator.

Never put database credentials, Laravel `.env` values, Sanctum secrets or Firebase
service-account keys in `local.properties` or in `BuildConfig`.

| Build type | Base URL | Cleartext HTTP |
| --- | --- | --- |
| `debug` | `http://10.0.2.2:8000/api/` (overridable in `local.properties`) | Allowed |
| `release` | `https://your-production-domain.example/api/` | **Denied** |

Both URLs end in a trailing slash, which Retrofit requires for relative paths such as
`health` to resolve correctly.

## 3. Run the app

1. Open the project in Android Studio and let Gradle sync.
2. Select the **debug** build variant (the default).
3. Run on an emulator (API 26+) or a connected phone.
4. The app opens the connection-test screen and shows
   *"Press the button to test the Laravel API connection."*
5. Tap **Test Connection**.

Expected results:

| Situation | What the app shows |
| --- | --- |
| Laravel running, correct URL | Green check + *"Bantay Fatima API is connected."* plus application, environment and server time |
| Laravel stopped | Warning icon + *"Unable to reach the Bantay Fatima server. Make sure the Laravel server is running."* + **Retry** |
| Wi-Fi off / airplane mode | Warning icon + *"Please check your network connection and try again."* |
| Wrong path, 404/500, or HTML instead of JSON | Warning icon + *"The server returned an unexpected response."* |

In debug builds the base URL in use is printed under the button, which makes a wrong
address obvious immediately. Release builds do not show it.

## 4. Test checklist

| # | Case | Expected |
| --- | --- | --- |
| 1 | Laravel running | Success state with the API message |
| 2 | Stop Laravel (Ctrl+C), tap Test Connection | Server-unavailable message, no crash |
| 3 | Start Laravel again, tap Retry | Success state |
| 4 | Set a bad `bantayfatima.debugApiBaseUrl`, rebuild | Error state, no crash |
| 5 | Rotate the screen mid-request and after a result | State survives; result still arrives |
| 6 | Emulator | Uses `10.0.2.2` |
| 7 | Physical phone on same Wi-Fi, firewall open | Uses the computer's LAN IPv4 |
| 8 | `:app:assembleRelease` | Builds; no cleartext permitted; HTTPS placeholder URL |

For case 5, note the request keeps running across the rotation because it is owned by
`ConnectionViewModel`, not by the composable.

## 5. Troubleshooting

**"Unable to reach the Bantay Fatima server" on the emulator** — Laravel is not running,
or it was started on a different port. `php artisan serve` defaults to 8000; if it
reported a different port, update the base URL to match.

**Works in the computer's browser, fails on a physical phone** — either the phone is on
a different network, or Windows Firewall is blocking port 8000, or Laravel was started
with `--host=127.0.0.1` instead of `--host=0.0.0.0`.

**"The server returned an unexpected response"** — Laravel answered but not with the
expected JSON. Check `php artisan route:list --path=api` shows `api/health`, and look at
`storage/logs/laravel.log`.

**Changed `local.properties` but nothing happened** — `BuildConfig` is generated at build
time. Sync Gradle and rebuild.

**Release build cannot reach a plain-HTTP server** — that is intended. Release builds
require HTTPS. Test against `http://` in debug only.

## 6. Not implemented yet

Login, registration, phone/email verification, report submission, photo upload,
geo-tagged locations, maps, announcements, notifications, the RAG assistant and profile
screens are deliberately out of scope for this stage. The package structure
(`data/model`, `data/remote`, `data/repository`, `ui`, `navigation`) and the route
placeholders in `navigation/BantayFatimaDestination.kt` are in place so those screens
can be added without reshaping the project.

Camera, location and storage permissions are **not** declared yet. They will be added
alongside the features that need them.
