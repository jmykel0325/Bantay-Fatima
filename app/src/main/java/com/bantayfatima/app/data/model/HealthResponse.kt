package com.bantayfatima.app.data.model

import com.google.gson.annotations.SerializedName

/**
 * Response body of `GET /api/health` on the Bantay Fatima Laravel API.
 *
 * Every field is nullable on purpose. Gson constructs instances without running the
 * Kotlin constructor, so a missing or renamed JSON key leaves the property null
 * instead of throwing. Callers must treat absent values as "unknown", never crash.
 */
data class HealthResponse(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: HealthData? = null,
)

data class HealthData(
    @SerializedName("application") val application: String? = null,
    @SerializedName("environment") val environment: String? = null,
    @SerializedName("server_time") val serverTime: String? = null,
)
