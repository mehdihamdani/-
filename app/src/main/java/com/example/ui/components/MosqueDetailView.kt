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
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolunteerActivism
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
import com.example.ui.theme.ClimateCyan
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.FuneralSlate
import com.example.ui.theme.IftarAmber
import com.example.ui.theme.MosqueEmerald
import com.example.ui.theme.PersianGreen
import com.example.ui.theme.RahaBlue
import com.example.ui.theme.StarGold
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.WaterBlue
import com.example.ui.theme.WomenSectionViolet
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage

/**
 * Represents a single service or facility tag offered by a mosque.
 */
data class MosqueServiceTag(
    val id: String,
    val label: String,
    val icon: ImageVector? = null,
    val accentColor: Color? = null
)

/**
 * Decoupled UI state model for [MosqueDetailView], allowing the component
 * to be reused across screens, bottom sheets, previews, and tests.
 */
data class MosqueDetailUiModel(
    val name: String,
    val secondaryName: String = "",
    val locationSubtitle: String = "",
    val address: String = "",
    val openingHours: String = "",
    val rating: Float? = null,
    val reviewsCount: Int = 0,
    val imamOrCommittee: String = "",
    val phoneNumber: String = "",
    val services: List<MosqueServiceTag> = emptyList(),
    val servicesScheduleNotes: String = "",
    val facebookUrl: String = ""
)

/**
 * Safely normalizes and opens an external HTTP/HTTPS link (such as a mosque's Facebook page)
 * in the user's external browser via [Intent.ACTION_VIEW].
 */
fun openExternalBrowserUrl(
    context: Context,
    rawUrl: String,
    lang: AppLanguage = AppLanguage.AR
): Boolean {
    val trimmed = rawUrl.trim()
    if (trimmed.isBlank()) return false

    // Reject explicit non-web schemes (e.g., javascript:, file:, intent:) before prepending https://
    val hasExplicitScheme = trimmed.contains("://") || trimmed.startsWith("javascript:", ignoreCase = true) ||
        trimmed.startsWith("file:", ignoreCase = true) || trimmed.startsWith("data:", ignoreCase = true)

    val normalized = if (!hasExplicitScheme) {
        "https://$trimmed"
    } else {
        trimmed
    }

    return try {
        val uri = Uri.parse(normalized)
        val scheme = uri.scheme?.lowercase()
        if ((scheme == "http" || scheme == "https") && !uri.host.isNullOrBlank()) {
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                if (context !is Activity) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            context.startActivity(intent)
            true
        } else {
            Toast.makeText(context, AppStrings.invalidUrlMessage(lang), Toast.LENGTH_SHORT).show()
            false
        }
    } catch (_: Exception) {
        Toast.makeText(context, AppStrings.openUrlFailedMessage(lang), Toast.LENGTH_SHORT).show()
        false
    }
}

/**
 * Maps a [RestroomEntity] representing a mosque or musalla into a [MosqueDetailUiModel].
 */
fun RestroomEntity.toMosqueDetailUiModel(
    lang: AppLanguage,
    distanceText: String = ""
): MosqueDetailUiModel {
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
        if (hasQuranSchool) {
            add(
                MosqueServiceTag(
                    id = "quran_school",
                    label = AppStrings.quranSchoolBadge(lang),
                    icon = Icons.Default.School,
                    accentColor = MosqueEmerald
                )
            )
        }
        if (hasFridayPrayer) {
            add(
                MosqueServiceTag(
                    id = "friday_prayer",
                    label = AppStrings.fridayPrayerBadge(lang),
                    icon = Icons.Default.EventAvailable,
                    accentColor = PersianGreen
                )
            )
        }
        if (hasLibrary) {
            add(
                MosqueServiceTag(
                    id = "library",
                    label = AppStrings.mosqueLibraryBadge(lang),
                    icon = Icons.Default.LocalLibrary,
                    accentColor = RahaBlue
                )
            )
        }
        if (hasWomenSection) {
            add(
                MosqueServiceTag(
                    id = "women_section",
                    label = AppStrings.womenSection(lang),
                    icon = Icons.Default.Wc,
                    accentColor = WomenSectionViolet
                )
            )
        }
        if (hasWudu) {
            add(
                MosqueServiceTag(
                    id = "wudu",
                    label = AppStrings.wuduAvailable(lang),
                    icon = Icons.Default.WaterDrop,
                    accentColor = TealPrimary
                )
            )
        }
        if (hasWater && !waterCutReported) {
            add(
                MosqueServiceTag(
                    id = "water",
                    label = AppStrings.waterAvailable(lang),
                    icon = Icons.Default.WaterDrop,
                    accentColor = WaterBlue
                )
            )
        }
        if (isAccessiblePmr) {
            add(
                MosqueServiceTag(
                    id = "pmr",
                    label = AppStrings.accessiblePmr(lang),
                    icon = Icons.AutoMirrored.Filled.Accessible,
                    accentColor = RahaBlue
                )
            )
        }
        if (hasParking) {
            add(
                MosqueServiceTag(
                    id = "parking",
                    label = AppStrings.mosqueParkingBadge(lang),
                    icon = Icons.Default.LocalParking,
                    accentColor = WaterBlue
                )
            )
        }
        if (hasAirConditioning) {
            add(
                MosqueServiceTag(
                    id = "air_conditioning",
                    label = AppStrings.airConditioningBadge(lang),
                    icon = Icons.Default.AcUnit,
                    accentColor = ClimateCyan
                )
            )
        }
        if (hasIftarRamadan) {
            add(
                MosqueServiceTag(
                    id = "iftar_ramadan",
                    label = AppStrings.iftarRamadanBadge(lang),
                    icon = Icons.Default.Restaurant,
                    accentColor = IftarAmber
                )
            )
        }
        if (hasFuneralWash) {
            add(
                MosqueServiceTag(
                    id = "funeral_wash",
                    label = AppStrings.funeralWashBadge(lang),
                    icon = Icons.Default.VolunteerActivism,
                    accentColor = FuneralSlate
                )
            )
        }
    }

    return MosqueDetailUiModel(
        name = primaryName,
        secondaryName = secondary,
        locationSubtitle = locationLabel,
        address = address,
        openingHours = openingHours,
        rating = rating,
        reviewsCount = reviewsCount,
        imamOrCommittee = imamName,
        phoneNumber = mosquePhone,
        services = activeServices,
        servicesScheduleNotes = mosqueServicesNotes,
        facebookUrl = facebookUrl
    )
}

/**
 * Convenience overload of [MosqueDetailView] accepting a [RestroomEntity].
 */
@Composable
fun MosqueDetailView(
    mosque: RestroomEntity,
    modifier: Modifier = Modifier,
    distanceText: String = "",
    showHeaderCard: Boolean = true,
    onOpenFacebookClick: ((String) -> Unit)? = null,
    onEditServicesClick: (() -> Unit)? = null
) {
    val lang = LocalAppLanguage.current
    MosqueDetailView(
        mosqueInfo = mosque.toMosqueDetailUiModel(lang = lang, distanceText = distanceText),
        modifier = modifier,
        showHeaderCard = showHeaderCard,
        onOpenFacebookClick = onOpenFacebookClick,
        onEditServicesClick = onEditServicesClick
    )
}

/**
 * Reusable Material 3 Compose component that displays a mosque's general information,
 * a [FlowRow] of offered service tags (e.g., Quranic School, Friday Prayer, Library),
 * and an accessible button to open the mosque's Facebook link in an external browser.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MosqueDetailView(
    mosqueInfo: MosqueDetailUiModel,
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
            .testTag("mosque_detail_view"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Mosque Overview & Contact Header Card
        if (showHeaderCard) {
            MosqueHeaderInfoCard(
                mosqueInfo = mosqueInfo,
                lang = lang,
                context = context
            )
        } else if (mosqueInfo.imamOrCommittee.isNotBlank() || mosqueInfo.phoneNumber.isNotBlank()) {
            MosqueCompactCommitteeBar(
                imamOrCommittee = mosqueInfo.imamOrCommittee,
                phoneNumber = mosqueInfo.phoneNumber,
                lang = lang,
                context = context
            )
        }

        // 2. Services Section (FlowRow of styled service tags + optional schedule notes)
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("mosque_services_section")
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
                            text = AppStrings.mosqueServicesSectionTitle(lang),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (mosqueInfo.services.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = MosqueEmerald.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = AppStrings.activeServicesCountBadge(mosqueInfo.services.size, lang),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MosqueEmerald,
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
                                .testTag("mosque_edit_services_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = AppStrings.edit(lang),
                                tint = MosqueEmerald,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = AppStrings.edit(lang),
                                color = MosqueEmerald,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (mosqueInfo.services.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("mosque_services_flow_row")
                    ) {
                        mosqueInfo.services.forEach { tag ->
                            MosqueServiceTagChip(tag = tag)
                        }
                    }
                } else {
                    Text(
                        text = AppStrings.mosqueNoServicesListed(lang),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.testTag("mosque_services_empty_text")
                    )
                }

                if (mosqueInfo.servicesScheduleNotes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MosqueEmerald.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, MosqueEmerald.copy(alpha = 0.25f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("mosque_services_schedule_card")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = MosqueEmerald,
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = AppStrings.mosqueServicesScheduleLabel(lang),
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MosqueEmerald
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = mosqueInfo.servicesScheduleNotes,
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

        // 3. Official Facebook External Browser Link Section
        if (mosqueInfo.facebookUrl.isNotBlank() || onEditServicesClick != null) {
            MosqueFacebookLinkCard(
                facebookUrl = mosqueInfo.facebookUrl,
                lang = lang,
                context = context,
                onOpenFacebookClick = onOpenFacebookClick,
                onEditServicesClick = onEditServicesClick
            )
        }
    }
}

@Composable
private fun MosqueHeaderInfoCard(
    mosqueInfo: MosqueDetailUiModel,
    lang: AppLanguage,
    context: Context
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MosqueEmerald.copy(alpha = 0.28f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("mosque_info_card")
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
                    color = MosqueEmerald.copy(alpha = 0.14f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Mosque,
                            contentDescription = null,
                            tint = MosqueEmerald,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = mosqueInfo.name,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.testTag("mosque_detail_name")
                    )
                    if (mosqueInfo.secondaryName.isNotBlank()) {
                        Text(
                            text = mosqueInfo.secondaryName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (mosqueInfo.locationSubtitle.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MosqueEmerald,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = mosqueInfo.locationSubtitle,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = MosqueEmerald
                            )
                        }
                    }
                }
            }

            if (mosqueInfo.address.isNotBlank()) {
                Text(
                    text = mosqueInfo.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Metadata pills (Rating & Opening Hours)
            if (mosqueInfo.rating != null || mosqueInfo.openingHours.isNotBlank()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (mosqueInfo.rating != null) {
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
                                val countSuffix = if (mosqueInfo.reviewsCount > 0) " (${mosqueInfo.reviewsCount})" else ""
                                Text(
                                    text = "${mosqueInfo.rating}$countSuffix",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                    if (mosqueInfo.openingHours.isNotBlank()) {
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
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = MosqueEmerald,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = mosqueInfo.openingHours,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            if (mosqueInfo.imamOrCommittee.isNotBlank() || mosqueInfo.phoneNumber.isNotBlank()) {
                MosqueCompactCommitteeBar(
                    imamOrCommittee = mosqueInfo.imamOrCommittee,
                    phoneNumber = mosqueInfo.phoneNumber,
                    lang = lang,
                    context = context
                )
            }
        }
    }
}

@Composable
private fun MosqueCompactCommitteeBar(
    imamOrCommittee: String,
    phoneNumber: String,
    lang: AppLanguage,
    context: Context
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (imamOrCommittee.isNotBlank()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mosque_imam_info")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = RahaBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${AppStrings.imamNameLabel(lang).substringBefore(" (")}: ",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = imamOrCommittee,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
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
                border = BorderStroke(1.dp, MosqueEmerald.copy(alpha = 0.45f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .minimumInteractiveComponentSize()
                    .testTag("mosque_phone_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = AppStrings.mosqueCallPhoneAction(lang),
                    tint = MosqueEmerald,
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
                    color = MosqueEmerald
                )
            }
        }
    }
}

@Composable
private fun MosqueServiceTagChip(
    tag: MosqueServiceTag,
    modifier: Modifier = Modifier
) {
    val accent = tag.accentColor ?: MosqueEmerald
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = accent.copy(alpha = 0.11f),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.35f)),
        modifier = modifier.testTag("mosque_service_tag_${tag.id}")
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
private fun MosqueFacebookLinkCard(
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
            .testTag("mosque_facebook_card")
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
                        text = AppStrings.facebookPageTitle(lang),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (hasFacebook) facebookUrl else AppStrings.mosqueFacebookMissingHint(lang),
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
                            .testTag("mosque_facebook_button")
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
                            clipboard.setPrimaryClip(ClipData.newPlainText("Facebook URL", facebookUrl))
                            Toast.makeText(context, AppStrings.copied(lang), Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(FacebookBlue.copy(alpha = 0.12f))
                            .testTag("mosque_copy_facebook_button")
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
                        .testTag("mosque_add_facebook_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = FacebookBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.addFacebookLinkButton(lang),
                        color = FacebookBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
