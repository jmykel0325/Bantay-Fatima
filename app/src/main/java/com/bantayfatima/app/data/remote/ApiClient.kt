package com.bantayfatima.app.data.remote

import com.bantayfatima.app.BuildConfig
import okhttp3.OkHttpClient
import android.content.Context
import com.bantayfatima.app.data.local.TokenStorage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Single Retrofit instance for the whole application.
 *
 * The base URL comes from [BuildConfig.API_BASE_URL], which the Gradle build sets per
 * build type: `http://10.0.2.2:8000/api/` for debug, an HTTPS placeholder for release.
 *
 * The app only ever speaks HTTP(S) to the Laravel API. It holds no database driver,
 * no MySQL credentials and no connection to port 3306.
 *
 * Everything is created lazily so that a malformed base URL surfaces as a caught
 * failure inside the repository rather than a crash while the ViewModel is built.
 */
object ApiClient {

    private lateinit var tokenStorage: TokenStorage

    fun initialize(context: Context) {
        if (!::tokenStorage.isInitialized) tokenStorage = TokenStorage(context.applicationContext)
    }

    private const val CONNECT_TIMEOUT_SECONDS = 15L
    private const val READ_TIMEOUT_SECONDS = 20L
    private const val WRITE_TIMEOUT_SECONDS = 20L

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().apply {
                    if (::tokenStorage.isInitialized) {
                        tokenStorage.readToken()?.let { header("Authorization", "Bearer $it") }
                    }
                    header("Accept", "application/json")
                }.build()
                chain.proceed(request)
            }
            .apply {
                // Present in debug builds only; null in release.
                NetworkLogging.interceptor()?.let(::addInterceptor)
            }
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: BantayFatimaApi by lazy { retrofit.create(BantayFatimaApi::class.java) }

    val tokens: TokenStorage get() = tokenStorage

    /** Shown on the debug connection screen only. Never displayed in release builds. */
    val baseUrl: String get() = BuildConfig.API_BASE_URL
}
