# Google sign-in

One button covers both directions. A resident who taps "Continue with Google" or
"Sign up with Google" ends up signed in either way: if their Gmail address already
has an account the API signs them into it, and if it does not the API creates one
from the verified Google profile. There is no separate "sign up with Google" call,
and no e-mail code step on that route — Google has already proven the address.

## How the pieces divide

| Side | Responsibility |
| --- | --- |
| Android | Ask Credential Manager for a Google **ID token**. Nothing else. |
| Laravel | Verify the token with Google, match or create the user, issue the Sanctum token. |

The device never decides who the resident is. An ID token parsed on the phone can be
forged; only the signature check on the server makes it trustworthy.

## Android setup

1. In Google Cloud Console create an OAuth client:
   - **Web application** client — this is the one whose ID both the app and Laravel use.
   - **Android** client — package `com.bantayfatima.app` plus the SHA-1 of the signing
     key. Debug SHA-1 comes from
     `keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android`.
     Add the release key's SHA-1 before publishing, or sign-in works in debug only.
2. Put the **web** client ID in `local.properties` (untracked):

   ```properties
   bantayfatima.googleWebClientId=1234567890-abcdef.apps.googleusercontent.com
   ```

3. Rebuild. Until this is set, `isGoogleSignInConfigured` is false and every Google
   button and its divider is left out of the layout, so nothing is offered that
   cannot complete.

Testing needs a Google account added to the device or emulator
(Settings → Passwords & accounts → Add account). With no account present, Credential
Manager opens the add-account flow instead of an account chooser.

## API contract

`POST /api/auth/google`

```json
{
  "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6...",
  "device_name": "Bantay Fatima Android"
}
```

Response — the same envelope and `AuthData` shape as `auth/login`, plus one flag:

```json
{
  "success": true,
  "data": {
    "token": "12|sanctum-plain-text-token",
    "token_type": "Bearer",
    "is_new_account": false,
    "user": {
      "id": 41,
      "first_name": "Juan",
      "middle_name": null,
      "last_name": "Dela Cruz",
      "suffix": null,
      "email": "juan@gmail.com",
      "role": "resident",
      "status": "active"
    }
  }
}
```

`is_new_account` is `true` only when this call created the account. It is optional —
it defaults to `false` — and today only affects wording, not routing.

Failures use the existing error envelope; `message` is shown to the resident, so it
should be in plain language.

## What the Laravel endpoint must do

1. **Verify the token with Google.** Use `google/apiclient`:

   ```php
   $client  = new \Google_Client(['client_id' => config('services.google.client_id')]);
   $payload = $client->verifyIdToken($request->input('id_token'));
   abort_if($payload === false, 401, 'Google sign-in could not be verified.');
   ```

   `verifyIdToken` checks the signature, the expiry, and that `aud` matches the same
   **web** client ID the app was built with. Never skip it and never trust the
   `email` field of an unverified token.

2. **Require `email_verified`.** Reject the payload if it is not true.

3. **Match on the e-mail address**, and store the Google subject (`sub`) on first
   use so a later address change still resolves to the same person.

4. **Create the account when there is no match**, from `given_name` / `family_name` /
   `email`, with `role = resident`, no password (or a random unusable one), and the
   e-mail already marked verified. `phone_number` is not available from Google — leave
   it null and collect it later on the profile screen rather than blocking sign-in.

5. **Issue a Sanctum token** exactly as `auth/login` does, and return
   `is_new_account`.

6. **Refuse a suspended or deactivated account** with the same message
   `auth/login` uses, so Google is not a way around a block.

## Security notes

- The web client ID is a public identifier, not a secret. It is kept out of version
  control so each developer points at their own Google Cloud project, not because
  leaking it would be dangerous.
- A resident who registered with a password and later uses Google keeps one account:
  the match is on the e-mail address. Decide deliberately whether the reverse —
  setting a password on a Google-created account — is allowed; today the password
  reset flow is the natural route.
- The device sends no profile data of its own. Name and e-mail come from the token
  Google signed, so a modified client cannot register under someone else's address.
