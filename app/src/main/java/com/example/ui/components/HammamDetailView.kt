package com.example.ui.components

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Accessible
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HotTub
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shower
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AlgerianWilayas
import com.example.data.model.AppLanguage
import com.example.data.model.RestroomEntity
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.FuneralSlate
import com.example.ui.theme.HammamBronze
import com.example.ui.theme.IftarAmber
import com.example.ui.theme.MosqueEmerald
import com.example.ui.theme.PersianGreen
import com.example.ui.theme.RahaBlue
import com.example.ui.theme.StarGold
import com.example.ui.theme.SteamAmber
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.ThermalSpringTeal
import com.example.ui.theme.WaterBlue
import com.example.ui.theme.WomenSectionViolet
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage

/**
 * Represents a single service or wellness amenity tag offered by a Hammam.
 */
data class HammamServiceTag(
    val id: String,
    val label: String,
    val icon: ImageVector? = null,
    val accentColor: Color? = null
)

/**
 * Decoupled UI state model for [HammamDetailView].
 */
data class HammamDetailUiModel(
    val name: String,
    val secondaryName: String = "",
    val locationSubtitle: String = "",
    val address: String = "",
    val openingHours: String = "",
    val isFree: Boolean = false,
    val priceDzd: Int = 0,
    val isThermalSpring: Boolean = false,
    val rating: Float? = null,
    val reviewsCount: Int = 0,
    val menHours: String = "",
    val womenHours: String = "",
    val phoneNumber: String = "",
    val services: List<HammamServiceTag> = emptyList(),
    val servicesNotes: String = "",
    val facebookUrl: String = ""
)

/**
 * Maps a [RestroomEntity] representing a Hammam or thermal bath into a [HammamDetailUiModel].
 */
fun RestroomEntity.toHammamDetailUiModel(
    lang: AppLanguage,
    distanceText: String = ""
): HammamDetailUiModel {
    val primaryName = getDisplayName(lang)
    val secondary = when {
        lang == AppLanguage.AR && nameFr.isNotBlank() -> nameFr
        lang != AppLanguage.AR && name.isNotBlank() && name != primaryName -> name
        else -> ""
    }
    val locationLabel = buildString {
        append(AlgerianWilayas.getLocalizedWilayaName(wilaya, lang))
        if (commune.isNotBlank()) append(" • $commune")
        if (distanceText.isNotBlank()) append(" • $distanceText")
    }

    val activeServices = buildList {
        if (isThermalSpring) {
            add(
                HammamServiceTag(
                    id = "thermal_spring",
                    label = AppStrings.thermalSpringBadge(lang),
                    icon = Icons.Default.HotTub,
                    accentColor = ThermalSpringTeal
                )
            )
        }
        if (hasSteamSauna) {
            add(
                HammamServiceTag(
                    id = "steam_sauna",
                    label = AppStrings.steamSaunaBadge(lang),
                    icon = Icons.Default.HotTub,
                    accentColor = SteamAmber
                )
            )
        }
        if (hasKessaScrub) {
            add(
                HammamServiceTag(
                    id = "kessa_scrub",
                    label = AppStrings.kessaScrubBadge(lang),
                    icon = Icons.Default.Spa,
                    accentColor = HammamBronze
                )
            )
        }
        if (hasPrivateShowerRooms || hasShower) {
            add(
                HammamServiceTag(
                    id = "private_showers",
                    label = AppStrings.privateShowerRoomsBadge(lang),
                    icon = Icons.Default.Shower,
                    accentColor = RahaBlue
                )
            )
        }
        if (hasTowelRental) {
            add(
                HammamServiceTag(
                    id = "towel_rental",
                    label = AppStrings.towelRentalBadge(lang),
                    icon = Icons.Default.Checkroom,
                    accentColor = PersianGreen
                )
            )
        }
        if (hasTeaLounge) {
            add(
                HammamServiceTag(
                    id = "tea_lounge",
                    label = AppStrings.teaLoungeBadge(lang),
                    icon = Icons.Default.LocalCafe,
                    accentColor = IftarAmber
                )
            )
        }
        if (hasBarberGrooming) {
            add(
                HammamServiceTag(
                    id = "barber_grooming",
                    label = AppStrings.barberGroomingBadge(lang),
                    icon = Icons.Default.ContentCut,
                    accentColor = FuneralSlate
                )
            )
        }
        if (hasWomenSection) {
            add(
                HammamServiceTag(
                    id = "women_section",
                    label = AppStrings.womenSection(lang),
                    icon = Icons.Default.Wc,
                    accentColor = WomenSectionViolet
                )
            )
        }
        if (hasWudu) {
            add(
                HammamServiceTag(
                    id = "wudu",
                    label = AppStrings.wuduAvailable(lang),
                    icon = Icons.Default.WaterDrop,
                    accentColor = MosqueEmerald
                )
            )
        }
        if (hasParking) {
            add(
                HammamServiceTag(
                    id = "parking",
                    label = AppStrings.mosqueParkingBadge(lang),
                    icon = Icons.Default.LocalParking,
                    accentColor = WaterBlue
                )
            )
        }
        if (isAccessiblePmr) {
            add(
                HammamServiceTag(
                    id = "pmr",
                    label = AppStrings.accessiblePmr(lang),
                    icon = Icons.AutoMirrored.Filled.Accessible,
                    accentColor = RahaBlue
                )
            )
        }
    }

    return HammamDetailUiModel(
        name = primaryName,
        secondaryName = secondary,
        locationSubtitle = locationLabel,
        address = address,
        openingHours = openingHours,
        isFree = isFree,
        priceDzd = priceDzd,
        isThermalSpring = isThermalSpring,
        rating = rating,
        reviewsCount = reviewsCount,
        menHours = hammamMenHours,
        womenHours = hammamWomenHours,
        phoneNumber = hammamPhone,
        services = activeServices,
        servicesNotes = hammamServicesNotes,
        facebookUrl = facebookUrl
    )
}

@Composable
fun HammamDetailView(
    hammam: RestroomEntity,
    modifier: Modifier = Modifier,
    distanceText: String = "",
    showHeaderCard: Boolean = true,
    onOpenFacebookClick: ((String) -> Unit)? = null,
    onEditServicesClick: (() -> Unit)? = null
) {
    val lang = LocalAppLanguage.current
    HammamDetailView(
        hammamInfo = hammam.toHammamDetailUiModel(lang = lang, distanceText = distanceText),
        modifier = modifier,
        showHeaderCard = showHeaderCard,
        onOpenFacebookClick = onOpenFacebookClick,
        onEditServicesClick = onEditServicesClick
    )
}

/**
 * Reusable Material 3 Compose component that displays a Hammam's general information,
 * dedicated Men & Women schedules, booking phone, a [FlowRow] of offered Hammam services,
 * pricing/packages notes, and an external browser button for its Facebook page.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HammamDetailView(
    hammamInfo: HammamDetailUiModel,
    modifier: Modifier = Modifier,
    showHeaderCard: Boolean = true,
    onOpenFacebookClick: ((String) -> Unit)? = null,
    onEditServicesClick: (() -> Unit)? = null
) {
    val lang = LocalAppLanguage.current
    val context = LocalContext.current

    Column(
        modifier = modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .testTag("hammam_detail_view"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (showHeaderCard) {
            HammamHeaderInfoCard(hammamInfo = hammamInfo, lang = lang)
        }

        // Men & Women Schedules + Phone Booking Card
        if (hammamInfo.menHours.isNotBlank() || hammamInfo.womenHours.isNotBlank() || hammamInfo.phoneNumber.isNotBlank()) {
            HammamSchedulesAndContactCard(
                menHours = hammamInfo.menHours,
                womenHours = hammamInfo.womenHours,
                phoneNumber = hammamInfo.phoneNumber,
                lang = lang,
                context = context
            )
        }

        // Services Section (FlowRow of service tags + packages/pricing notes)
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, HammamBronze.copy(alpha = 0.3f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("hammam_services_section")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = AppStrings.hammamServicesSectionTitle(lang),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (hammamInfo.services.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = HammamBronze.copy(alpha = 0.14f)
                            ) {
                                Text(
                                    text = AppStrings.activeServicesCountBadge(hammamInfo.services.size, lang),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = HammamBronze,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    if (onEditServicesClick != null) {
                        TextButton(
                            onClick = onEditServicesClick,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("hammam_edit_services_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = AppStrings.edit(lang),
                                tint = HammamBronze,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = AppStrings.edit(lang),
                                color = HammamBronze,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (hammamInfo.services.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("hammam_services_flow_row")
                    ) {
                        hammamInfo.services.forEach { tag ->
                            HammamServiceTagChip(tag = tag)
                        }
                    }
                } else {
                    Text(
                        text = AppStrings.hammamNoServicesListed(lang),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.testTag("hammam_services_empty_text")
                    )
                }

                if (hammamInfo.servicesNotes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = HammamBronze.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, HammamBronze.copy(alpha = 0.28f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("hammam_services_notes_card")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = HammamBronze,
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = AppStrings.hammamServicesNotesLabel(lang),
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = HammamBronze
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = hammamInfo.servicesNotes,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Official Facebook External Browser Link Section
        if (hammamInfo.facebookUrl.isNotBlank() || onEditServicesClick != null) {
            HammamFacebookLinkCard(
                facebookUrl = hammamInfo.facebookUrl,
                lang = lang,
                context = context,
                onOpenFacebookClick = onOpenFacebookClick,
                onEditServicesClick = onEditServicesClick
            )
        }
    }
}

@Composable
private fun HammamHeaderInfoCard(
    hammamInfo: HammamDetailUiModel,
    lang: AppLanguage
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, HammamBronze.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hammam_info_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = HammamBronze.copy(alpha = 0.14f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.HotTub,
                            contentDescription = null,
                            tint = HammamBronze,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = hammamInfo.name,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.testTag("hammam_detail_name")
                    )
                    if (hammamInfo.secondaryName.isNotBlank()) {
                        Text(
                            text = hammamInfo.secondaryName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (hammamInfo.locationSubtitle.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = HammamBronze,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = hammamInfo.locationSubtitle,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = HammamBronze
                            )
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val priceColor = if (hammamInfo.isFree) SuccessGreen else HammamBronze
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = priceColor.copy(alpha = 0.14f)
                ) {
                    Text(
                        text = if (hammamInfo.isFree) AppStrings.freeEntry(lang)
                        else AppStrings.entryFee(hammamInfo.priceDzd, lang),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = priceColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
                if (hammamInfo.rating != null) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = StarGold,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "${hammamInfo.rating} (${hammamInfo.reviewsCount})",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HammamSchedulesAndContactCard(
    menHours: String,
    womenHours: String,
    phoneNumber: String,
    lang: AppLanguage,
    context: Context
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hammam_schedules_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (menHours.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = RahaBlue.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, RahaBlue.copy(alpha = 0.25f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hammam_men_hours_card")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = RahaBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AppStrings.hammamMenHoursLabel(lang),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = RahaBlue
                            )
                            Text(
                                text = menHours,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            if (womenHours.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = WomenSectionViolet.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, WomenSectionViolet.copy(alpha = 0.25f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hammam_women_hours_card")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = WomenSectionViolet,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AppStrings.hammamWomenHoursLabel(lang),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = WomenSectionViolet
                            )
                            Text(
                                text = womenHours,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            if (phoneNumber.isNotBlank()) {
                OutlinedButton(
                    onClick = {
                        try {
                            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")).apply {
                                if (context !is Activity) {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                            }
                            context.startActivity(dialIntent)
                        } catch (_: Exception) {
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, HammamBronze.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .minimumInteractiveComponentSize()
                        .testTag("hammam_phone_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = AppStrings.mosqueCallPhoneAction(lang),
                        tint = HammamBronze,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = phoneNumber,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = AppStrings.mosqueCallPhoneAction(lang),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = HammamBronze
                    )
                }
            }
        }
    }
}

@Composable
private fun HammamServiceTagChip(
    tag: HammamServiceTag,
    modifier: Modifier = Modifier
) {
    val accent = tag.accentColor ?: HammamBronze
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = accent.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.35f)),
        modifier = modifier.testTag("hammam_service_tag_${tag.id}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (tag.icon != null) {
                Icon(
                    imageVector = tag.icon,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = tag.label,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = accent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun HammamFacebookLinkCard(
    facebookUrl: String,
    lang: AppLanguage,
    context: Context,
    onOpenFacebookClick: ((String) -> Unit)?,
    onEditServicesClick: (() -> Unit)?
) {
    val hasFacebook = facebookUrl.isNotBlank()

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (hasFacebook) FacebookBlue.copy(alpha = 0.08f)
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (hasFacebook) FacebookBlue.copy(alpha = 0.35f)
            else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hammam_facebook_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = CircleShape,
                    color = FacebookBlue,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "f",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = AppStrings.hammamFacebookTitle(lang),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (hasFacebook) facebookUrl else AppStrings.hammamFacebookMissingHint(lang),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (hasFacebook) FacebookBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = if (hasFacebook) 1 else 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (hasFacebook) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (onOpenFacebookClick != null) {
                                onOpenFacebookClick(facebookUrl)
                            } else {
                                openExternalBrowserUrl(context, facebookUrl, lang)
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FacebookBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("hammam_facebook_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.openFacebookPage(lang),
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = AppStrings.openFacebookPage(lang),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Hammam Facebook URL", facebookUrl))
                            Toast.makeText(context, AppStrings.copied(lang), Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(FacebookBlue.copy(alpha = 0.12f))
                            .testTag("hammam_copy_facebook_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = AppStrings.copy(lang),
                            tint = FacebookBlue
                        )
                    }
                }
            } else if (onEditServicesClick != null) {
                OutlinedButton(
                    onClick = onEditServicesClick,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, FacebookBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .minimumInteractiveComponentSize()
                        .testTag("hammam_add_facebook_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = FacebookBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.addHammamFacebookButton(lang),
                        color = FacebookBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
