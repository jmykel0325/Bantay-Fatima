package com.bantayfatima.app.data.remote

import com.bantayfatima.app.data.model.HealthResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST
import com.bantayfatima.app.data.model.*

/**
 * Retrofit description of the Bantay Fatima Laravel REST API.
 *
 * Paths are relative to [ApiClient]'s base URL, so `health` resolves to
 * `http://10.0.2.2:8000/api/health` on a debug emulator build.
 *
 * Endpoints return [Response] rather than the body directly, so the repository can
 * inspect the HTTP status code without relying on thrown exceptions.
 *
 * Future stages add the authenticated resident and staff endpoints here
 * (login, reports, announcements, notifications, profile).
 */
interface BantayFatimaApi {

    @GET("health")
    suspend fun health(): Response<HealthResponse>

    @GET("public/announcements") suspend fun publicUpdates(): Response<ApiEnvelope<List<PublicUpdate>>>
    @GET("public/emergency-information") suspend fun emergencyInformation(): Response<ApiEnvelope<List<EmergencyInfo>>>
    @POST("auth/login") suspend fun login(@Body request: LoginRequest): Response<ApiEnvelope<AuthData>>
    @POST("auth/register/request-code") suspend fun requestRegistrationCode(@Body request: RegistrationRequest): Response<ApiEnvelope<VerificationMeta>>
    @POST("auth/register/verify-code") suspend fun verifyRegistration(@Body request: VerificationRequest): Response<ApiEnvelope<AuthData>>
    @POST("auth/register/resend-code") suspend fun resendRegistration(@Body request: EmailRequest): Response<ApiEnvelope<VerificationMeta>>
    @GET("user") suspend fun currentUser(): Response<ApiEnvelope<UserDto>>
    @POST("auth/logout") suspend fun logout(): Response<ApiEnvelope<Any>>
}
