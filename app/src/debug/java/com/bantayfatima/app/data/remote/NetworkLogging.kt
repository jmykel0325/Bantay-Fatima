package com.bantayfatima.app.data.remote

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Debug variant of the HTTP logging hook. A release counterpart lives in
 * `src/release/java` and returns null.
 *
 * The logging interceptor is a `debugImplementation` dependency, so release builds
 * do not even contain the class.
 *
 * Level is [HttpLoggingInterceptor.Level.BASIC]: method, URL, status and timing only.
 * Headers and bodies stay out of Logcat so that Sanctum bearer tokens, passwords,
 * OTP codes and resident personal information are never printed once the
 * authenticated endpoints are added.
 */
internal object NetworkLogging {

    fun interceptor(): Interceptor? =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
}
