package com.bantayfatima.app.data.repository

import com.bantayfatima.app.data.model.HealthResponse

/**
 * Outcome of a call to the Bantay Fatima API.
 *
 * Failures carry a [ConnectionError] category rather than a message string. The UI
 * layer resolves the category to a localised, human-readable string resource, so raw
 * exception text and stack traces never reach the screen.
 */
sealed interface ConnectionResult {

    data class Success(val health: HealthResponse) : ConnectionResult

    data class Failure(val error: ConnectionError) : ConnectionResult
}

enum class ConnectionError {

    /** Reached the network but nothing answered: Laravel is stopped, wrong port, or timed out. */
    SERVER_UNAVAILABLE,

    /** No usable connection: airplane mode, Wi-Fi down, or the host name cannot be resolved. */
    NETWORK,

    /** Answered, but not with the JSON the app expects: HTTP error status, empty or invalid body. */
    UNEXPECTED_RESPONSE,

    /** Anything else. Kept deliberately vague in the UI. */
    UNKNOWN,
}
