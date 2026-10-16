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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.rememberScrollState
import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Accessible
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shower
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.core.content.ContextCompat
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import java.io.File
import java.io.FileOutputStream
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
import com.example.data.model.AppLanguage
import com.example.data.model.AlgerianWilayas
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage
import com.example.ui.theme.AlertRed
import com.example.ui.theme.MosqueEmerald
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
    onSubmitReview: (authorName: String, rating: Float, cleanlinessRating: Float, comment: String, hasWater: Boolean, hasSoap: Boolean, imageUri: String?) -> Unit
) {
    val lang = LocalAppLanguage.current
    val context = LocalContext.current
    var isWritingReview by remember { mutableStateOf(false) }
    var reviewAuthor by remember { mutableStateOf("") }
    var userOverallRating by remember { mutableFloatStateOf(5.0f) }
    var userCleanlinessRating by remember { mutableFloatStateOf(5.0f) }
    var userComment by remember { mutableStateOf("") }
    var userHasWater by remember { mutableStateOf(restroom.hasWater && !restroom.waterCutReported) }
    var userHasSoap by remember { mutableStateOf(restroom.hasSoapPaper) }
    var selectedReviewImageUri by remember { mutableStateOf<String?>(null) }
    var expandedReviewImage by remember { mutableStateOf<String?>(null) }
    var reviewSubmittedSuccess by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            try {
                val dir = File(context.filesDir, "review_photos").apply { mkdirs() }
                val file = File(dir, "review_${System.currentTimeMillis()}.jpg")
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                }
                selectedReviewImageUri = file.absolutePath
            } catch (e: Exception) {
                Toast.makeText(context, "خطأ في حفظ الصورة: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            try {
                val dir = File(context.filesDir, "review_photos").apply { mkdirs() }
                val file = File(dir, "review_${System.currentTimeMillis()}.jpg")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
                selectedReviewImageUri = file.absolutePath
            } catch (e: Exception) {
                selectedReviewImageUri = uri.toString()
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, AppStrings.cameraPermissionRequired(lang), Toast.LENGTH_LONG).show()
        }
    }

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
                            text = restroom.type.getDisplayName(lang),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = getTypeColor(restroom.type)
                        )
                    }
                }

                Row {
                    IconButton(onClick = onFavoriteToggle) {
                        Icon(
                            imageVector = if (restroom.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = AppStrings.navFavorites(lang),
                            tint = if (restroom.isFavorite) AlertRed else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onShareClick) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = AppStrings.shareFacility(lang),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = AppStrings.close(lang))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Name in selected language or fallback
            val displayName = if (lang != AppLanguage.AR && !restroom.nameFr.isNullOrBlank()) restroom.nameFr else restroom.name
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            if (lang != AppLanguage.AR && restroom.name.isNotBlank() && restroom.name != displayName) {
                Text(
                    text = restroom.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else if (lang == AppLanguage.AR && restroom.nameFr.isNotBlank()) {
                Text(
                    text = restroom.nameFr,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Location & Distance
            val distanceAwayText = when (lang) {
                AppLanguage.AR -> "يبعد $distanceText"
                AppLanguage.FR -> "À $distanceText"
                AppLanguage.EN -> "$distanceText away"
            }
            Text(
                text = "📍 ${AlgerianWilayas.getLocalizedWilayaName(restroom.wilaya, lang)} • ${restroom.commune} • $distanceAwayText",
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
                    val coordsPrefix = when (lang) {
                        AppLanguage.AR -> "الإحداثيات: "
                        AppLanguage.FR -> "Coordonnées : "
                        AppLanguage.EN -> "Coordinates: "
                    }
                    Text(
                        text = "$coordsPrefix${restroom.offlineCoordinatesString}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF1E392A)
                    )
                    Text(
                        text = AppStrings.savedLocallyRoom(lang),
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
                                text = AppStrings.waterCutAlertTitle(lang),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = AlertRed
                            )
                            Text(
                                text = AppStrings.waterCutAlertSubtitle(lang),
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
                text = AppStrings.availableAmenitiesLabel(lang),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusBadge(
                    text = if (restroom.isFree) AppStrings.freeEntry(lang) else AppStrings.entryFee(restroom.priceDzd, lang),
                    color = if (restroom.isFree) SuccessGreen else MaterialTheme.colorScheme.tertiary
                )
                StatusBadge(
                    text = if (restroom.hasWater && !restroom.waterCutReported) AppStrings.waterAvailable(lang) else AppStrings.waterCut(lang),
                    color = if (restroom.hasWater && !restroom.waterCutReported) WaterBlue else AlertRed,
                    icon = Icons.Default.WaterDrop
                )
                if (restroom.hasSoapPaper) {
                    StatusBadge(text = AppStrings.filterSoapPaper(lang), color = SuccessGreen, icon = Icons.Default.CheckCircle)
                }
                if (restroom.hasWudu) {
                    StatusBadge(text = AppStrings.wuduAvailable(lang), color = TealPrimary, icon = Icons.Default.WaterDrop)
                }
                if (restroom.hasWomenSection) {
                    StatusBadge(text = AppStrings.womenSection(lang), color = Color(0xFF9C27B0), icon = Icons.Default.Wc)
                }
                if (restroom.isAccessiblePmr) {
                    StatusBadge(text = AppStrings.accessiblePmr(lang), color = Color(0xFF0077B6), icon = Icons.AutoMirrored.Filled.Accessible)
                }
                if (restroom.hasChangingTable) {
                    StatusBadge(text = AppStrings.babyChanging(lang), color = Color(0xFFE91E63), icon = Icons.Default.ChildCare)
                }
                if (restroom.cleanlinessRating >= 4.2f) {
                    StatusBadge(text = "${AppStrings.highCleanliness(lang)} (${restroom.cleanlinessRating}★)", color = Color(0xFF2E7D32), icon = Icons.Default.CleaningServices)
                }
                if (restroom.hasShower) {
                    StatusBadge(text = AppStrings.filterShower(lang), color = Color(0xFFB07D62), icon = Icons.Default.Shower)
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
                            text = AppStrings.additionalInfo(lang),
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

            // Donation Account (CCP / BaridiMob) Card
            if (restroom.donationCcp.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MosqueEmerald.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, MosqueEmerald.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("donation_ccp_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = Icons.Default.VolunteerActivism,
                                contentDescription = null,
                                tint = MosqueEmerald,
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = AppStrings.donationCcpLabelHeader(lang),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MosqueEmerald
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = restroom.donationCcp,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Donation Account", restroom.donationCcp)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, AppStrings.copyCcpSuccess(lang), Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = AppStrings.copyCcpContentDesc(lang),
                                tint = MosqueEmerald
                            )
                        }
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
                                    text = AppStrings.cleanlinessScore(lang),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            val cleanlinessDesc = when {
                                restroom.cleanlinessRating >= 4.7f -> when (lang) {
                                    AppLanguage.AR -> "معقم ونظيف جداً ✨"
                                    AppLanguage.FR -> "Très propre et désinfecté ✨"
                                    AppLanguage.EN -> "Very clean & sanitized ✨"
                                }
                                restroom.cleanlinessRating >= 4.0f -> when (lang) {
                                    AppLanguage.AR -> "نظيف ومرتب 🧼"
                                    AppLanguage.FR -> "Propre et bien entretenu 🧼"
                                    AppLanguage.EN -> "Clean & tidy 🧼"
                                }
                                restroom.cleanlinessRating >= 3.0f -> when (lang) {
                                    AppLanguage.AR -> "نظافة مقبولة وتفي بالغرض"
                                    AppLanguage.FR -> "Propreté convenable"
                                    AppLanguage.EN -> "Acceptable cleanliness"
                                }
                                else -> when (lang) {
                                    AppLanguage.AR -> "يحتاج إلى صيانة وتنظيف دوري ⚠️"
                                    AppLanguage.FR -> "Nécessite un nettoyage ⚠️"
                                    AppLanguage.EN -> "Needs cleaning & upkeep ⚠️"
                                }
                            }
                            Text(
                                text = cleanlinessDesc,
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
                                text = "${AppStrings.overallScore(lang)}: ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${restroom.rating} ★",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFFE67E22)
                            )
                        }
                        val revCount = if (reviews.isNotEmpty()) reviews.size else restroom.reviewsCount
                        Text(
                            text = "$revCount ${AppStrings.reviewsCountSuffix(lang)}",
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
                                text = AppStrings.writeReview(lang),
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
                                val successReviewMsg = when (lang) {
                                    AppLanguage.AR -> "شكراً لمساهمتك! تم نشر تقييمك لمستوى النظافة وتجربتك بنجاح."
                                    AppLanguage.FR -> "Merci pour votre contribution ! Votre avis a été publié avec succès."
                                    AppLanguage.EN -> "Thank you! Your cleanliness rating and review have been published."
                                }
                                Text(
                                    text = successReviewMsg,
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
                                        text = AppStrings.writeReview(lang),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Author Name Input
                                    OutlinedTextField(
                                        value = reviewAuthor,
                                        onValueChange = { reviewAuthor = it },
                                        label = { Text(AppStrings.yourName(lang)) },
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("review_author_input"),
                                        shape = RoundedCornerShape(10.dp)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Cleanliness Rating Stars (Highlighted & Specific)
                                    Text(
                                        text = "1. ${AppStrings.cleanlinessScore(lang)} 🧹:",
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
                                                    contentDescription = "$i ★",
                                                    tint = RahaGreen
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        val cleanStarLabel = when (userCleanlinessRating.toInt()) {
                                            5 -> when (lang) { AppLanguage.AR -> "معقم ونظيف جداً"; AppLanguage.FR -> "Très propre"; AppLanguage.EN -> "Very clean" }
                                            4 -> when (lang) { AppLanguage.AR -> "نظيف ومرتب"; AppLanguage.FR -> "Propre"; AppLanguage.EN -> "Clean" }
                                            3 -> when (lang) { AppLanguage.AR -> "نظافة مقبولة"; AppLanguage.FR -> "Moyen"; AppLanguage.EN -> "Average" }
                                            2 -> when (lang) { AppLanguage.AR -> "يحتاج تنظيفاً"; AppLanguage.FR -> "À nettoyer"; AppLanguage.EN -> "Needs cleaning" }
                                            else -> when (lang) { AppLanguage.AR -> "غير نظيف"; AppLanguage.FR -> "Sale"; AppLanguage.EN -> "Dirty" }
                                        }
                                        Text(
                                            text = cleanStarLabel,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = RahaGreen
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Overall Rating Stars
                                    Text(
                                        text = "2. ${AppStrings.overallScore(lang)} ⭐:",
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
                                                    contentDescription = "$i ★",
                                                    tint = Color(0xFFE67E22)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        val overallLabel = when (userOverallRating.toInt()) {
                                            5 -> when (lang) { AppLanguage.AR -> "ممتاز"; AppLanguage.FR -> "Excellent"; AppLanguage.EN -> "Excellent" }
                                            4 -> when (lang) { AppLanguage.AR -> "جيد جداً"; AppLanguage.FR -> "Très bon"; AppLanguage.EN -> "Very Good" }
                                            3 -> when (lang) { AppLanguage.AR -> "جيد"; AppLanguage.FR -> "Bon"; AppLanguage.EN -> "Good" }
                                            2 -> when (lang) { AppLanguage.AR -> "مقبول"; AppLanguage.FR -> "Passable"; AppLanguage.EN -> "Fair" }
                                            else -> when (lang) { AppLanguage.AR -> "سيء"; AppLanguage.FR -> "Mauvais"; AppLanguage.EN -> "Poor" }
                                        }
                                        Text(
                                            text = overallLabel,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = Color(0xFFE67E22)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Amenities Status Toggles (Water & Soap)
                                    val statusDuringVisitLabel = when (lang) {
                                        AppLanguage.AR -> "حالة المرافق أثناء زيارتك:"
                                        AppLanguage.FR -> "État des équipements lors de votre visite :"
                                        AppLanguage.EN -> "Facility status during your visit:"
                                    }
                                    Text(
                                        text = statusDuringVisitLabel,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    ) {
                                        FilterChip(
                                            selected = userHasWater,
                                            onClick = { userHasWater = !userHasWater },
                                            label = {
                                                val waterLabel = if (userHasWater) "💧 " + AppStrings.waterAvailable(lang) else "⚠️ " + AppStrings.waterCut(lang)
                                                Text(waterLabel)
                                            },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = WaterBlue.copy(alpha = 0.2f),
                                                selectedLabelColor = WaterBlue
                                            )
                                        )
                                        FilterChip(
                                            selected = userHasSoap,
                                            onClick = { userHasSoap = !userHasSoap },
                                            label = {
                                                val soapLabel = if (userHasSoap) "🧼 " + AppStrings.filterSoapPaper(lang) else when (lang) {
                                                    AppLanguage.AR -> "❌ بدون صابون"
                                                    AppLanguage.FR -> "❌ Sans savon"
                                                    AppLanguage.EN -> "❌ No soap"
                                                }
                                                Text(soapLabel)
                                            },
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
                                        label = { Text(AppStrings.writeCommentPlaceholder(lang)) },
                                        minLines = 3,
                                        maxLines = 5,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("review_comment_input"),
                                        shape = RoundedCornerShape(10.dp)
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Photo Attachment Section (Camera / Gallery)
                                    Text(
                                        text = AppStrings.attachPhoto(lang) + " 📸:",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))

                                    if (selectedReviewImageUri == null) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            OutlinedButton(
                                                onClick = {
                                                    val hasPerm = ContextCompat.checkSelfPermission(
                                                        context,
                                                        Manifest.permission.CAMERA
                                                    ) == PackageManager.PERMISSION_GRANTED
                                                    if (hasPerm) {
                                                        cameraLauncher.launch(null)
                                                    } else {
                                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                                    }
                                                },
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .testTag("take_camera_photo_button")
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.PhotoCamera,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp),
                                                    tint = TealPrimary
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(AppStrings.takePhotoCamera(lang), fontSize = 11.sp)
                                            }

                                            OutlinedButton(
                                                onClick = {
                                                    galleryLauncher.launch(
                                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                                    )
                                                },
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .testTag("pick_gallery_photo_button")
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.PhotoLibrary,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp),
                                                    tint = RahaGreen
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(AppStrings.pickFromGallery(lang), fontSize = 11.sp)
                                            }
                                        }
                                    } else {
                                        // Preview attached image
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                            border = BorderStroke(1.dp, RahaGreen.copy(alpha = 0.5f)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(10.dp)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.CheckCircle,
                                                            contentDescription = null,
                                                            tint = RahaGreen,
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                        Text(
                                                            text = AppStrings.photoAttached(lang),
                                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                            color = RahaGreen
                                                        )
                                                    }
                                                    IconButton(
                                                        onClick = { selectedReviewImageUri = null },
                                                        modifier = Modifier.size(24.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Close,
                                                            contentDescription = "حذف الصورة",
                                                            tint = AlertRed,
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                    }
                                                }

                                                Spacer(modifier = Modifier.height(6.dp))

                                                AsyncImage(
                                                    model = selectedReviewImageUri,
                                                    contentDescription = "صورة المرفق المرفقة",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(140.dp)
                                                        .clip(RoundedCornerShape(8.dp))
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Action buttons (Cancel & Submit)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                isWritingReview = false
                                                selectedReviewImageUri = null
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(AppStrings.cancel(lang))
                                        }

                                        Button(
                                            onClick = {
                                                val defaultAuthor = when (lang) {
                                                    AppLanguage.AR -> "مواطن"
                                                    AppLanguage.FR -> "Voyageur"
                                                    AppLanguage.EN -> "Traveler"
                                                }
                                                onSubmitReview(
                                                    reviewAuthor.ifBlank { defaultAuthor },
                                                    userOverallRating,
                                                    userCleanlinessRating,
                                                    userComment,
                                                    userHasWater,
                                                    userHasSoap,
                                                    selectedReviewImageUri
                                                )
                                                onRateRestroom(userOverallRating)
                                                isWritingReview = false
                                                reviewSubmittedSuccess = true
                                                userComment = ""
                                                selectedReviewImageUri = null
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
                                            Text(AppStrings.submitReview(lang))
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
                            text = AppStrings.reviewsSection(reviews.size, lang),
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
                                                    text = "✨ ${AppStrings.cleanliness(lang)}: ${rev.cleanlinessRating}/5",
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
                                            val waterText = if (rev.hasWaterAvailable) "💧 " + AppStrings.waterAvailable(lang) else "⚠️ " + AppStrings.waterCut(lang)
                                            Text(
                                                text = waterText,
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = if (rev.hasWaterAvailable) WaterBlue else AlertRed
                                            )

                                            if (rev.hasSoapPaper) {
                                                Text(
                                                    text = "• 🧼 " + AppStrings.filterSoapPaper(lang),
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

                                        // Attached review photo
                                        if (!rev.imageUri.isNullOrBlank()) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(160.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .clickable { expandedReviewImage = rev.imageUri }
                                            ) {
                                                AsyncImage(
                                                    model = rev.imageUri,
                                                    contentDescription = "صورة المرفق المرفقة بالتقييم",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = Color.Black.copy(alpha = 0.6f),
                                                    modifier = Modifier
                                                        .align(Alignment.BottomEnd)
                                                        .padding(6.dp)
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.ZoomIn,
                                                            contentDescription = null,
                                                            tint = Color.White,
                                                            modifier = Modifier.size(12.dp)
                                                        )
                                                        Text(
                                                            text = when (lang) {
                                                                AppLanguage.AR -> "عرض الصورة"
                                                                AppLanguage.FR -> "Agrandir"
                                                                AppLanguage.EN -> "Zoom photo"
                                                            },
                                                            color = Color.White,
                                                            fontSize = 10.sp
                                                        )
                                                    }
                                                }
                                            }
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
                                    text = AppStrings.noReviewsYet(lang),
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                val beFirstDesc = when (lang) {
                                    AppLanguage.AR -> "كن أول من يشارك تجربته ويقيّم مستوى النظافة لمساعدة الآخرين!"
                                    AppLanguage.FR -> "Soyez le premier à partager votre expérience et évaluer la propreté !"
                                    AppLanguage.EN -> "Be the first to share your experience and rate the cleanliness!"
                                }
                                Text(
                                    text = beFirstDesc,
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
                    text = if (restroom.waterCutReported) AppStrings.reportWaterRestored(lang) else AppStrings.reportWaterCut(lang),
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
                    text = AppStrings.openInMaps(lang),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }

    // Fullscreen Image Preview Dialog
    if (expandedReviewImage != null) {
        Dialog(onDismissRequest = { expandedReviewImage = null }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = expandedReviewImage,
                        contentDescription = "صورة المرفق بالحجم الكامل",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 420.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { expandedReviewImage = null },
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(AppStrings.close(lang))
                    }
                }
            }
        }
    }
}
