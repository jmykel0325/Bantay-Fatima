package com.bantayfatima.app.data.repository

import com.bantayfatima.app.data.model.*
import com.bantayfatima.app.data.remote.ApiClient
import com.google.gson.Gson
import retrofit2.Response
import java.io.IOException

class AuthRepository {
    suspend fun restore(): Result<UserDto?> {
        if (ApiClient.tokens.readToken() == null) return Result.success(null)
        return call { ApiClient.api.currentUser() }.map { it.data }.onFailure { ApiClient.tokens.clear() }
    }

    suspend fun login(email: String, password: String, remember: Boolean): Result<UserDto> =
        call { ApiClient.api.login(LoginRequest(email.trim().lowercase(), password, remember)) }.mapCatching { envelope ->
            val auth = requireNotNull(envelope.data)
            ApiClient.tokens.saveToken(auth.token)
            auth.user
        }

    /**
     * Exchanges a Google ID token for a session.
     *
     * The same call covers both directions the resident can arrive from. If the
     * Gmail address already has an account the server signs them straight into it;
     * if it does not, the server creates the account from the Google profile — the
     * address is already proven, so no e-mail code step is needed.
     */
    suspend fun googleSignIn(idToken: String): Result<AuthData> =
        call { ApiClient.api.googleSignIn(GoogleAuthRequest(idToken)) }.mapCatching { envelope ->
            val auth = requireNotNull(envelope.data)
            ApiClient.tokens.saveToken(auth.token)
            auth
        }

    suspend fun requestCode(request: RegistrationRequest): Result<VerificationMeta> =
        call { ApiClient.api.requestRegistrationCode(request.copy(email = request.email.trim().lowercase())) }
            .mapCatching { requireNotNull(it.data) }

    suspend fun verify(email: String, code: String): Result<UserDto> =
        call { ApiClient.api.verifyRegistration(VerificationRequest(email.trim().lowercase(), code)) }.mapCatching {
            val auth = requireNotNull(it.data)
            ApiClient.tokens.saveToken(auth.token)
            auth.user
        }

    suspend fun resend(email: String): Result<VerificationMeta> =
        call { ApiClient.api.resendRegistration(EmailRequest(email.trim().lowercase())) }
            .mapCatching { requireNotNull(it.data) }

    suspend fun logout() {
        runCatching { ApiClient.api.logout() }
        ApiClient.tokens.clear()
    }

    /**
     * Performs a call and turns every outcome into a [Result].
     *
     * The network call itself throws — a refused connection or a timeout raises
     * [IOException] before there is any response to inspect. Without this the
     * exception escapes the ViewModel's coroutine and terminates the process, so the
     * application appears to close and restart instead of showing "cannot reach the
     * server". Nothing above this layer should ever see a thrown network failure.
     */
    private suspend fun <T> call(block: suspend () -> Response<ApiEnvelope<T>>): Result<ApiEnvelope<T>> =
        try {
            request(block())
        } catch (offline: IOException) {
            Result.failure(Exception("Cannot reach the Bantay Fatima server. Check your internet connection and try again."))
        } catch (unexpected: Exception) {
            Result.failure(Exception("Something went wrong. Please try again."))
        }

    private fun <T> request(response: Response<ApiEnvelope<T>>): Result<ApiEnvelope<T>> {
        if (response.isSuccessful) return response.body()?.let(Result.Companion::success) ?: Result.failure(Exception("The server returned an empty response."))
        val parsed = runCatching { Gson().fromJson(response.errorBody()?.string(), ApiEnvelope::class.java) }.getOrNull()
        val errors = parsed?.errors?.values?.firstOrNull()?.firstOrNull()
        return Result.failure(Exception(errors ?: parsed?.message ?: "Unable to reach the Bantay Fatima server. Please try again."))
    }
}
