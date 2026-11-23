package com.bantayfatima.app.data.repository

import android.content.Context
import android.net.Uri
import com.bantayfatima.app.data.model.*
import com.bantayfatima.app.data.remote.ApiClient
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.IOException

class StaffRepository {
    private val api get() = ApiClient.api

    suspend fun dashboard() = call { api.staffDashboard() }.mapCatching { requireNotNull(it.data) }

    suspend fun assigned(page: Int) = call { api.staffAssigned(page) }

    suspend fun report(id: Long) = call { api.staffReport(id) }.mapCatching { requireNotNull(it.data) }

    suspend fun notifications() = call { api.staffNotifications() }.map { it.data.orEmpty() }

    suspend fun markNotificationsRead() = call { api.markStaffNotificationsRead() }

    suspend fun progress(context: Context, id: Long, status: String, notes: String, photo: Uri?): Result<ReportDto> {
        val proof = try {
            withContext(Dispatchers.IO) {
                photo?.let { uri ->
                    val resolver = context.contentResolver
                    val mime = resolver.getType(uri)
                    require(mime?.startsWith("image/") == true) { "Choose an image for the proof photo." }
                    val limit = 5 * 1024 * 1024
                    val bytes = resolver.openInputStream(uri)?.use { java.io.ByteArrayOutputStream().also { output ->
                        val buffer = ByteArray(8192)
                        while (output.size() <= limit) {
                            val count = it.read(buffer, 0, minOf(buffer.size, limit + 1 - output.size()))
                            if (count < 0) break
                            output.write(buffer, 0, count)
                        }
                    }.toByteArray() }
                        ?: error("The selected photo could not be opened.")
                    require(bytes.size <= limit) { "Choose a proof photo under 5 MB." }
                    MultipartBody.Part.createFormData("proof_photo", "proof", bytes.toRequestBody(mime?.toMediaTypeOrNull()))
                }
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            return Result.failure(error)
        }
        val fields = listOf(
            MultipartBody.Part.createFormData("status", status),
            MultipartBody.Part.createFormData("notes", notes.trim()),
        )
        return call { api.staffProgress(id, fields, proof) }.mapCatching { requireNotNull(it.data) }
    }

    private suspend fun <T> call(request: suspend () -> Response<ApiEnvelope<T>>): Result<ApiEnvelope<T>> =
        try {
            val response = request()
            val body = response.body()
            if (response.isSuccessful && body?.success == true) {
                Result.success(body)
            } else {
                val error = response.errorBody()?.use {
                    runCatching { Gson().fromJson(it.string(), ApiEnvelope::class.java) }.getOrNull()
                } ?: body
                Result.failure(IOException(
                    error?.errors?.values?.firstOrNull()?.firstOrNull()
                        ?: error?.message ?: "Unable to complete the staff request.",
                ))
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: IOException) {
            Result.failure(IOException("Cannot reach the Bantay Fatima server.", error))
        } catch (error: Exception) {
            Result.failure(IOException("The server returned an unexpected response.", error))
        }
}
