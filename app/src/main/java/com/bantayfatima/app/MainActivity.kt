package com.bantayfatima.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bantayfatima.app.navigation.BantayFatimaApp
import com.bantayfatima.app.data.remote.ApiClient
import com.bantayfatima.app.ui.theme.BantayFatimaTheme

/**
 * Single activity host.
 *
 * It only sets the theme and the root composable. No networking happens here: the
 * chain is Compose screen -> ViewModel -> repository -> Retrofit -> Laravel API.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setOnExitAnimationListener { provider ->
            provider.view.animate()
                .alpha(0f)
                .scaleX(1.03f)
                .scaleY(1.03f)
                .setDuration(220L)
                .withEndAction(provider::remove)
                .start()
        }
        ApiClient.initialize(applicationContext)
        enableEdgeToEdge()
        setContent {
            BantayFatimaTheme {
                BantayFatimaApp()
            }
        }
    }
}
