# Bantay Fatima email verification setup

Bantay Fatima uses email and password for login. Registration also requires a unique Philippine mobile number, but account ownership is verified through a six-digit email code. The same code system protects password resets.

## Gmail SMTP setup

1. Create or select the Gmail/Google Workspace account that Barangay Fatima will use as the sender.
2. Enable 2-Step Verification on that Google account.
3. Open the Google Account security settings and create an App Password for the Laravel application.
4. Put the generated 16-character App Password in `.env`; do not use the normal Google account password.

```dotenv
MAIL_MAILER=smtp
MAIL_SCHEME=smtp
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-gmail-address@gmail.com
MAIL_PASSWORD=your-16-character-google-app-password
MAIL_FROM_ADDRESS=your-gmail-address@gmail.com
MAIL_FROM_NAME="Bantay Fatima"
```

Never commit the real Gmail address/password values. After changing `.env`, run:

```powershell
php artisan optimize:clear
```

## Verification behavior

- Codes contain six cryptographically random digits.
- Only a one-way hash of the code is stored.
- Pending registration data is encrypted by Laravel before database storage.
- Codes expire after 10 minutes.
- A code allows at most five incorrect attempts.
- Sending and resending are rate limited.
- A permanent account is created only after the registration code is verified.
- Password reset codes are tied to the verified account and invalidate existing sessions and API tokens.

## Production recommendations

- Use a dedicated Google Workspace or barangay-owned sender account rather than a developer's personal Gmail account.
- Configure SPF, DKIM, and DMARC when using a custom barangay domain.
- Use queues for mail delivery when the application grows.
- Monitor delivery failures and Gmail sending limits.
- Keep `APP_DEBUG=false`, use HTTPS, and enable secure session cookies in production.

## Testing checklist

- Register with a unique email and Philippine phone number.
- Confirm no permanent user exists before code verification.
- Confirm the email arrives and contains a six-digit code.
- Test correct, incorrect, expired, and over-attempted codes.
- Test resend cooldown/rate limiting.
- Confirm duplicate email and duplicate phone numbers are rejected.
- Log in with verified email/password and test Remember Me.
- Confirm unverified accounts cannot log in.
- Reset a password through the emailed code and confirm the old password fails.
- Confirm existing sessions and Sanctum tokens are invalidated after reset.
- Test the Android API registration, login, authenticated user, and logout endpoints.
