package com.bantayfatima.app.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bantayfatima.app.data.model.RegistrationRequest
import com.bantayfatima.app.data.model.UserDto
import com.bantayfatima.app.data.remote.GoogleIdentityClient
import com.bantayfatima.app.data.remote.GoogleSignInCancelled
import com.bantayfatima.app.data.repository.AuthRepository
import kotlinx.coroutines.CancellationException
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
    /** Tracked apart from [busy] so the spinner appears on the button that was tapped. */
    val googleBusy: Boolean = false,
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

    /**
     * One entry point for both "Continue with Google" and "Sign up with Google".
     *
     * The device only proves which Gmail address the resident controls; the API
     * decides whether that address already has an account. An existing account is
     * signed straight in, a new address is registered and signed in, and either way
     * the resident lands on their dashboard without an e-mail code step.
     *
     * [context] must be the activity, because Credential Manager shows the account
     * chooser over it.
     */
    fun signInWithGoogle(context: Context, onSuccess: (isNewAccount: Boolean) -> Unit) = viewModelScope.launch {
        _state.value = _state.value.copy(googleBusy = true, error = null)
        try {
            GoogleIdentityClient(context).requestIdToken().fold(
                onSuccess = { idToken ->
                    repository.googleSignIn(idToken).fold({ auth ->
                        setSession(auth.user)
                        onSuccess(auth.isNewAccount)
                    }, ::showError)
                },
                onFailure = { error ->
                    // Dismissing the chooser is a choice, not a problem to report.
                    if (error !== GoogleSignInCancelled) showError(error)
                },
            )
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (failure: Exception) {
            showError(failure)
        } finally {
            _state.value = _state.value.copy(googleBusy = false)
        }
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

    /**
     * Runs an authentication step with the busy flag held for its duration.
     *
     * The catch is deliberate and must stay. An exception thrown here propagates out
     * of the coroutine and terminates the process, which the resident experiences as
     * the application closing and restarting itself mid-login. A failure belongs in
     * the error banner, never in a crash.
     */
    private fun launch(block: suspend () -> Unit) = viewModelScope.launch {
        _state.value = _state.value.copy(busy = true, error = null)
        try {
            block()
        } catch (cancellation: CancellationException) {
            throw cancellation // Normal scope teardown, not a failure to report.
        } catch (failure: Exception) {
            showError(failure)
        } finally {
            _state.value = _state.value.copy(busy = false)
        }
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
