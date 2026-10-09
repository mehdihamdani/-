package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.Accessible
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.HotTub
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Shower
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RestroomEntity
import com.example.data.model.RestroomType
import com.example.ui.theme.AlertRed
import com.example.ui.theme.HammamBronze
import com.example.ui.theme.MallViolet
import com.example.ui.theme.MosqueEmerald
import com.example.ui.theme.NaftalFlame
import com.example.ui.theme.RahaGreen
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TransportBlue
import com.example.ui.theme.WaterBlue
import com.example.ui.viewmodel.RestroomWithDistance

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RestroomCard(
    item: RestroomWithDistance,
    onClick: () -> Unit,
    onNavigateClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val restroom = item.restroom
    val typeColor = getTypeColor(restroom.type)
    val typeIcon = getTypeIcon(restroom.type)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("restroom_card_${restroom.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Category Icon & Name + Favorite + Distance badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Avatar
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(typeColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = typeIcon,
                        contentDescription = restroom.type.arabicName,
                        tint = typeColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title and Wilaya
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = restroom.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${restroom.wilaya} • ${restroom.commune}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Distance pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(start = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.DirectionsWalk,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = item.formattedDistance,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (restroom.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "المفضلة",
                        tint = if (restroom.isFavorite) AlertRed else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Water cut alert banner if reported
            if (restroom.waterCutReported) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AlertRed.copy(alpha = 0.12f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = AlertRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "تنبيه مجتمعي: تم الإبلاغ عن انقطاع مؤقت للمياه في هذا الموقع",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AlertRed
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Amenities & Status Chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Free or Paid badge
                StatusBadge(
                    text = if (restroom.isFree) "مجاني" else "${restroom.priceDzd} دج",
                    color = if (restroom.isFree) SuccessGreen else MaterialTheme.colorScheme.tertiary
                )

                // Rating badge
                StatusBadge(
                    text = "${restroom.rating} ★ (${restroom.reviewsCount})",
                    color = Color(0xFFE67E22),
                    icon = Icons.Default.Star
                )

                // Cleanliness badge
                StatusBadge(
                    text = "نظافة: ${restroom.cleanlinessRating}/5",
                    color = RahaGreen,
                    icon = Icons.Default.CleaningServices
                )

                // Water status
                if (!restroom.waterCutReported && restroom.hasWater) {
                    StatusBadge(
                        text = "ماء متوفر",
                        color = WaterBlue,
                        icon = Icons.Default.WaterDrop
                    )
                }

                // Wudu
                if (restroom.hasWudu) {
                    StatusBadge(
                        text = "مرافق وضوء",
                        color = MosqueEmerald,
                        icon = Icons.Default.WaterDrop
                    )
                }

                // Women section
                if (restroom.hasWomenSection) {
                    StatusBadge(
                        text = "قسم نساء",
                        color = Color(0xFF9C27B0),
                        icon = Icons.Default.Wc
                    )
                }

                // Wheelchair
                if (restroom.isAccessiblePmr) {
                    StatusBadge(
                        text = "ذوي الاحتياجات",
                        color = TransportBlue,
                        icon = Icons.AutoMirrored.Filled.Accessible
                    )
                }

                // Changing Table
                if (restroom.hasChangingTable) {
                    StatusBadge(
                        text = "طاولة رضع",
                        color = Color(0xFFE91E63),
                        icon = Icons.Default.ChildCare
                    )
                }

                // High Cleanliness
                if (restroom.cleanlinessRating >= 4.2f) {
                    StatusBadge(
                        text = "نظافة عالية",
                        color = Color(0xFF2E7D32),
                        icon = Icons.Default.CleaningServices
                    )
                }

                // Shower
                if (restroom.hasShower) {
                    StatusBadge(
                        text = "دوش واستحمام",
                        color = HammamBronze,
                        icon = Icons.Default.Shower
                    )
                }

                // Offline Available Room badge
                StatusBadge(
                    text = "مخزن محلياً",
                    color = Color(0xFF1E392A),
                    icon = Icons.Default.CloudDone
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer: Hours, Coordinates and Navigate Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f, fill = false)) {
                    Text(
                        text = "⏰ ${restroom.openingHours}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = restroom.offlineCoordinatesString,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }

                Button(
                    onClick = onNavigateClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TealPrimary
                    ),
                    modifier = Modifier.testTag("navigate_button_${restroom.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("انطلق الآن", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
fun StatusBadge(
    text: String,
    color: Color,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.12f),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(12.dp)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = color,
                fontSize = 11.sp
            )
        }
    }
}

fun getTypeColor(type: RestroomType): Color {
    return when (type) {
        RestroomType.MOSQUE -> MosqueEmerald
        RestroomType.HAMMAM -> HammamBronze
        RestroomType.NAFTAL_HIGHWAY -> NaftalFlame
        RestroomType.TRANSPORT_HUB -> TransportBlue
        RestroomType.MALL -> MallViolet
        RestroomType.PUBLIC_MUNICIPAL -> TealPrimary
        RestroomType.PRIVATE_COMMERCIAL -> Color(0xFFD97706)
    }
}

fun getTypeIcon(type: RestroomType): ImageVector {
    return when (type) {
        RestroomType.MOSQUE -> Icons.Default.Mosque
        RestroomType.HAMMAM -> Icons.Default.HotTub
        RestroomType.NAFTAL_HIGHWAY -> Icons.Default.LocalGasStation
        RestroomType.TRANSPORT_HUB -> Icons.Default.Train
        RestroomType.MALL -> Icons.Default.ShoppingBag
        RestroomType.PUBLIC_MUNICIPAL -> Icons.Default.Wc
        RestroomType.PRIVATE_COMMERCIAL -> Icons.Default.Wc
    }
}
