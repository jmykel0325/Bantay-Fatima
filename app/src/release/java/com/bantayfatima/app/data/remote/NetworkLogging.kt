package com.bantayfatima.app.data.remote

import okhttp3.Interceptor

/**
 * Release variant of the HTTP logging hook.
 *
 * Production builds never log request or response traffic, so no interceptor is
 * installed. Do not add logging here.
 */
internal object NetworkLogging {

    fun interceptor(): Interceptor? = null
}
