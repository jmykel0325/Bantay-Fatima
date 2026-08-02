package com.bantayfatima.app.ui.connection

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bantayfatima.app.BuildConfig
import com.bantayfatima.app.R
import com.bantayfatima.app.data.remote.ApiClient
import com.bantayfatima.app.data.repository.ConnectionError
import com.bantayfatima.app.ui.theme.BantayFatimaTheme

/**
 * Temporary first screen of the application: verifies that the phone can reach the
 * Bantay Fatima Laravel REST API.
 *
 * The composable holds no networking code. It observes [ConnectionViewModel] and
 * forwards button taps to it.
 */
@Composable
fun ConnectionScreen(
    modifier: Modifier = Modifier,
    viewModel: ConnectionViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ConnectionScreenContent(
        uiState = uiState,
        onTestConnection = viewModel::testConnection,
        onRetry = viewModel::retry,
        modifier = modifier,
    )
}

/**
 * Stateless rendering of [ConnectionUiState], so it can be previewed and tested
 * without a ViewModel or a running server.
 */
@Composable
fun ConnectionScreenContent(
    uiState: ConnectionUiState,
    onTestConnection: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                // Caps the content on tablets and large phones; on small screens the
                // column simply fills the available width, so nothing overflows sideways.
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 560.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.connection_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.connection_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(28.dp))

                ConnectionStatusCard(uiState = uiState)

                Spacer(Modifier.height(24.dp))

                val isLoading = uiState is ConnectionUiState.Loading
                val isError = uiState is ConnectionUiState.Error

                Button(
                    onClick = if (isError) onRetry else onTestConnection,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        // Comfortably above the 48dp minimum touch target.
                        .heightIn(min = 52.dp),
                ) {
                    if (isError) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        text = stringResource(
                            if (isError) R.string.connection_action_retry
                            else R.string.connection_action_test
                        ),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                if (BuildConfig.DEBUG) {
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = stringResource(R.string.connection_debug_base_url, ApiClient.baseUrl),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
private fun ConnectionStatusCard(uiState: ConnectionUiState, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(R.string.connection_status_heading),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                StatusIndicator(uiState)

                Spacer(Modifier.size(14.dp))

                Text(
                    text = statusMessage(uiState),
                    style = MaterialTheme.typography.bodyLarge,
                    color = when (uiState) {
                        is ConnectionUiState.Error -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    modifier = Modifier
                        .weight(1f)
                        // Announces status changes to TalkBack without stealing focus.
                        .semantics { liveRegion = LiveRegionMode.Polite },
                )
            }

            if (uiState is ConnectionUiState.Success) {
                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                Spacer(Modifier.height(12.dp))

                DetailRow(R.string.connection_detail_application, uiState.application)
                DetailRow(R.string.connection_detail_environment, uiState.environment)
                DetailRow(R.string.connection_detail_server_time, uiState.serverTime)
            }
        }
    }
}

@Composable
private fun StatusIndicator(uiState: ConnectionUiState) {
    when (uiState) {
        ConnectionUiState.Loading -> CircularProgressIndicator(
            modifier = Modifier.size(28.dp),
            strokeWidth = 3.dp,
        )

        is ConnectionUiState.Success -> StatusIcon(
            icon = Icons.Filled.CheckCircle,
            tint = MaterialTheme.colorScheme.primary,
            descriptionRes = R.string.connection_status_connected,
        )

        is ConnectionUiState.Error -> StatusIcon(
            icon = Icons.Filled.Warning,
            tint = MaterialTheme.colorScheme.error,
            descriptionRes = R.string.connection_status_failed,
        )

        ConnectionUiState.Idle -> StatusIcon(
            icon = Icons.Filled.Info,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            descriptionRes = R.string.connection_status_not_tested,
        )
    }
}

@Composable
private fun StatusIcon(icon: ImageVector, tint: Color, @StringRes descriptionRes: Int) {
    Icon(
        imageVector = icon,
        contentDescription = stringResource(descriptionRes),
        tint = tint,
        modifier = Modifier.size(28.dp),
    )
}

/** Label/value pair that wraps instead of pushing the card wider than the screen. */
@Composable
private fun DetailRow(@StringRes labelRes: Int, value: String?) {
    if (value.isNullOrBlank()) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.6f),
        )
    }
}

@Composable
private fun statusMessage(uiState: ConnectionUiState): String = when (uiState) {
    ConnectionUiState.Idle -> stringResource(R.string.connection_message_idle)
    ConnectionUiState.Loading -> stringResource(R.string.connection_message_loading)
    // Falls back to a local string if Laravel ever omits the message field.
    is ConnectionUiState.Success ->
        uiState.message?.takeIf { it.isNotBlank() } ?: stringResource(R.string.connection_status_connected)

    is ConnectionUiState.Error -> stringResource(uiState.error.messageRes())
}

@StringRes
private fun ConnectionError.messageRes(): Int = when (this) {
    ConnectionError.SERVER_UNAVAILABLE -> R.string.connection_error_server_unavailable
    ConnectionError.NETWORK -> R.string.connection_error_network
    ConnectionError.UNEXPECTED_RESPONSE -> R.string.connection_error_unexpected_response
    ConnectionError.UNKNOWN -> R.string.connection_error_unknown
}

@Preview(showBackground = true, name = "Idle")
@Composable
private fun ConnectionScreenIdlePreview() {
    BantayFatimaTheme {
        ConnectionScreenContent(ConnectionUiState.Idle, {}, {})
    }
}

@Preview(showBackground = true, name = "Success")
@Composable
private fun ConnectionScreenSuccessPreview() {
    BantayFatimaTheme {
        ConnectionScreenContent(
            uiState = ConnectionUiState.Success(
                message = "Bantay Fatima API is connected.",
                application = "Bantay Fatima",
                environment = "local",
                serverTime = "2026-08-02T04:00:00+08:00",
            ),
            onTestConnection = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun ConnectionScreenErrorPreview() {
    BantayFatimaTheme {
        ConnectionScreenContent(
            uiState = ConnectionUiState.Error(ConnectionError.SERVER_UNAVAILABLE),
            onTestConnection = {},
            onRetry = {},
        )
    }
}
