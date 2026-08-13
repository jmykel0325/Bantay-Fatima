package com.bantayfatima.app.data.remote

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.bantayfatima.app.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

/**
 * Raised when the resident dismisses the Google account chooser.
 *
 * Cancelling is a deliberate choice, not a failure, so the ViewModel swallows this
 * instead of showing an error banner.
 */
object GoogleSignInCancelled : Exception("Google sign-in was cancelled.")

/**
 * Whether this build carries an OAuth web client ID.
 *
 * Read by the screens so they can leave the Google buttons out entirely rather than
 * offering a button that cannot work.
 */
val isGoogleSignInConfigured: Boolean get() = BuildConfig.GOOGLE_WEB_CLIENT_ID.isNotBlank()

/**
 * Obtains a Google ID token through Credential Manager.
 *
 * This class deliberately stops at the token. It never decides who the resident is:
 * the ID token is passed to the Laravel API, which verifies the signature and the
 * audience against the same OAuth client and then issues the Sanctum token. Trusting
 * a token parsed on the device would let anyone sign in as anyone.
 *
 * The account chooser is requested twice when needed. The first attempt is limited
 * to accounts that have already been used with this app, which is what makes a
 * returning resident land straight back in their account with a single tap. If no
 * such account exists the second attempt offers every Google account on the device,
 * which is the sign-up path.
 */
class GoogleIdentityClient(context: Context) {

    private val activityContext = context
    private val credentialManager = CredentialManager.create(context)

    suspend fun requestIdToken(): Result<String> {
        if (!isGoogleSignInConfigured) {
            return Result.failure(
                Exception("Google sign-in is not configured for this build. Add bantayfatima.googleWebClientId to local.properties.")
            )
        }
        return attempt(filterByAuthorizedAccounts = true).recoverCatching { error ->
            // No previously used account on this device: widen to every account.
            if (error is NoCredentialException) attempt(filterByAuthorizedAccounts = false).getOrThrow()
            else throw error
        }
    }

    private suspend fun attempt(filterByAuthorizedAccounts: Boolean): Result<String> {
        val option = GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
            // Skips the chooser when exactly one account is already authorised, so a
            // returning resident is signed straight back in.
            .setAutoSelectEnabled(filterByAuthorizedAccounts)
            .build()

        return try {
            val response = credentialManager.getCredential(
                context = activityContext,
                request = GetCredentialRequest.Builder().addCredentialOption(option).build(),
            )
            val credential = response.credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                Result.success(GoogleIdTokenCredential.createFrom(credential.data).idToken)
            } else {
                Result.failure(Exception("Google returned an unexpected credential type."))
            }
        } catch (cancelled: GetCredentialCancellationException) {
            Result.failure(GoogleSignInCancelled)
        } catch (missing: NoCredentialException) {
            // Propagated so requestIdToken can retry without the authorised filter.
            Result.failure(missing)
        } catch (malformed: GoogleIdTokenParsingException) {
            Result.failure(Exception("Google sign-in failed. Please try again."))
        } catch (failure: GetCredentialException) {
            Result.failure(
                Exception(
                    "Google sign-in is unavailable on this device. Check that a Google account is added and Google Play services is up to date."
                )
            )
        }
    }
}
