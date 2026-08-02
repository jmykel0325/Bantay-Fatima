package com.bantayfatima.app.ui.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bantayfatima.app.R
import com.bantayfatima.app.ui.theme.BantayGreen
import com.bantayfatima.app.ui.theme.BantayNavy
import com.bantayfatima.app.ui.theme.BantayNavyDeep

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { entered = true }
    val alpha by animateFloatAsState(if (entered) 1f else 0f, tween(500), label = "splashFade")
    val scale by animateFloatAsState(if (entered) 1f else .94f, tween(500), label = "splashScale")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BantayNavy, BantayNavyDeep)))
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center).padding(horizontal = 32.dp).alpha(alpha).scale(scale),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.bantay_fatima_logo),
                contentDescription = "Bantay Fatima logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.sizeIn(minWidth = 92.dp, minHeight = 92.dp, maxWidth = 124.dp, maxHeight = 124.dp).fillMaxWidth(.34f),
            )
            Spacer(Modifier.height(22.dp))
            Text("Bantay Fatima", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text("Report. Track. Stay Informed.", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = .82f), textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            Box(Modifier.width(34.dp).height(3.dp).background(Color(0xFFC9A227), CircleShape))
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator(color = BantayGreen, trackColor = Color.White.copy(alpha = .18f), strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(18.dp))
            Text("Barangay Fatima · General Santos City", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = .62f), textAlign = TextAlign.Center)
        }
    }
}
