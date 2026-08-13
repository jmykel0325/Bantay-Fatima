package com.bantayfatima.app.data.remote

import com.bantayfatima.app.data.model.HealthResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Multipart
import retrofit2.http.Part
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    /** Single endpoint for both Google sign-in and Google sign-up; see [AuthData.isNewAccount]. */
    @POST("auth/google") suspend fun googleSignIn(@Body request: GoogleAuthRequest): Response<ApiEnvelope<AuthData>>
    @POST("auth/register/request-code") suspend fun requestRegistrationCode(@Body request: RegistrationRequest): Response<ApiEnvelope<VerificationMeta>>
    @POST("auth/register/verify-code") suspend fun verifyRegistration(@Body request: VerificationRequest): Response<ApiEnvelope<AuthData>>
    @POST("auth/register/resend-code") suspend fun resendRegistration(@Body request: EmailRequest): Response<ApiEnvelope<VerificationMeta>>
    @GET("user") suspend fun currentUser(): Response<ApiEnvelope<UserDto>>
    @POST("auth/logout") suspend fun logout(): Response<ApiEnvelope<Any>>

    // Authenticated resident endpoints. The Sanctum token is attached by ApiClient's
    // interceptor, so these need no explicit header.
    @GET("resident/dashboard") suspend fun residentDashboard(): Response<ApiEnvelope<ResidentDashboard>>
    @GET("resident/reports") suspend fun residentReports(): Response<ApiEnvelope<List<ReportDto>>>
    @GET("resident/report-categories") suspend fun reportCategories(): Response<ApiEnvelope<List<ReportCategoryDto>>>
    @GET("resident/puroks") suspend fun puroks(): Response<ApiEnvelope<List<PurokDto>>>
    @Multipart
    @POST("resident/reports")
    suspend fun submitReport(
        @Part fields: List<MultipartBody.Part>,
        @Part photos: List<MultipartBody.Part>,
    ): Response<ApiEnvelope<ReportDto>>
    @POST("resident/assistant/query") suspend fun assistantQuery(@Body request: AssistantRequest): Response<ApiEnvelope<AssistantSources>>
}
