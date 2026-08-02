package com.bantayfatima.app.ui.connection

import com.bantayfatima.app.data.repository.ConnectionError

/**
 * Everything the connection screen needs to draw itself, and nothing else.
 *
 * The screen renders exactly one of these states, so loading, success and error can
 * never be shown at the same time.
 */
sealed interface ConnectionUiState {

    /** Nothing attempted yet. */
    data object Idle : ConnectionUiState

    /** A request is in flight. The test button is disabled while in this state. */
    data object Loading : ConnectionUiState

    /**
     * The API answered successfully.
     *
     * @param message the message returned by Laravel, e.g. "Bantay Fatima API is connected."
     * @param application, [environment], [serverTime] optional details; null when absent.
     */
    data class Success(
        val message: String?,
        val application: String?,
        val environment: String?,
        val serverTime: String?,
    ) : ConnectionUiState

    /** The call failed. [error] is mapped to a user-facing string by the screen. */
    data class Error(val error: ConnectionError) : ConnectionUiState
}
