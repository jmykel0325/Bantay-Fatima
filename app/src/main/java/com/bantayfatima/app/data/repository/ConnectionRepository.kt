package com.bantayfatima.app.data.repository

import com.bantayfatima.app.data.remote.ApiClient
import com.bantayfatima.app.data.remote.BantayFatimaApi
import com.google.gson.JsonParseException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.EOFException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.io.IOException

/**
 * Talks to the Laravel API on behalf of the connection screen.
 *
 * This is the only layer that knows about Retrofit and about exceptions. It turns
 * every possible failure into a [ConnectionError] category, so nothing above it ever
 * has to catch an exception or render a stack trace.
 *
 * @param apiProvider deferred on purpose: constructing Retrofit can throw if the base
 *   URL is malformed, and that must be reported as a failure, not crash the ViewModel.
 */
class ConnectionRepository(
    private val apiProvider: () -> BantayFatimaApi = { ApiClient.api },
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend fun checkHealth(): ConnectionResult = withContext(ioDispatcher) {
        try {
            val response = apiProvider().health()

            if (!response.isSuccessful) {
                // 404 (route missing), 500 (Laravel error page), 419, HTML from a proxy...
                return@withContext ConnectionResult.Failure(ConnectionError.UNEXPECTED_RESPONSE)
            }

            val body = response.body()
                ?: return@withContext ConnectionResult.Failure(ConnectionError.UNEXPECTED_RESPONSE)

            if (body.success != true) {
                return@withContext ConnectionResult.Failure(ConnectionError.UNEXPECTED_RESPONSE)
            }

            ConnectionResult.Success(body)
        } catch (e: CancellationException) {
            // Coroutine cancellation is not a connection failure; it must propagate.
            throw e
        } catch (e: UnknownHostException) {
            // Host name could not be resolved: usually no network at all.
            ConnectionResult.Failure(ConnectionError.NETWORK)
        } catch (e: ConnectException) {
            // Route exists but the port refused the connection: `php artisan serve` not running.
            ConnectionResult.Failure(ConnectionError.SERVER_UNAVAILABLE)
        } catch (e: NoRouteToHostException) {
            ConnectionResult.Failure(ConnectionError.SERVER_UNAVAILABLE)
        } catch (e: SocketTimeoutException) {
            ConnectionResult.Failure(ConnectionError.SERVER_UNAVAILABLE)
        } catch (e: JsonParseException) {
            // Body was not the JSON shape we expect (an HTML error page, for instance).
            ConnectionResult.Failure(ConnectionError.UNEXPECTED_RESPONSE)
        } catch (e: EOFException) {
            ConnectionResult.Failure(ConnectionError.UNEXPECTED_RESPONSE)
        } catch (e: IOException) {
            // Any other transport-level problem: dropped Wi-Fi, TLS failure, cleartext blocked.
            ConnectionResult.Failure(ConnectionError.NETWORK)
        } catch (e: IllegalArgumentException) {
            // Malformed BuildConfig.API_BASE_URL, thrown while Retrofit is being built.
            ConnectionResult.Failure(ConnectionError.UNKNOWN)
        } catch (e: RuntimeException) {
            ConnectionResult.Failure(ConnectionError.UNKNOWN)
        }
    }
}
