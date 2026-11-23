package com.bantayfatima.app.data.model

import com.google.gson.annotations.SerializedName

data class PageMeta(
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("last_page") val lastPage: Int,
)

data class StaffCounts(
    val total: Int = 0,
    val pending: Int = 0,
    @SerializedName("in_progress") val inProgress: Int = 0,
    @SerializedName("awaiting_confirmation") val awaitingConfirmation: Int = 0,
    val completed: Int = 0,
)

data class StaffDashboard(
    val counts: StaffCounts? = null,
    @SerializedName("newly_assigned") val newlyAssigned: List<ReportDto>? = null,
)

data class StaffNotification(
    val id: String,
    val data: StaffNotificationData? = null,
    @SerializedName("read_at") val readAt: String? = null,
)

data class StaffNotificationData(val title: String? = null, val message: String? = null)
