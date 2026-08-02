package com.bantayfatima.app.data.repository

import com.bantayfatima.app.data.model.*
import com.bantayfatima.app.data.remote.ApiClient
import com.google.gson.Gson
import retrofit2.Response

class AuthRepository {
    suspend fun restore(): Result<UserDto?> {
        if (ApiClient.tokens.readToken() == null) return Result.success(null)
        return request(ApiClient.api.currentUser()).map { it.data }.onFailure { ApiClient.tokens.clear() }
    }

    suspend fun login(email: String, password: String, remember: Boolean): Result<UserDto> =
        request(ApiClient.api.login(LoginRequest(email.trim().lowercase(), password, remember))).mapCatching { envelope ->
            val auth = requireNotNull(envelope.data)
            ApiClient.tokens.saveToken(auth.token)
            auth.user
        }

    suspend fun requestCode(request: RegistrationRequest): Result<VerificationMeta> =
        request(ApiClient.api.requestRegistrationCode(request.copy(email = request.email.trim().lowercase()))).mapCatching { requireNotNull(it.data) }

    suspend fun verify(email: String, code: String): Result<UserDto> =
        request(ApiClient.api.verifyRegistration(VerificationRequest(email.trim().lowercase(), code))).mapCatching {
            val auth = requireNotNull(it.data)
            ApiClient.tokens.saveToken(auth.token)
            auth.user
        }

    suspend fun resend(email: String): Result<VerificationMeta> =
        request(ApiClient.api.resendRegistration(EmailRequest(email.trim().lowercase()))).mapCatching { requireNotNull(it.data) }

    suspend fun logout() {
        runCatching { ApiClient.api.logout() }
        ApiClient.tokens.clear()
    }

    private fun <T> request(response: Response<ApiEnvelope<T>>): Result<ApiEnvelope<T>> {
        if (response.isSuccessful) return response.body()?.let(Result.Companion::success) ?: Result.failure(Exception("The server returned an empty response."))
        val parsed = runCatching { Gson().fromJson(response.errorBody()?.string(), ApiEnvelope::class.java) }.getOrNull()
        val errors = parsed?.errors?.values?.firstOrNull()?.firstOrNull()
        return Result.failure(Exception(errors ?: parsed?.message ?: "Unable to reach the Bantay Fatima server. Please try again."))
    }
}
