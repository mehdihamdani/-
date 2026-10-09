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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import com.example.data.model.RestroomEntity
import com.example.data.model.ReviewEntity
import com.example.ui.theme.AlertRed
import com.example.ui.theme.RahaGreen
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.WaterBlue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RestroomDetailSheet(
    restroom: RestroomEntity,
    reviews: List<ReviewEntity> = emptyList(),
    distanceText: String,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onNavigateClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onReportWaterCut: (Boolean) -> Unit,
    onRateRestroom: (Float) -> Unit = {},
    onSubmitReview: (authorName: String, rating: Float, cleanlinessRating: Float, comment: String, hasWater: Boolean, hasSoap: Boolean) -> Unit
) {
    var isWritingReview by remember { mutableStateOf(false) }
    var reviewAuthor by remember { mutableStateOf("") }
    var userOverallRating by remember { mutableFloatStateOf(5.0f) }
    var userCleanlinessRating by remember { mutableFloatStateOf(5.0f) }
    var userComment by remember { mutableStateOf("") }
    var userHasWater by remember { mutableStateOf(restroom.hasWater && !restroom.waterCutReported) }
    var userHasSoap by remember { mutableStateOf(restroom.hasSoapPaper) }
    var reviewSubmittedSuccess by remember { mutableStateOf(false) }

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

            // Cleanliness Rating & Reviews Overview Section
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("cleanliness_reviews_section")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header: Cleanliness and Overall Rating Summary
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CleaningServices,
                                    contentDescription = null,
                                    tint = RahaGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "مستوى نظافة المكان",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            Text(
                                text = when {
                                    restroom.cleanlinessRating >= 4.7f -> "معقم ونظيف جداً ✨"
                                    restroom.cleanlinessRating >= 4.0f -> "نظيف ومرتب 🧼"
                                    restroom.cleanlinessRating >= 3.0f -> "نظافة مقبولة وتفي بالغرض"
                                    else -> "يحتاج إلى صيانة وتنظيف دوري ⚠️"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = if (restroom.cleanlinessRating >= 4.0f) SuccessGreen else Color(0xFFE67E22)
                            )
                        }

                        // Cleanliness Score Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = RahaGreen.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${restroom.cleanlinessRating} / 5",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = RahaGreen
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Cleanliness Progress Indicator Bar
                    val cleanProgress = (restroom.cleanlinessRating / 5.0f).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { cleanProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (restroom.cleanlinessRating >= 4.0f) RahaGreen else Color(0xFFE67E22),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Secondary stats row: Overall rating and review count
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "التقييم العام: ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${restroom.rating} ★",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFFE67E22)
                            )
                        }
                        Text(
                            text = "${if (reviews.isNotEmpty()) reviews.size else restroom.reviewsCount} تقييم وتجربة مسجلة",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Action: Add / Write a Review Button
                    if (!isWritingReview) {
                        Button(
                            onClick = {
                                isWritingReview = true
                                reviewSubmittedSuccess = false
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("open_write_review_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.RateReview,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "شارك تجربتك وقَيّم نظافة المكان",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    // Success Feedback Banner
                    if (reviewSubmittedSuccess) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SuccessGreen.copy(alpha = 0.12f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "شكراً لمساهمتك! تم نشر تقييمك لمستوى النظافة وتجربتك بنجاح.",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = SuccessGreen
                                )
                            }
                        }
                    }

                    // Expandable Review Form
                    AnimatedVisibility(visible = isWritingReview) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface,
                                tonalElevation = 2.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "إضافة تقييم وتجربة جديدة",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Author Name Input
                                    OutlinedTextField(
                                        value = reviewAuthor,
                                        onValueChange = { reviewAuthor = it },
                                        label = { Text("اسمك أو صفتك (مثال: مسافر، كريم...)") },
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("review_author_input"),
                                        shape = RoundedCornerShape(10.dp)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Cleanliness Rating Stars (Highlighted & Specific)
                                    Text(
                                        text = "1. تقييم نظافة المكان 🧹:",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    ) {
                                        for (i in 1..5) {
                                            IconButton(
                                                onClick = { userCleanlinessRating = i.toFloat() },
                                                modifier = Modifier.size(36.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (i <= userCleanlinessRating) Icons.Default.Star else Icons.Default.StarBorder,
                                                    contentDescription = "$i نجوم في النظافة",
                                                    tint = RahaGreen
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = when (userCleanlinessRating.toInt()) {
                                                5 -> "معقم ونظيف جداً"
                                                4 -> "نظيف ومرتب"
                                                3 -> "نظافة مقبولة"
                                                2 -> "يحتاج تنظيفاً"
                                                else -> "غير نظيف"
                                            },
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = RahaGreen
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Overall Rating Stars
                                    Text(
                                        text = "2. التقييم العام للمرفق ⭐:",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    ) {
                                        for (i in 1..5) {
                                            IconButton(
                                                onClick = { userOverallRating = i.toFloat() },
                                                modifier = Modifier.size(36.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (i <= userOverallRating) Icons.Default.Star else Icons.Default.StarBorder,
                                                    contentDescription = "$i نجوم عام",
                                                    tint = Color(0xFFE67E22)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = when (userOverallRating.toInt()) {
                                                5 -> "ممتاز"
                                                4 -> "جيد جداً"
                                                3 -> "جيد"
                                                2 -> "مقبول"
                                                else -> "سيء"
                                            },
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = Color(0xFFE67E22)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Amenities Status Toggles (Water & Soap)
                                    Text(
                                        text = "حالة المرافق أثناء زيارتك:",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    ) {
                                        FilterChip(
                                            selected = userHasWater,
                                            onClick = { userHasWater = !userHasWater },
                                            label = { Text(if (userHasWater) "💧 الماء متوفر" else "⚠️ الماء منقطع") },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = WaterBlue.copy(alpha = 0.2f),
                                                selectedLabelColor = WaterBlue
                                            )
                                        )
                                        FilterChip(
                                            selected = userHasSoap,
                                            onClick = { userHasSoap = !userHasSoap },
                                            label = { Text(if (userHasSoap) "🧼 صابون متوفر" else "❌ بدون صابون") },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = RahaGreen.copy(alpha = 0.2f),
                                                selectedLabelColor = RahaGreen
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // User Experience Comment Text Field
                                    OutlinedTextField(
                                        value = userComment,
                                        onValueChange = { userComment = it },
                                        label = { Text("شارك تفاصيل تجربتك أو ملاحظاتك عن النظافة...") },
                                        minLines = 3,
                                        maxLines = 5,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("review_comment_input"),
                                        shape = RoundedCornerShape(10.dp)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Action buttons (Cancel & Submit)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = { isWritingReview = false },
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("إلغاء")
                                        }

                                        Button(
                                            onClick = {
                                                onSubmitReview(
                                                    reviewAuthor.ifBlank { "مواطن" },
                                                    userOverallRating,
                                                    userCleanlinessRating,
                                                    userComment,
                                                    userHasWater,
                                                    userHasSoap
                                                )
                                                onRateRestroom(userOverallRating)
                                                isWritingReview = false
                                                reviewSubmittedSuccess = true
                                                userComment = ""
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("submit_review_button")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Send,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("نشر التقييم")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Community Reviews & Experiences List Section
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("community_reviews_list")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "تجارب وآراء الزوار (${reviews.size})",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Icon(
                            imageVector = Icons.Default.AddComment,
                            contentDescription = null,
                            tint = TealPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (reviews.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            reviews.forEach { rev ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    tonalElevation = 1.dp,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        // Reviewer header
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(32.dp)
                                                        .clip(CircleShape)
                                                        .background(TealPrimary.copy(alpha = 0.15f)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = rev.authorName.take(1),
                                                        fontWeight = FontWeight.Bold,
                                                        color = TealPrimary,
                                                        fontSize = 14.sp
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Column {
                                                    Text(
                                                        text = rev.authorName,
                                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                                    )
                                                    Text(
                                                        text = rev.formattedTime,
                                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }

                                            // Cleanliness score pill
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = RahaGreen.copy(alpha = 0.12f)
                                            ) {
                                                Text(
                                                    text = "✨ نظافة: ${rev.cleanlinessRating}/5",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = RahaGreen,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // Stars & amenities chips
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            // Overall Stars
                                            Row {
                                                for (s in 1..5) {
                                                    Icon(
                                                        imageVector = if (s <= rev.rating) Icons.Default.Star else Icons.Default.StarBorder,
                                                        contentDescription = null,
                                                        tint = Color(0xFFE67E22),
                                                        modifier = Modifier.size(13.dp)
                                                    )
                                                }
                                            }

                                            Text(
                                                text = "•",
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontSize = 12.sp
                                            )

                                            // Water badge
                                            Text(
                                                text = if (rev.hasWaterAvailable) "💧 ماء متوفر" else "⚠️ ماء منقطع",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = if (rev.hasWaterAvailable) WaterBlue else AlertRed
                                            )

                                            if (rev.hasSoapPaper) {
                                                Text(
                                                    text = "• 🧼 صابون متوفر",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                    color = RahaGreen
                                                )
                                            }
                                        }

                                        // Comment text
                                        if (rev.comment.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = rev.comment,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // Empty reviews prompt
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RateReview,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "لا توجد تعليقات مسجلة بعد لهذا المرفق.",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "كن أول من يشارك تجربته ويقيّم مستوى النظافة لمساعدة الآخرين!",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                )
                            }
                        }
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
