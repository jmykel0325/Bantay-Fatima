package com.bantayfatima.app.data.repository

import android.content.Context
import android.net.Uri
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bantayfatima.app.data.model.*
import com.bantayfatima.app.data.remote.ApiClient
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.IOException
import java.io.ByteArrayOutputStream

/**
 * Authenticated resident data: report counts, recent reports, and the barangay
 * knowledge assistant.
 *
 * Follows the same rule as [AuthRepository]: a network failure becomes a
 * [Result.failure] carrying a sentence the resident can act on, never a thrown
 * exception. The dashboard collects several of these, and one unreachable endpoint
 * must degrade that section only, not take the screen down.
 */
class ResidentRepository {

    suspend fun dashboard(): Result<ResidentDashboard> =
        call { ApiClient.api.residentDashboard() }.mapCatching { requireNotNull(it.data) }

    suspend fun reports(): Result<List<ReportDto>> =
        call { ApiClient.api.residentReports() }.mapCatching { it.data.orEmpty() }

    suspend fun formOptions(): Result<ReportFormOptions> {
        val categories = call { ApiClient.api.reportCategories() }.getOrElse { return Result.failure(it) }
        val puroks = call { ApiClient.api.puroks() }.getOrElse { return Result.failure(it) }
        return Result.success(ReportFormOptions(categories.data.orEmpty(), puroks.data.orEmpty()))
    }

    suspend fun submitReport(
        context: Context,
        categoryId: Long,
        title: String,
        description: String,
        purokId: Long,
        landmark: String,
        latitude: Double,
        longitude: Double,
        submissionToken: String,
        photoUris: List<String>,
    ): Result<ReportDto> {
        fun field(name: String, value: String) = MultipartBody.Part.createFormData(name, value)
        val fields = listOf(
            field("category_id", categoryId.toString()), field("title", title.trim()),
            field("description", description.trim()), field("purok_id", purokId.toString()),
            field("location_notes", landmark.trim()), field("latitude", latitude.toString()),
            field("longitude", longitude.toString()), field("submission_token", submissionToken),
            field("confirm_accuracy", "1"),
        )
        val photos = photoUris.take(5).mapIndexedNotNull { index, value ->
            runCatching {
                val uri = Uri.parse(value)
                val source = context.contentResolver.openInputStream(uri)?.use(BitmapFactory::decodeStream) ?: return@runCatching null
                val longest = maxOf(source.width, source.height)
                val scaled = if (longest > 1600) {
                    val ratio = 1600f / longest
                    Bitmap.createScaledBitmap(source, (source.width * ratio).toInt(), (source.height * ratio).toInt(), true)
                } else source
                val output = ByteArrayOutputStream()
                scaled.compress(Bitmap.CompressFormat.JPEG, 82, output)
                if (scaled !== source) source.recycle()
                scaled.recycle()
                MultipartBody.Part.createFormData("photos[]", "report-${index + 1}.jpg", output.toByteArray().toRequestBody("image/jpeg".toMediaTypeOrNull()))
            }.getOrNull()
        }
        return call { ApiClient.api.submitReport(fields, photos) }.mapCatching { requireNotNull(it.data) }
    }

    /**
     * Asks the assistant a question.
     *
     * The answer is the envelope's `message`, and the API replies 503 with an
     * explanatory message when the knowledge base is unavailable — which [call]
     * surfaces as a failure carrying that same sentence.
     */
    suspend fun askAssistant(question: String): Result<AssistantReply> =
        call { ApiClient.api.assistantQuery(AssistantRequest(question.trim())) }.mapCatching { envelope ->
            AssistantReply(
                answer = envelope.message.orEmpty().ifBlank { "The assistant did not return an answer." },
                sources = envelope.data?.sources.orEmpty(),
            )
        }

    private suspend fun <T> call(block: suspend () -> Response<ApiEnvelope<T>>): Result<ApiEnvelope<T>> =
        try {
            handle(block())
        } catch (offline: IOException) {
            Result.failure(Exception("Cannot reach the Bantay Fatima server. Check your internet connection and try again."))
        } catch (unexpected: Exception) {
            Result.failure(Exception("Something went wrong. Please try again."))
        }

    private fun <T> handle(response: Response<ApiEnvelope<T>>): Result<ApiEnvelope<T>> {
        if (response.isSuccessful) {
            return response.body()?.let(Result.Companion::success)
                ?: Result.failure(Exception("The server returned an empty response."))
        }
        val parsed = runCatching { Gson().fromJson(response.errorBody()?.string(), ApiEnvelope::class.java) }.getOrNull()
        val errors = parsed?.errors?.values?.firstOrNull()?.firstOrNull()
        return Result.failure(Exception(errors ?: parsed?.message ?: "Unable to load your barangay information."))
    }
}

/** Assistant answer paired with the documents it was drawn from. */
data class AssistantReply(val answer: String, val sources: List<String>)
