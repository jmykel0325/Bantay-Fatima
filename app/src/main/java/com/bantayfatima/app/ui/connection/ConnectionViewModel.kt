package com.bantayfatima.app.ui.connection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bantayfatima.app.data.repository.ConnectionRepository
import com.bantayfatima.app.data.repository.ConnectionResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Owns the connection-test state.
 *
 * Living in a ViewModel means the state survives recomposition and configuration
 * changes such as screen rotation: a request started before a rotation keeps running
 * and its result still lands on the rebuilt screen.
 *
 * All parameters have defaults, so the standard `viewModel()` factory can construct
 * this without a custom factory. When dependency injection is introduced later, the
 * repository can be supplied here instead.
 */
class ConnectionViewModel(
    private val repository: ConnectionRepository = ConnectionRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConnectionUiState>(ConnectionUiState.Idle)
    val uiState: StateFlow<ConnectionUiState> = _uiState.asStateFlow()

    /**
     * Calls `GET /api/health`.
     *
     * Ignored while a request is already running, so repeated taps cannot queue up
     * duplicate calls.
     */
    fun testConnection() {
        if (_uiState.value is ConnectionUiState.Loading) return

        _uiState.value = ConnectionUiState.Loading

        viewModelScope.launch {
            _uiState.value = when (val result = repository.checkHealth()) {
                is ConnectionResult.Success -> ConnectionUiState.Success(
                    message = result.health.message,
                    application = result.health.data?.application,
                    environment = result.health.data?.environment,
                    serverTime = result.health.data?.serverTime,
                )

                is ConnectionResult.Failure -> ConnectionUiState.Error(result.error)
            }
        }
    }

    /** Same request, exposed separately so the error state can offer a clear retry action. */
    fun retry() = testConnection()
}
