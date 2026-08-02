package com.bantayfatima.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bantayfatima.app.data.model.RegistrationRequest
import com.bantayfatima.app.data.model.UserDto
import com.bantayfatima.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

sealed interface SessionState {
    data object Loading : SessionState
    data object Guest : SessionState
    data class Resident(val user: UserDto) : SessionState
    data class Staff(val user: UserDto) : SessionState
}

data class AuthUiState(
    val session: SessionState = SessionState.Loading,
    val busy: Boolean = false,
    val error: String? = null,
    val verificationEmail: String? = null,
    val maskedEmail: String? = null,
    val resendSeconds: Int = 0,
)

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    init { restoreSession() }

    fun restoreSession() = launch {
        repository.restore().fold(::setSession) { _state.value = AuthUiState(session = SessionState.Guest) }
    }

    fun login(email: String, password: String, remember: Boolean, onSuccess: () -> Unit) = launch {
        repository.login(email, password, remember).fold({ setSession(it); onSuccess() }, ::showError)
    }

    fun requestRegistration(request: RegistrationRequest, onCodeSent: () -> Unit) = launch {
        repository.requestCode(request).fold({
            _state.value = _state.value.copy(verificationEmail = it.email, maskedEmail = it.maskedEmail, resendSeconds = it.resendAfter)
            startCooldown()
            onCodeSent()
        }, ::showError)
    }

    fun verify(code: String, onSuccess: () -> Unit) = launch {
        val email = _state.value.verificationEmail ?: return@launch showError(Exception("Registration session expired. Please start again."))
        repository.verify(email, code).fold({ setSession(it); onSuccess() }, ::showError)
    }

    fun resend() = launch {
        val email = _state.value.verificationEmail ?: return@launch
        repository.resend(email).fold({ _state.value = _state.value.copy(maskedEmail = it.maskedEmail, resendSeconds = it.resendAfter); startCooldown() }, ::showError)
    }

    fun tickCooldown() { if (_state.value.resendSeconds > 0) _state.value = _state.value.copy(resendSeconds = _state.value.resendSeconds - 1) }
    fun clearError() { _state.value = _state.value.copy(error = null) }
    fun logout() = viewModelScope.launch { repository.logout(); _state.value = AuthUiState(session = SessionState.Guest) }

    private fun launch(block: suspend () -> Unit) = viewModelScope.launch {
        _state.value = _state.value.copy(busy = true, error = null)
        try { block() } finally { _state.value = _state.value.copy(busy = false) }
    }

    private fun setSession(user: UserDto?) {
        _state.value = _state.value.copy(session = when (user?.role) {
            "resident" -> SessionState.Resident(user)
            "staff" -> SessionState.Staff(user)
            else -> SessionState.Guest
        }, error = null)
    }

    private fun showError(error: Throwable) { _state.value = _state.value.copy(error = error.message ?: "Something went wrong.") }

    private fun startCooldown() = viewModelScope.launch {
        while (_state.value.resendSeconds > 0) { delay(1000); tickCooldown() }
    }
}
