package com.bantayfatima.app.ui.resident

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bantayfatima.app.data.repository.ResidentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AssistantMessage(
    val text: String,
    val fromResident: Boolean = false,
    val sources: List<String> = emptyList(),
    val isError: Boolean = false,
)

class AssistantViewModel : ViewModel() {
    private val repository = ResidentRepository()
    private val _messages = MutableStateFlow(listOf(AssistantMessage(
        "Good day. Ask me about barangay services and I will answer using approved Barangay Fatima information.",
    )))
    val messages = _messages.asStateFlow()
    private val _busy = MutableStateFlow(false)
    val busy = _busy.asStateFlow()

    fun send(question: String) {
        val clean = question.trim()
        if (clean.length < 3 || _busy.value) return
        _busy.value = true
        _messages.value = _messages.value + AssistantMessage(clean, fromResident = true)
        viewModelScope.launch {
            try {
                val message = repository.askAssistant(clean).fold(
                    { AssistantMessage(it.answer, sources = it.sources) },
                    { AssistantMessage(it.message ?: "The assistant is unavailable.", isError = true) },
                )
                _messages.value = _messages.value + message
            } finally {
                _busy.value = false
            }
        }
    }
}
