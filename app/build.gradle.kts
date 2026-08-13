import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

/**
 * Debug base URL resolution.
 *
 * Default is the Android emulator loopback alias for the host machine (10.0.2.2).
 * To test on a physical phone, add your computer's own IPv4 address (from `ipconfig`)
 * to `local.properties`, which is untracked by version control:
 *
 *     bantayfatima.debugApiBaseUrl=http://192.168.x.x:8000/api/
 *
 * Never put passwords, database credentials or Laravel .env secrets in this file.
 */
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

val debugApiBaseUrl: String =
    (localProperties.getProperty("bantayfatima.debugApiBaseUrl")?.trim()?.takeIf { it.isNotEmpty() }
        ?: "http://10.0.2.2:8000/api/")
        .let { if (it.endsWith("/")) it else "$it/" }

// Replace with the real HTTPS domain when the production API is deployed.
val releaseApiBaseUrl = "https://your-production-domain.example/api/"

/**
 * OAuth *web* client ID used by Google sign-in.
 *
 * Create it in Google Cloud Console (APIs & Services -> Credentials -> OAuth client ID
 * -> Web application) and add it to `local.properties`, which is untracked:
 *
 *     bantayfatima.googleWebClientId=1234567890-abcdef.apps.googleusercontent.com
 *
 * The same ID must be configured on the Laravel side, because the server verifies
 * that the `aud` claim of the ID token matches it. This is a public client
 * identifier, not a secret, but it stays out of version control so each developer
 * points at their own Google Cloud project. When absent the app still builds and
 * runs; the Google buttons simply explain that sign-in is unavailable.
 */
val googleWebClientId: String =
    localProperties.getProperty("bantayfatima.googleWebClientId")?.trim().orEmpty()

android {
    namespace = "com.bantayfatima.app"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.bantayfatima.app"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleWebClientId\"")
    }

    buildTypes {
        debug {
            // Cleartext HTTP is permitted for this build type only, via
            // src/debug/AndroidManifest.xml + src/debug/res/xml/network_security_config.xml
            buildConfigField("String", "API_BASE_URL", "\"$debugApiBaseUrl\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // HTTPS only. Release builds never enable cleartext traffic.
            buildConfigField("String", "API_BASE_URL", "\"$releaseApiBaseUrl\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.core.splashscreen)

    // Google sign-in (Credential Manager). No Firebase; the ID token is exchanged
    // for a Sanctum token by the Laravel API.
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.google.id)

    // ViewModel + lifecycle-aware state collection in Compose
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Laravel REST API client
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)

    // HTTP logging is a debug-only dependency; see data/remote/NetworkLogging.kt
    debugImplementation(libs.okhttp.logging.interceptor)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
