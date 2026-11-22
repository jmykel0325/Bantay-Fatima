# Bantay Fatima deployment configuration

## OpenRouter RAG assistant

The resident assistant retrieves relevant chunks only from administrator-approved knowledge sources, then sends that context to OpenRouter. Configure these values in the Laravel `.env` file:

```dotenv
OPENROUTER_API_KEY=your_openrouter_api_key
OPENROUTER_MODEL=openrouter/free
OPENROUTER_APP_NAME="Bantay Fatima"
```

After changing environment values, run:

```powershell
php artisan optimize:clear
```

Create and approve sources under **Admin > Knowledge Base**. The assistant refuses to invent an answer when no approved source matches or OpenRouter is unavailable. Citations returned to Android identify the approved local source used to ground the answer.

`openrouter/free` is the OpenRouter free-model router. Availability and rate limits are controlled by OpenRouter, so production deployments should monitor failures and can replace `OPENROUTER_MODEL` with a specific supported model without changing application code.

## Google authentication

Set `GOOGLE_WEB_CLIENT_ID` to the same web client ID used by the Android Google sign-in configuration. Laravel verifies the Google ID-token audience and verified-email flag before issuing a Sanctum token.

## Android API addresses

The debug app uses `http://10.0.2.2:8000/api/`, which reaches the host machine from the Android emulator. Start Laravel on all interfaces:

```powershell
php artisan serve --host=0.0.0.0 --port=8000
```

Set `BANTAY_FATIMA_RELEASE_API_URL` when producing a release build. It must be an HTTPS URL ending in `/api/`.

## Android download

The tested debug APK is copied to `public/downloads/bantay-fatima.apk`. The public landing-page button downloads this file. For a real deployment, replace it with a signed release APK built using the production API URL.

## Maps and notifications

Maps use OpenStreetMap and do not require a Google Maps key. Database-backed in-app notifications are enabled for residents and staff. Device-token registration storage is ready for a push provider, but external push delivery requires Firebase project credentials and an Android `google-services.json`; these secrets are intentionally not committed.
