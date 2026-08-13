package com.bantayfatima.app.ui.resident

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bantayfatima.app.data.model.PurokDto
import com.bantayfatima.app.data.model.ReportCategoryDto
import com.bantayfatima.app.data.model.ReportDto
import com.bantayfatima.app.data.repository.ResidentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class ReportFormState(
    val step: Int = 1,
    val categories: List<ReportCategoryDto> = emptyList(),
    val puroks: List<PurokDto> = emptyList(),
    val loadingOptions: Boolean = true,
    val categoryId: Long? = null,
    val categoryName: String = "",
    val specifyConcern: String = "",
    val title: String = "",
    val description: String = "",
    val purokId: Long? = null,
    val purokName: String = "",
    val landmark: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val photos: List<String> = emptyList(),
    val declaration: Boolean = false,
    val submitting: Boolean = false,
    val error: String? = null,
    val submitted: ReportDto? = null,
    val submissionToken: String = UUID.randomUUID().toString(),
)

class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ResidentRepository()
    private val prefs = application.getSharedPreferences("report_draft", 0)
    private val _state = MutableStateFlow(loadDraft())
    val state: StateFlow<ReportFormState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.formOptions().fold(
                { options -> _state.update { it.copy(categories = options.categories, puroks = options.puroks, loadingOptions = false) } },
                { _state.update { it.copy(loadingOptions = false, error = "Unable to load report options. Please check your connection and try again.") } },
            )
        }
    }

    fun update(block: (ReportFormState) -> ReportFormState) { _state.update(block) }
    fun selectCategory(item: ReportCategoryDto) = update { it.copy(categoryId = item.id, categoryName = item.name.orEmpty(), specifyConcern = if (item.name.equals("Other", true)) it.specifyConcern else "") }
    fun selectPurok(item: PurokDto) = update { it.copy(purokId = item.id, purokName = item.name.orEmpty()) }
    fun setLocation(lat: Double, lon: Double) = update { it.copy(latitude = lat.coerceIn(-90.0, 90.0), longitude = lon.coerceIn(-180.0, 180.0), error = null) }
    fun addPhotos(values: List<String>) = update { it.copy(photos = (it.photos + values).distinct().take(5)) }
    fun removePhoto(value: String) = update { it.copy(photos = it.photos - value) }
    fun goTo(step: Int) = update { it.copy(step = step.coerceIn(1, 3), error = null) }

    fun detailsError(): String? = state.value.let {
        when {
            it.categoryId == null -> "Please select a report category."
            it.categoryName.equals("Other", true) && it.specifyConcern.isBlank() -> "Please specify the concern."
            it.title.trim().length !in 5..100 -> "Please enter a short title for the concern (5–100 characters)."
            it.description.isBlank() -> "Please describe the concern."
            it.description.length > 500 -> "Description must not exceed 500 characters."
            it.purokId == null -> "Please select your purok."
            else -> null
        }
    }

    fun continueFromDetails() {
        val error = detailsError()
        if (error == null) goTo(2) else update { it.copy(error = error) }
    }

    fun continueFromLocation() {
        if (state.value.latitude == null || state.value.longitude == null) update { it.copy(error = "Please select the location of the concern.") }
        else goTo(3)
    }

    fun saveDraft() {
        val s = state.value
        prefs.edit().putString("categoryId", s.categoryId?.toString()).putString("categoryName", s.categoryName)
            .putString("specify", s.specifyConcern).putString("title", s.title).putString("description", s.description)
            .putString("purokId", s.purokId?.toString()).putString("purokName", s.purokName).putString("landmark", s.landmark)
            .putString("latitude", s.latitude?.toString()).putString("longitude", s.longitude?.toString())
            .putStringSet("photos", s.photos.toSet()).apply()
        update { it.copy(error = "Draft saved on this device.") }
    }

    fun submit() {
        val s = state.value
        if (s.submitting || !s.declaration || detailsError() != null || s.latitude == null || s.longitude == null) return
        update { it.copy(submitting = true, error = null) }
        viewModelScope.launch {
            repository.submitReport(getApplication(), s.categoryId!!, s.title,
                if (s.categoryName.equals("Other", true)) "${s.specifyConcern.trim()}\n\n${s.description.trim()}" else s.description,
                s.purokId!!, s.landmark, s.latitude, s.longitude, s.submissionToken, s.photos).fold(
                { report -> prefs.edit().clear().apply(); _state.update { it.copy(submitting = false, submitted = report) } },
                { _state.update { it.copy(submitting = false, error = "Your report could not be submitted. Your information is still saved here. Please check your connection and try again.") } },
            )
        }
    }

    fun reset() { prefs.edit().clear().apply(); _state.value = ReportFormState(loadingOptions = false, categories = state.value.categories, puroks = state.value.puroks) }

    private fun loadDraft() = ReportFormState(
        categoryId = prefs.getString("categoryId", null)?.toLongOrNull(), categoryName = prefs.getString("categoryName", "").orEmpty(),
        specifyConcern = prefs.getString("specify", "").orEmpty(), title = prefs.getString("title", "").orEmpty(),
        description = prefs.getString("description", "").orEmpty(), purokId = prefs.getString("purokId", null)?.toLongOrNull(),
        purokName = prefs.getString("purokName", "").orEmpty(), landmark = prefs.getString("landmark", "").orEmpty(),
        latitude = prefs.getString("latitude", null)?.toDoubleOrNull(), longitude = prefs.getString("longitude", null)?.toDoubleOrNull(),
        photos = prefs.getStringSet("photos", emptySet()).orEmpty().toList(),
    )
}
