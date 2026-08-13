package com.bantayfatima.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bantayfatima.app.data.model.PublicUpdate
import com.bantayfatima.app.ui.theme.*

/**
 * Base surface for all content cards.
 *
 * Separation comes from the background/surface contrast plus a soft shadow, with
 * only a whisper of a border. Heavy outlines around every element were what made
 * the previous design feel dense.
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    container: Color = MaterialTheme.colorScheme.surface,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = CardDefaults.cardColors(containerColor = container)
    val elevation = CardDefaults.cardElevation(defaultElevation = 1.dp, pressedElevation = 4.dp)
    if (onClick != null) {
        Card(onClick = onClick, modifier = modifier, shape = CardShape, colors = colors, elevation = elevation, content = content)
    } else {
        Card(modifier = modifier, shape = CardShape, colors = colors, elevation = elevation, content = content)
    }
}

/**
 * Landing hero.
 *
 * Replaces the full-bleed navy header: a contained gradient card that introduces
 * the service and carries the two primary actions.
 */
@Composable
fun HeroCard(
    title: String,
    description: String,
    primaryLabel: String,
    secondaryLabel: String,
    modifier: Modifier = Modifier,
    onPrimary: () -> Unit,
    onSecondary: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(HeroShape)
            .background(Brush.linearGradient(listOf(BantayNavy, BantayNavyDeep, BantayIndigo))),
    ) {
        Column(Modifier.padding(Spacing.xl)) {
            AuthorityLockup(sealSize = 42.dp, onDark = true)

            Spacer(Modifier.height(Spacing.lg))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = BantayWhite,
            )
            Spacer(Modifier.height(Spacing.sm))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = BantayWhite.copy(alpha = 0.85f),
            )

            Spacer(Modifier.height(Spacing.xl))
            Button(
                onClick = onPrimary,
                shape = ButtonShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BantayPrimary,
                    contentColor = BantayWhite,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = ControlHeight),
            ) {
                Text(primaryLabel, style = MaterialTheme.typography.labelLarge)
            }
            Spacer(Modifier.height(Spacing.sm))
            TextButton(
                onClick = onSecondary,
                shape = ButtonShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp),
            ) {
                Text(
                    text = secondaryLabel,
                    style = MaterialTheme.typography.titleSmall,
                    color = BantayWhite.copy(alpha = 0.92f),
                )
            }
        }
    }
}

/**
 * Compact tile in the quick-action strip.
 *
 * @param locked shows a padlock for guests. The lock is accompanied by the
 *   "Sign in required" content description so it is not colour/icon only.
 */
@Composable
fun QuickActionCard(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    locked: Boolean = false,
    onClick: () -> Unit,
) {
    AppCard(modifier = modifier.width(124.dp), onClick = onClick) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Box {
                Box(
                    Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(22.dp),
                    )
                }
                if (locked) {
                    Box(
                        Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 5.dp, y = (-5).dp)
                            .size(18.dp)
                            .clip(RoundedCornerShape(9.dp))
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = "Sign in required",
                            tint = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.size(10.dp),
                        )
                    }
                }
            }
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.heightIn(min = 42.dp),
            )
        }
    }
}

/** Service entry in the public feature list. */
@Composable
fun ServiceFeatureCard(
    title: String,
    description: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    locked: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    AppCard(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Row(
            Modifier.padding(Spacing.md),
            horizontalArrangement = Arrangement.spacedBy(Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(23.dp),
                )
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    if (locked) {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = "Sign in required",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(15.dp),
                        )
                    }
                }
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (onClick != null) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

/** Announcement or advisory card, used in the preview strip and the full list. */
@Composable
fun UpdateCard(
    update: PublicUpdate,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    AppCard(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Column(Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StatusBadge(update.type ?: "Announcement", StatusTone.Neutral)
                if (update.isUrgent) StatusBadge("Urgent", StatusTone.Urgent)
                Spacer(Modifier.weight(1f))
                update.publishedAt?.let {
                    Text(
                        text = it.take(10),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Spacer(Modifier.height(2.dp))
            Text(
                text = update.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            val body = update.summary ?: update.content.orEmpty()
            if (body.isNotBlank()) {
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

/**
 * Calm but visible emergency reminder.
 *
 * Deliberately not alarm-red across the whole card: residents see this on every
 * visit, so it stays readable rather than shouting.
 */
@Composable
fun EmergencyCard(
    modifier: Modifier = Modifier,
    title: String = "Need Immediate Assistance?",
    text: String = "Bantay Fatima does not replace official emergency services. Contact the appropriate hotline for urgent situations.",
    actionLabel: String = "View Emergency Hotlines",
    icon: ImageVector,
    onAction: () -> Unit,
) {
    val status = AppTheme.status
    AppCard(modifier = modifier.fillMaxWidth(), container = status.urgentContainer) {
        Column(Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(status.urgent.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(icon, contentDescription = null, tint = status.urgent, modifier = Modifier.size(21.dp))
                }
                Text(title, style = MaterialTheme.typography.titleMedium, color = status.urgentText)
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = status.urgentText.copy(alpha = 0.92f),
            )
            OutlinedButton(
                onClick = onAction,
                shape = ButtonShape,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, status.urgent.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = status.urgentText),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp),
            ) {
                Text(actionLabel, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

/** Dashboard figure tile. */
@Composable
fun SummaryCard(
    label: String,
    value: String,
    icon: ImageVector,
    accent: Color,
    accentContainer: Color,
    modifier: Modifier = Modifier,
) {
    AppCard(modifier = modifier) {
        Column(Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Box(
                Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(19.dp))
            }
            Text(value, style = MaterialTheme.typography.headlineSmall)
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/** Neutral information panel used for guidance and notices. */
@Composable
fun InfoCard(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    title: String? = null,
    accent: Color = MaterialTheme.colorScheme.secondary,
    container: Color = MaterialTheme.colorScheme.secondaryContainer,
    onContainer: Color = MaterialTheme.colorScheme.onSecondaryContainer,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardShape)
            .background(container)
            .padding(Spacing.md),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(21.dp))
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            title?.let { Text(it, style = MaterialTheme.typography.titleSmall, color = onContainer) }
            Text(text, style = MaterialTheme.typography.bodySmall, color = onContainer)
        }
    }
}
