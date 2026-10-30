package com.example.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Accessible
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.HotTub
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AlgerianWilayas
import com.example.data.model.AppLanguage
import com.example.data.model.RestroomEntity
import com.example.data.model.ReviewEntity
import com.example.ui.components.HammamDetailView
import com.example.ui.components.StatusBadge
import com.example.ui.components.openExternalBrowserUrl
import com.example.ui.theme.AlertRed
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.HammamBronze
import com.example.ui.theme.HammamBronzeDark
import com.example.ui.theme.HammamBronzeLight
import com.example.ui.theme.HammamGoldAccent
import com.example.ui.theme.RahaBlue
import com.example.ui.theme.RahaGreen
import com.example.ui.theme.StarGold
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.ThermalSpringTeal
import com.example.ui.theme.WaterBlue
import com.example.ui.theme.WomenSectionViolet
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HammamProfileScreen(
    hammam: RestroomEntity,
    distanceText: String,
    reviews: List<ReviewEntity>,
    onBack: () -> Unit,
    onNavigateClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onUpdateHammamDetails: (RestroomEntity) -> Unit,
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
            .testTag("hammam_profile_screen"),
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
                    if (hammam.facebookUrl.isNotBlank()) {
                        OutlinedButton(
                            onClick = { openExternalBrowserUrl(context, hammam.facebookUrl, lang) },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.5.dp, FacebookBlue),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = FacebookBlue),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("hammam_bottom_facebook_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = when (lang) {
                                    AppLanguage.AR -> "فيسبوك الحمام"
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
                        colors = ButtonDefaults.buttonColors(containerColor = HammamBronze),
                        modifier = Modifier
                            .weight(1.3f)
                            .height(48.dp)
                            .testTag("hammam_navigate_btn")
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
            // 1. Warm Bronze & Terracotta Architectural Hero Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = if (hammam.isThermalSpring) {
                                    listOf(HammamBronzeDark, HammamBronze, ThermalSpringTeal)
                                } else {
                                    listOf(HammamBronzeDark, HammamBronze, HammamBronzeLight)
                                }
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
                                    .testTag("hammam_back_button")
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
                                    text = AppStrings.hammamPageHeaderTitle(lang),
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
                                        imageVector = if (hammam.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = AppStrings.navFavorites(lang),
                                        tint = if (hammam.isFavorite) AlertRed else Color.White
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
                                        imageVector = Icons.Default.HotTub,
                                        contentDescription = null,
                                        tint = if (hammam.isThermalSpring) ThermalSpringTeal else HammamBronze,
                                        modifier = Modifier.size(38.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = hammam.getDisplayName(lang),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        lineHeight = 28.sp
                                    ),
                                    color = Color.White
                                )
                                if (hammam.nameFr.isNotBlank() && lang == AppLanguage.AR) {
                                    Text(
                                        text = hammam.nameFr,
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
                                        tint = HammamGoldAccent,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    val locLabel = buildString {
                                        append(AlgerianWilayas.getLocalizedWilayaName(hammam.wilaya, lang))
                                        if (hammam.commune.isNotBlank()) append(" • ${hammam.commune}")
                                        if (distanceText.isNotBlank()) append(" • $distanceText")
                                    }
                                    Text(
                                        text = locLabel,
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                        color = HammamGoldAccent
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            HammamHeroPill(
                                icon = Icons.Default.HotTub,
                                text = if (hammam.isFree) AppStrings.free(lang) else "${hammam.priceDzd} ${AppStrings.dzdCurrency(lang)}",
                                iconTint = HammamGoldAccent
                            )
                            val reviewTotal = if (reviews.isNotEmpty()) reviews.size else hammam.reviewsCount
                            HammamHeroPill(
                                icon = Icons.Default.Star,
                                text = "${hammam.rating} ($reviewTotal)",
                                iconTint = StarGold
                            )
                            HammamHeroPill(
                                icon = Icons.Default.Schedule,
                                text = hammam.openingHours,
                                iconTint = Color.White
                            )
                        }
                    }
                }
            }

            // 2. Reusable HammamDetailView (Schedules, Phone, FlowRow Service Tags, Notes, Facebook)
            item {
                HammamDetailView(
                    hammam = hammam,
                    distanceText = distanceText,
                    showHeaderCard = false,
                    onEditServicesClick = { showEditServicesDialog = true },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 18.dp)
                )
            }

            // 3. Hygiene, Water Availability & Restroom Facilities Section
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
                            AppLanguage.AR -> "مستوى النظافة وتوفر المياه والمرافق الصحية"
                            AppLanguage.FR -> "Propreté, disponibilité de l'eau et sanitaires"
                            AppLanguage.EN -> "Cleanliness, Water Supply & Sanitary Facilities"
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
                                    text = if (hammam.hasWater && !hammam.waterCutReported) AppStrings.waterAvailable(lang) else AppStrings.waterCut(lang),
                                    color = if (hammam.hasWater && !hammam.waterCutReported) WaterBlue else AlertRed,
                                    icon = Icons.Default.WaterDrop
                                )
                                StatusBadge(
                                    text = "${AppStrings.cleanliness(lang)}: ${hammam.cleanlinessRating}/5",
                                    color = RahaGreen,
                                    icon = Icons.Default.CleaningServices
                                )
                                if (hammam.hasSoapPaper) {
                                    StatusBadge(
                                        text = AppStrings.filterSoapPaper(lang),
                                        color = SuccessGreen,
                                        icon = Icons.Default.CheckCircle
                                    )
                                }
                                if (hammam.hasWomenSection) {
                                    StatusBadge(
                                        text = AppStrings.womenSection(lang),
                                        color = WomenSectionViolet,
                                        icon = Icons.Default.Wc
                                    )
                                }
                                if (hammam.isAccessiblePmr) {
                                    StatusBadge(
                                        text = AppStrings.accessiblePmr(lang),
                                        color = RahaBlue,
                                        icon = Icons.AutoMirrored.Filled.Accessible
                                    )
                                }
                            }

                            if (hammam.notes.isNotBlank()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = hammam.notes,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (hammam.address.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "📍 ${hammam.address}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = TealPrimary
                                )
                            }
                        }
                    }
                }
            }

            // 4. Update / Contribute Hammam Services CTA Card
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
                        border = BorderStroke(1.dp, HammamBronze),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("edit_hammam_services_cta_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = HammamBronze,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.editHammamServicesButton(lang),
                            color = HammamBronze,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    if (showEditServicesDialog) {
        EditHammamServicesDialog(
            hammam = hammam,
            onDismiss = { showEditServicesDialog = false },
            onSave = { updatedHammam ->
                onUpdateHammamDetails(updatedHammam)
                showEditServicesDialog = false
                Toast.makeText(
                    context,
                    when (lang) {
                        AppLanguage.AR -> "تم تحديث خدمات وأوقات الحمام بنجاح ✓"
                        AppLanguage.FR -> "Services et horaires du hammam mis à jour ✓"
                        AppLanguage.EN -> "Hammam services & schedules updated ✓"
                    },
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}

@Composable
private fun HammamHeroPill(
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
fun EditHammamServicesDialog(
    hammam: RestroomEntity,
    onDismiss: () -> Unit,
    onSave: (RestroomEntity) -> Unit
) {
    val lang = LocalAppLanguage.current
    var priceText by remember { mutableStateOf(hammam.priceDzd.toString()) }
    var menHours by remember { mutableStateOf(hammam.hammamMenHours) }
    var womenHours by remember { mutableStateOf(hammam.hammamWomenHours) }
    var hammamPhone by remember { mutableStateOf(hammam.hammamPhone) }
    var facebookUrl by remember { mutableStateOf(hammam.facebookUrl) }
    var isThermalSpring by remember { mutableStateOf(hammam.isThermalSpring) }
    var hasSteamSauna by remember { mutableStateOf(hammam.hasSteamSauna) }
    var hasKessaScrub by remember { mutableStateOf(hammam.hasKessaScrub) }
    var hasPrivateShowerRooms by remember { mutableStateOf(hammam.hasPrivateShowerRooms) }
    var hasTowelRental by remember { mutableStateOf(hammam.hasTowelRental) }
    var hasTeaLounge by remember { mutableStateOf(hammam.hasTeaLounge) }
    var hasBarberGrooming by remember { mutableStateOf(hammam.hasBarberGrooming) }
    var hasWomenSection by remember { mutableStateOf(hammam.hasWomenSection) }
    var hasParking by remember { mutableStateOf(hammam.hasParking) }
    var servicesNotes by remember { mutableStateOf(hammam.hammamServicesNotes) }
    var supportsRemotePayment by remember { mutableStateOf(hammam.hasRemotePayment) }
    var remotePaymentDetails by remember { mutableStateOf(hammam.donationCcp) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("edit_hammam_services_dialog")
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
                            imageVector = Icons.Default.HotTub,
                            contentDescription = null,
                            tint = HammamBronze,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.editHammamServicesButton(lang),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = AppStrings.close(lang))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text(AppStrings.priceLabel(lang)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = menHours,
                    onValueChange = { menHours = it },
                    label = { Text(AppStrings.hammamMenHoursLabel(lang)) },
                    placeholder = { Text("06:00 - 12:30 / 18:30 - 22:00") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = womenHours,
                    onValueChange = { womenHours = it },
                    label = { Text(AppStrings.hammamWomenHoursLabel(lang)) },
                    placeholder = { Text("13:00 - 18:00") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = hammamPhone,
                    onValueChange = { hammamPhone = it },
                    label = { Text(AppStrings.hammamPhoneLabel(lang)) },
                    placeholder = { Text("021 00 00 00 / 0550...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = facebookUrl,
                    onValueChange = { facebookUrl = it },
                    label = { Text(AppStrings.hammamFacebookUrlLabel(lang)) },
                    placeholder = { Text("https://www.facebook.com/...") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hammam_facebook_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = AppStrings.hammamServicesSectionTitle(lang),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = HammamBronze
                )

                HammamCheckboxRow(checked = isThermalSpring, onCheckedChange = { isThermalSpring = it }, label = "♨️ " + AppStrings.thermalSpringBadge(lang))
                HammamCheckboxRow(checked = hasSteamSauna, onCheckedChange = { hasSteamSauna = it }, label = "🧖 " + AppStrings.steamSaunaBadge(lang))
                HammamCheckboxRow(checked = hasKessaScrub, onCheckedChange = { hasKessaScrub = it }, label = "🧼 " + AppStrings.kessaScrubBadge(lang))
                HammamCheckboxRow(checked = hasPrivateShowerRooms, onCheckedChange = { hasPrivateShowerRooms = it }, label = "🚿 " + AppStrings.privateShowerRoomsBadge(lang))
                HammamCheckboxRow(checked = hasTowelRental, onCheckedChange = { hasTowelRental = it }, label = "🧺 " + AppStrings.towelRentalBadge(lang))
                HammamCheckboxRow(checked = hasTeaLounge, onCheckedChange = { hasTeaLounge = it }, label = "🍵 " + AppStrings.teaLoungeBadge(lang))
                HammamCheckboxRow(checked = hasBarberGrooming, onCheckedChange = { hasBarberGrooming = it }, label = "💈 " + AppStrings.barberGroomingBadge(lang))
                HammamCheckboxRow(checked = hasWomenSection, onCheckedChange = { hasWomenSection = it }, label = "🧕 " + AppStrings.womenSection(lang))
                HammamCheckboxRow(checked = hasParking, onCheckedChange = { hasParking = it }, label = "🅿️ " + AppStrings.mosqueParkingBadge(lang))

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = RahaBlue.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, RahaBlue.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        HammamCheckboxRow(
                            checked = supportsRemotePayment,
                            onCheckedChange = { supportsRemotePayment = it },
                            label = "💳 " + AppStrings.remotePaymentEnableCheckbox(lang)
                        )
                        if (supportsRemotePayment) {
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = remotePaymentDetails,
                                onValueChange = { remotePaymentDetails = it },
                                label = { Text(AppStrings.remotePaymentAccountLabel(lang)) },
                                placeholder = { Text(AppStrings.remotePaymentAccountPlaceholder(lang)) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.CreditCard,
                                        contentDescription = null,
                                        tint = RahaBlue
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("edit_hammam_remote_payment_input")
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = servicesNotes,
                    onValueChange = { servicesNotes = it },
                    label = { Text(AppStrings.hammamServicesNotesLabel(lang)) },
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
                            val parsedPrice = priceText.toIntOrNull()?.coerceAtLeast(0) ?: hammam.priceDzd
                            val updatedRemotePayment = if (supportsRemotePayment) {
                                remotePaymentDetails.trim().ifBlank { AppStrings.defaultRemotePaymentMethod(lang) }
                            } else {
                                ""
                            }
                            onSave(
                                hammam.copy(
                                    isFree = parsedPrice == 0,
                                    priceDzd = parsedPrice,
                                    isThermalSpring = isThermalSpring,
                                    hasSteamSauna = hasSteamSauna,
                                    hasKessaScrub = hasKessaScrub,
                                    hasPrivateShowerRooms = hasPrivateShowerRooms,
                                    hasTowelRental = hasTowelRental,
                                    hasTeaLounge = hasTeaLounge,
                                    hasBarberGrooming = hasBarberGrooming,
                                    hasWomenSection = hasWomenSection,
                                    hasParking = hasParking,
                                    hammamMenHours = menHours.trim(),
                                    hammamWomenHours = womenHours.trim(),
                                    hammamPhone = hammamPhone.trim(),
                                    facebookUrl = facebookUrl.trim(),
                                    hammamServicesNotes = servicesNotes.trim(),
                                    donationCcp = updatedRemotePayment
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = HammamBronze),
                        modifier = Modifier.testTag("save_hammam_services_btn")
                    ) {
                        Text(AppStrings.save(lang))
                    }
                }
            }
        }
    }
}

@Composable
private fun HammamCheckboxRow(
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
