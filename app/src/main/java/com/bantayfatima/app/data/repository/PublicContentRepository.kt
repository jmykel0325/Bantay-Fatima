package com.bantayfatima.app.data.repository

import com.bantayfatima.app.data.model.EmergencyInfo
import com.bantayfatima.app.data.model.PublicUpdate
import com.bantayfatima.app.data.remote.ApiClient

class PublicContentRepository {
    suspend fun updates(): Result<List<PublicUpdate>> = runCatching {
        val response = ApiClient.api.publicUpdates()
        if (!response.isSuccessful) error("Unable to load barangay updates.")
        response.body()?.data.orEmpty()
    }
    suspend fun emergency(): Result<List<EmergencyInfo>> = runCatching {
        val response = ApiClient.api.emergencyInformation()
        if (!response.isSuccessful) error("Unable to load emergency information.")
        response.body()?.data.orEmpty()
    }
}
