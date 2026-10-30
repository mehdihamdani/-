package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Accessible
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AlgerianWilayas
import com.example.data.model.AppLanguage
import com.example.data.model.RestroomEntity
import com.example.data.model.ReviewEntity
import com.example.ui.components.MosqueDetailView
import com.example.ui.components.StatusBadge
import com.example.ui.components.openExternalBrowserUrl
import com.example.ui.theme.AlertRed
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.MosqueEmerald
import com.example.ui.theme.MosqueEmeraldDark
import com.example.ui.theme.MosqueEmeraldLight
import com.example.ui.theme.MosqueGoldAccent
import com.example.ui.theme.RahaBlue
import com.example.ui.theme.RahaGreen
import com.example.ui.theme.RahaGreenLight
import com.example.ui.theme.StarGold
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.WaterBlue
import com.example.ui.theme.WomenSectionViolet
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage

/**
 * Safely opens an external web URL (such as the Mosque's Facebook page)
 * enforcing http/https schemes only.
 */
fun openSafeExternalUrl(context: Context, rawUrl: String) {
    openExternalBrowserUrl(context = context, rawUrl = rawUrl)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MosqueProfileScreen(
    mosque: RestroomEntity,
    distanceText: String,
    reviews: List<ReviewEntity>,
    onBack: () -> Unit,
    onNavigateClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onUpdateMosqueDetails: (RestroomEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    val context = LocalContext.current
    var showEditServicesDialog by remember { mutableStateOf(false) }

    BackHandler {
        onBack()
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("mosque_profile_screen"),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (mosque.facebookUrl.isNotBlank()) {
                        OutlinedButton(
                            onClick = { openExternalBrowserUrl(context, mosque.facebookUrl, lang) },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.5.dp, FacebookBlue),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = FacebookBlue),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("mosque_bottom_facebook_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = when (lang) {
                                    AppLanguage.AR -> "فيسبوك المسجد"
                                    AppLanguage.FR -> "Facebook"
                                    AppLanguage.EN -> "Facebook"
                                },
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Button(
                        onClick = onNavigateClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MosqueEmerald),
                        modifier = Modifier
                            .weight(1.3f)
                            .height(48.dp)
                            .testTag("mosque_navigate_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Navigation,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.openInMaps(lang),
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 28.dp)
        ) {
            // 1. Rich Emerald & Gold-accented Islamic Architectural Hero Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MosqueEmeraldDark,
                                    MosqueEmerald,
                                    MosqueEmeraldLight
                                )
                            )
                        )
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(bottom = 24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .widthIn(max = 600.dp)
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.16f))
                                    .testTag("mosque_back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = AppStrings.back(lang),
                                    tint = Color.White
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White.copy(alpha = 0.18f)
                            ) {
                                Text(
                                    text = AppStrings.mosquePageHeaderTitle(lang),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                IconButton(
                                    onClick = onFavoriteToggle,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.16f))
                                ) {
                                    Icon(
                                        imageVector = if (mosque.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = AppStrings.navFavorites(lang),
                                        tint = if (mosque.isFavorite) AlertRed else Color.White
                                    )
                                }
                                IconButton(
                                    onClick = onShareClick,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.16f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = AppStrings.shareFacility(lang),
                                        tint = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = Color.White,
                                shadowElevation = 6.dp,
                                modifier = Modifier.size(68.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Mosque,
                                        contentDescription = null,
                                        tint = MosqueEmerald,
                                        modifier = Modifier.size(38.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = mosque.getDisplayName(lang),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        lineHeight = 28.sp
                                    ),
                                    color = Color.White
                                )
                                if (mosque.nameFr.isNotBlank() && lang == AppLanguage.AR) {
                                    Text(
                                        text = mosque.nameFr,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = MosqueGoldAccent,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    val locLabel = buildString {
                                        append(AlgerianWilayas.getLocalizedWilayaName(mosque.wilaya, lang))
                                        if (mosque.commune.isNotBlank()) append(" • ${mosque.commune}")
                                        if (distanceText.isNotBlank()) append(" • $distanceText")
                                    }
                                    Text(
                                        text = locLabel,
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                        color = MosqueGoldAccent
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val reviewTotal = if (reviews.isNotEmpty()) reviews.size else mosque.reviewsCount
                            HeroPill(
                                icon = Icons.Default.Star,
                                text = "${mosque.rating} ($reviewTotal)",
                                iconTint = StarGold
                            )
                            HeroPill(
                                icon = Icons.Default.Schedule,
                                text = mosque.openingHours,
                                iconTint = Color.White
                            )
                            if (mosque.hasQuranSchool) {
                                HeroPill(
                                    icon = Icons.Default.School,
                                    text = AppStrings.quranSchoolBadge(lang),
                                    iconTint = RahaGreenLight
                                )
                            }
                        }
                    }
                }
            }

            // 2. Reusable MosqueDetailView (Committee/Phone, FlowRow of Services Tags, Schedule, and Facebook External Link)
            item {
                MosqueDetailView(
                    mosque = mosque,
                    distanceText = distanceText,
                    showHeaderCard = false,
                    onEditServicesClick = { showEditServicesDialog = true },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 18.dp)
                )
            }

            // 3. Ablution (Wudu) & Restroom Facilities Section
            item {
                Column(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 20.dp)
                ) {
                    Text(
                        text = when (lang) {
                            AppLanguage.AR -> "مرافق الوضوء ودورات المياه بالمسجد"
                            AppLanguage.FR -> "Espaces d'ablutions et sanitaires"
                            AppLanguage.EN -> "Ablution (Wudu) & Restroom Facilities"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                StatusBadge(
                                    text = if (mosque.hasWater && !mosque.waterCutReported) AppStrings.waterAvailable(lang) else AppStrings.waterCut(lang),
                                    color = if (mosque.hasWater && !mosque.waterCutReported) WaterBlue else AlertRed,
                                    icon = Icons.Default.WaterDrop
                                )
                                if (mosque.hasWudu) {
                                    StatusBadge(
                                        text = AppStrings.wuduAvailable(lang),
                                        color = MosqueEmerald,
                                        icon = Icons.Default.WaterDrop
                                    )
                                }
                                if (mosque.hasWomenSection) {
                                    StatusBadge(
                                        text = AppStrings.womenSection(lang),
                                        color = WomenSectionViolet,
                                        icon = Icons.Default.Wc
                                    )
                                }
                                if (mosque.isAccessiblePmr) {
                                    StatusBadge(
                                        text = AppStrings.accessiblePmr(lang),
                                        color = RahaBlue,
                                        icon = Icons.AutoMirrored.Filled.Accessible
                                    )
                                }
                                if (mosque.hasSoapPaper) {
                                    StatusBadge(
                                        text = AppStrings.filterSoapPaper(lang),
                                        color = SuccessGreen,
                                        icon = Icons.Default.CheckCircle
                                    )
                                }
                                StatusBadge(
                                    text = "${AppStrings.cleanliness(lang)}: ${mosque.cleanlinessRating}/5",
                                    color = RahaGreen,
                                    icon = Icons.Default.CleaningServices
                                )
                            }

                            if (mosque.notes.isNotBlank()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = mosque.notes,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (mosque.address.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "📍 ${mosque.address}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = TealPrimary
                                )
                            }
                        }
                    }
                }
            }

            // 4. Sadaqah Jariyah / Mosque CCP Account (if available)
            if (mosque.donationCcp.isNotBlank()) {
                item {
                    Column(
                        modifier = Modifier
                            .widthIn(max = 600.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 20.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MosqueEmerald.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, MosqueEmerald.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolunteerActivism,
                                        contentDescription = null,
                                        tint = MosqueEmerald,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = AppStrings.donationCcpLabelHeader(lang),
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MosqueEmerald
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = mosque.donationCcp,
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Mosque CCP", mosque.donationCcp)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, AppStrings.copyCcpSuccess(lang), Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = AppStrings.copy(lang),
                                        tint = MosqueEmerald
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5. Update / Contribute Mosque Info CTA Card
            item {
                Column(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 20.dp)
                ) {
                    OutlinedButton(
                        onClick = { showEditServicesDialog = true },
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, MosqueEmerald),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("edit_mosque_services_cta_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MosqueEmerald,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.editMosqueServicesButton(lang),
                            color = MosqueEmerald,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    if (showEditServicesDialog) {
        EditMosqueServicesDialog(
            mosque = mosque,
            onDismiss = { showEditServicesDialog = false },
            onSave = { updatedMosque ->
                onUpdateMosqueDetails(updatedMosque)
                showEditServicesDialog = false
                Toast.makeText(
                    context,
                    when (lang) {
                        AppLanguage.AR -> "تم تحديث خدمات المسجد بنجاح ✓"
                        AppLanguage.FR -> "Services de la mosquée mis à jour ✓"
                        AppLanguage.EN -> "Mosque services updated ✓"
                    },
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}

@Composable
private fun HeroPill(
    icon: ImageVector,
    text: String,
    iconTint: Color
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.16f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun EditMosqueServicesDialog(
    mosque: RestroomEntity,
    onDismiss: () -> Unit,
    onSave: (RestroomEntity) -> Unit
) {
    val lang = LocalAppLanguage.current
    var hasQuranSchool by remember { mutableStateOf(mosque.hasQuranSchool) }
    var hasFridayPrayer by remember { mutableStateOf(mosque.hasFridayPrayer) }
    var hasLibrary by remember { mutableStateOf(mosque.hasLibrary) }
    var hasParking by remember { mutableStateOf(mosque.hasParking) }
    var hasAirConditioning by remember { mutableStateOf(mosque.hasAirConditioning) }
    var hasFuneralWash by remember { mutableStateOf(mosque.hasFuneralWash) }
    var hasIftarRamadan by remember { mutableStateOf(mosque.hasIftarRamadan) }
    var facebookUrl by remember { mutableStateOf(mosque.facebookUrl) }
    var mosquePhone by remember { mutableStateOf(mosque.mosquePhone) }
    var imamName by remember { mutableStateOf(mosque.imamName) }
    var mosqueServicesNotes by remember { mutableStateOf(mosque.mosqueServicesNotes) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("edit_mosque_services_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = Icons.Default.Mosque,
                            contentDescription = null,
                            tint = MosqueEmerald,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.editMosqueServicesButton(lang),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = AppStrings.close(lang))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = facebookUrl,
                    onValueChange = { facebookUrl = it },
                    label = { Text(AppStrings.facebookUrlLabel(lang)) },
                    placeholder = { Text("https://www.facebook.com/...") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("mosque_facebook_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = mosquePhone,
                    onValueChange = { mosquePhone = it },
                    label = { Text(AppStrings.mosquePhoneLabel(lang)) },
                    placeholder = { Text("021 00 00 00 / 0550...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = imamName,
                    onValueChange = { imamName = it },
                    label = { Text(AppStrings.imamNameLabel(lang)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = AppStrings.mosqueServicesSectionTitle(lang),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MosqueEmerald
                )

                ServiceCheckboxRow(
                    checked = hasQuranSchool,
                    onCheckedChange = { hasQuranSchool = it },
                    label = "📖 " + AppStrings.quranSchoolBadge(lang)
                )
                ServiceCheckboxRow(
                    checked = hasFridayPrayer,
                    onCheckedChange = { hasFridayPrayer = it },
                    label = "🕌 " + AppStrings.fridayPrayerBadge(lang)
                )
                ServiceCheckboxRow(
                    checked = hasLibrary,
                    onCheckedChange = { hasLibrary = it },
                    label = "📚 " + AppStrings.mosqueLibraryBadge(lang)
                )
                ServiceCheckboxRow(
                    checked = hasParking,
                    onCheckedChange = { hasParking = it },
                    label = "🅿️ " + AppStrings.mosqueParkingBadge(lang)
                )
                ServiceCheckboxRow(
                    checked = hasAirConditioning,
                    onCheckedChange = { hasAirConditioning = it },
                    label = "❄️ " + AppStrings.airConditioningBadge(lang)
                )
                ServiceCheckboxRow(
                    checked = hasIftarRamadan,
                    onCheckedChange = { hasIftarRamadan = it },
                    label = "🌙 " + AppStrings.iftarRamadanBadge(lang)
                )
                ServiceCheckboxRow(
                    checked = hasFuneralWash,
                    onCheckedChange = { hasFuneralWash = it },
                    label = "🤲 " + AppStrings.funeralWashBadge(lang)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = mosqueServicesNotes,
                    onValueChange = { mosqueServicesNotes = it },
                    label = { Text(AppStrings.mosqueServicesScheduleLabel(lang)) },
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(AppStrings.cancel(lang))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSave(
                                mosque.copy(
                                    hasQuranSchool = hasQuranSchool,
                                    hasFridayPrayer = hasFridayPrayer,
                                    hasLibrary = hasLibrary,
                                    hasParking = hasParking,
                                    hasAirConditioning = hasAirConditioning,
                                    hasFuneralWash = hasFuneralWash,
                                    hasIftarRamadan = hasIftarRamadan,
                                    facebookUrl = facebookUrl.trim(),
                                    mosquePhone = mosquePhone.trim(),
                                    imamName = imamName.trim(),
                                    mosqueServicesNotes = mosqueServicesNotes.trim()
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MosqueEmerald),
                        modifier = Modifier.testTag("save_mosque_services_btn")
                    ) {
                        Text(AppStrings.save(lang))
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceCheckboxRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}
