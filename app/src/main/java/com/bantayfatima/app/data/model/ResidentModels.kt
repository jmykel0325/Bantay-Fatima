package com.bantayfatima.app.data.model

import com.google.gson.annotations.SerializedName

/**
 * Resident report data, as returned by `GET /api/resident/dashboard`.
 *
 * Every field is nullable or defaulted on purpose. These objects are built by Gson
 * straight from the Laravel payload, and a column the API stops sending must leave a
 * blank on the dashboard rather than throwing while the screen composes.
 */
data class ReportCounts(
    val total: Int = 0,
    val pending: Int = 0,
    @SerializedName("in_progress") val inProgress: Int = 0,
    val resolved: Int = 0,
)

data class PurokDto(val id: Long? = null, val name: String? = null)

data class ReportCategoryDto(val id: Long? = null, val name: String? = null)

data class ReportFormOptions(
    val categories: List<ReportCategoryDto> = emptyList(),
    val puroks: List<PurokDto> = emptyList(),
)

data class ReportPhotoDto(val id: Long? = null, val path: String? = null)

data class ReportDto(
    val id: Long,
    @SerializedName("reference_number") val referenceNumber: String? = null,
    val title: String? = null,
    val description: String? = null,
    /** One of pending, in_progress, resolved, rejected. */
    val status: String? = null,
    val priority: String? = null,
    val category: ReportCategoryDto? = null,
    val purok: PurokDto? = null,
    val photos: List<ReportPhotoDto>? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null,
)

data class ResidentDashboard(
    val counts: ReportCounts = ReportCounts(),
    @SerializedName("recent_reports") val recentReports: List<ReportDto> = emptyList(),
)

/** Question sent to the barangay knowledge assistant. */
data class AssistantRequest(val question: String)

/**
 * Assistant reply.
 *
 * The answer itself arrives in the envelope's `message`; this carries the documents
 * it was drawn from, which is what makes the answer checkable.
 */
data class AssistantSources(val sources: List<String>? = null)
