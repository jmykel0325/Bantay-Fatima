package com.bantayfatima.app.data.model

import com.google.gson.annotations.SerializedName

data class ApiEnvelope<T>(val success: Boolean = false, val message: String? = null, val data: T? = null, val errors: Map<String, List<String>>? = null)
data class UserDto(
    val id: Long,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("middle_name") val middleName: String? = null,
    @SerializedName("last_name") val lastName: String,
    val suffix: String? = null,
    val email: String,
    val role: String,
    val status: String,
)
data class AuthData(val token: String, @SerializedName("token_type") val tokenType: String, val user: UserDto)
data class LoginRequest(val email: String, val password: String, val remember: Boolean, @SerializedName("device_name") val deviceName: String = "Bantay Fatima Android")
data class RegistrationRequest(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("middle_name") val middleName: String?,
    @SerializedName("last_name") val lastName: String,
    val suffix: String?,
    val email: String,
    @SerializedName("phone_number") val phoneNumber: String,
    val password: String,
    @SerializedName("password_confirmation") val passwordConfirmation: String,
    val terms: Boolean = true,
)
data class VerificationRequest(val email: String, val code: String, @SerializedName("device_name") val deviceName: String = "Bantay Fatima Android")
data class EmailRequest(val email: String)
data class VerificationMeta(val email: String, @SerializedName("masked_email") val maskedEmail: String, @SerializedName("expires_in") val expiresIn: Int, @SerializedName("resend_after") val resendAfter: Int)
data class PublicUpdate(
    val id: Long,
    val type: String? = null,
    val title: String,
    val summary: String? = null,
    val content: String? = null,
    @SerializedName("is_urgent") val isUrgent: Boolean = false,
    @SerializedName("published_at") val publishedAt: String? = null,
)
data class EmergencyInfo(
    val id: Long,
    val organization: String? = null,
    val name: String? = null,
    val category: String? = null,
    @SerializedName("hotline_number") val hotlineNumber: String? = null,
    val number: String? = null,
    @SerializedName("alternative_number") val alternativeNumber: String? = null,
    val address: String? = null,
    val availability: String? = null,
)
