package com.example.ui.components

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Accessible
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shower
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RestroomEntity
import com.example.ui.theme.AlertRed
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.WaterBlue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RestroomDetailSheet(
    restroom: RestroomEntity,
    distanceText: String,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onNavigateClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onReportWaterCut: (Boolean) -> Unit,
    onRateRestroom: (Float) -> Unit
) {
    var userRating by remember { mutableFloatStateOf(0f) }
    var ratingSubmitted by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Row: Category Badge & Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = getTypeColor(restroom.type).copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = getTypeIcon(restroom.type),
                            contentDescription = null,
                            tint = getTypeColor(restroom.type),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = restroom.type.arabicName,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = getTypeColor(restroom.type)
                        )
                    }
                }

                Row {
                    IconButton(onClick = onFavoriteToggle) {
                        Icon(
                            imageVector = if (restroom.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "المفضلة",
                            tint = if (restroom.isFavorite) AlertRed else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onShareClick) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "مشاركة",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق")
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Name in Arabic
            Text(
                text = restroom.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Name in French if available
            if (restroom.nameFr.isNotBlank()) {
                Text(
                    text = restroom.nameFr,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Location & Distance
            Text(
                text = "📍 ${restroom.wilaya} • ${restroom.commune} • يبعد $distanceText",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = TealPrimary
            )

            // Exact Coordinates & Offline Availability Banner
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF1E392A).copy(alpha = 0.08f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "الإحداثيات: ${restroom.offlineCoordinatesString}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF1E392A)
                    )
                    Text(
                        text = "💾 مخزن محلياً (Room)",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1E392A)
                    )
                }
            }

            if (restroom.address.isNotBlank()) {
                Text(
                    text = restroom.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Water cut alert banner
            if (restroom.waterCutReported) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AlertRed.copy(alpha = 0.12f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = AlertRed,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "تنبيه انقطاع المياه في هذا الموقع",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = AlertRed
                            )
                            Text(
                                text = "أبلغ مستخدمون عن عدم توفر الماء مؤقتاً. يرجى أخذ الحيطة أو التوجه لأقرب مسجد مجاور.",
                                style = MaterialTheme.typography.bodySmall,
                                color = AlertRed
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Amenities & Badges
            Text(
                text = "الخدمات والمرافق المتوفرة:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusBadge(
                    text = if (restroom.isFree) "دخول مجاني" else "سعر الدخول: ${restroom.priceDzd} دج",
                    color = if (restroom.isFree) SuccessGreen else MaterialTheme.colorScheme.tertiary
                )
                StatusBadge(
                    text = if (restroom.hasWater && !restroom.waterCutReported) "ماء جاري متوفر" else "ماء مقطوع حالياً",
                    color = if (restroom.hasWater && !restroom.waterCutReported) WaterBlue else AlertRed,
                    icon = Icons.Default.WaterDrop
                )
                if (restroom.hasSoapPaper) {
                    StatusBadge(text = "صابون ومجفف أيدي", color = SuccessGreen, icon = Icons.Default.CheckCircle)
                }
                if (restroom.hasWudu) {
                    StatusBadge(text = "أحواض وضوء مهيأة", color = TealPrimary, icon = Icons.Default.WaterDrop)
                }
                if (restroom.hasWomenSection) {
                    StatusBadge(text = "قسم مخصص للنساء", color = Color(0xFF9C27B0), icon = Icons.Default.Wc)
                }
                if (restroom.isAccessiblePmr) {
                    StatusBadge(text = "مهيأ لذوي الاحتياجات الخاصة", color = Color(0xFF0077B6), icon = Icons.AutoMirrored.Filled.Accessible)
                }
                if (restroom.hasChangingTable) {
                    StatusBadge(text = "طاولة تغيير حفاضات الأطفال", color = Color(0xFFE91E63), icon = Icons.Default.ChildCare)
                }
                if (restroom.cleanlinessRating >= 4.2f) {
                    StatusBadge(text = "مستوى نظافة ممتاز (${restroom.cleanlinessRating}★)", color = Color(0xFF2E7D32), icon = Icons.Default.CleaningServices)
                }
                if (restroom.hasShower) {
                    StatusBadge(text = "دوش واستحمام بالماء الساخن", color = Color(0xFFB07D62), icon = Icons.Default.Shower)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notes / Description
            if (restroom.notes.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "معلومات إضافية:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = restroom.notes,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Cleanliness Rating & User Review section
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "مستوى النظافة العام:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${restroom.rating} ★ (${restroom.reviewsCount} تقييم)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFE67E22)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!ratingSubmitted) {
                        Text(
                            text = "هل زرت هذا المكان مؤخراً؟ قيّم مستوى النظافة:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            for (i in 1..5) {
                                IconButton(
                                    onClick = {
                                        userRating = i.toFloat()
                                        onRateRestroom(i.toFloat())
                                        ratingSubmitted = true
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = if (i <= userRating) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = "$i نجوم",
                                        tint = Color(0xFFE67E22)
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "✓ شكراً لمساهمتك في تحديث مستوى النظافة للمجتمع!",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = SuccessGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Report Water Cut / Maintenance toggle
            OutlinedButton(
                onClick = { onReportWaterCut(!restroom.waterCutReported) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ReportProblem,
                    contentDescription = null,
                    tint = if (restroom.waterCutReported) SuccessGreen else AlertRed,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (restroom.waterCutReported) "إلغاء التنبيه (الماء عاد وتوفر)" else "إبلاغ عن انقطاع الماء أو خلل",
                    color = if (restroom.waterCutReported) SuccessGreen else AlertRed
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main CTA Navigation Button
            Button(
                onClick = onNavigateClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("sheet_navigate_button")
            ) {
                Icon(imageVector = Icons.Default.Navigation, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "فتح الاتجاهات على الخريطة",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
