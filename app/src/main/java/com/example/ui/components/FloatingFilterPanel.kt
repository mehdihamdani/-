package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MosqueEmerald
import com.example.ui.theme.NaftalFlame
import com.example.ui.theme.TealPrimary

@Composable
fun FloatingFilterPanel(
    showMosques: Boolean,
    showPublicToilets: Boolean,
    showRestAreas: Boolean,
    onToggleMosques: () -> Unit,
    onTogglePublicToilets: () -> Unit,
    onToggleRestAreas: () -> Unit,
    onResetAll: () -> Unit,
    modifier: Modifier = Modifier,
    activeCount: Int? = null
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
            )
            .testTag("floating_filter_panel"),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // Header: Title & status or quick reset
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(TealPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = TealPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = "فلترة العرض السريعة",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (activeCount != null) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = TealPrimary.copy(alpha = 0.12f),
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Text(
                                text = "$activeCount متاح",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = TealPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Show all toggle if any is disabled
                val anyDisabled = !showMosques || !showPublicToilets || !showRestAreas
                if (anyDisabled) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                        modifier = Modifier.clickable { onResetAll() }
                    ) {
                        Text(
                            text = "إظهار الكل",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                } else {
                    Text(
                        text = "انقر للإخفاء أو الإظهار",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3 Toggle Buttons: Mosques, Public Toilets, Rest Areas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. Mosques (مساجد ووضوء)
                FilterCategoryToggleItem(
                    title = "مساجد",
                    subtitle = "ووضوء",
                    icon = Icons.Default.Mosque,
                    themeColor = MosqueEmerald,
                    isActive = showMosques,
                    onClick = onToggleMosques,
                    testTag = "toggle_mosques",
                    modifier = Modifier.weight(1f)
                )

                // 2. Public Toilets (مراحيض عمومية)
                FilterCategoryToggleItem(
                    title = "مراحيض",
                    subtitle = "عمومية",
                    icon = Icons.Default.Wc,
                    themeColor = TealPrimary,
                    isActive = showPublicToilets,
                    onClick = onTogglePublicToilets,
                    testTag = "toggle_public_toilets",
                    modifier = Modifier.weight(1f)
                )

                // 3. Rest Areas (استراحات / نفطال)
                FilterCategoryToggleItem(
                    title = "استراحات",
                    subtitle = "نفطال وطرق",
                    icon = Icons.Default.LocalGasStation,
                    themeColor = NaftalFlame,
                    isActive = showRestAreas,
                    onClick = onToggleRestAreas,
                    testTag = "toggle_rest_areas",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun FilterCategoryToggleItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    themeColor: Color,
    isActive: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isActive) {
        themeColor.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    }

    val borderColor = if (isActive) {
        themeColor
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
    }

    val contentColor = if (isActive) {
        themeColor
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isActive) 1.5.dp else 1.dp,
            color = borderColor
        ),
        modifier = modifier
            .height(56.dp)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (isActive) themeColor else MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(17.dp)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isActive) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        maxLines = 1
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = contentColor,
                        maxLines = 1
                    )
                }
            }

            // Small active checkmark indicator or dash
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActive) themeColor.copy(alpha = 0.15f) else Color.Transparent
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isActive) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "مفعل",
                        tint = themeColor,
                        modifier = Modifier.size(12.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.VisibilityOff,
                        contentDescription = "مخفي",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.size(11.dp)
                    )
                }
            }
        }
    }
}
