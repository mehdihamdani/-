package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AlgerianRegion
import com.example.data.model.AlgerianRegions
import com.example.data.model.AppLanguage
import com.example.data.model.OfflineCacheMetadataEntity
import com.example.ui.theme.RahaBlue
import com.example.ui.theme.RahaGreen
import com.example.ui.theme.TealPrimary
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OfflineCacheDialog(
    metadata: OfflineCacheMetadataEntity?,
    totalRestroomsCount: Int,
    isOfflineMode: Boolean,
    onToggleOfflineMode: () -> Unit,
    onRefreshCache: () -> Unit,
    cachedRegionIds: Set<String> = setOf(AlgerianRegions.REGION_CENTRE.id),
    regionDownloadProgress: Map<String, Float> = emptyMap(),
    onDownloadRegion: (AlgerianRegion) -> Unit = {},
    onRemoveRegion: (AlgerianRegion) -> Unit = {},
    onDownloadAllRegions: () -> Unit = {},
    onDismiss: () -> Unit
) {
    val lang = LocalAppLanguage.current
    val locale = when (lang) {
        AppLanguage.AR -> Locale("ar", "DZ")
        AppLanguage.FR -> Locale.FRENCH
        AppLanguage.EN -> Locale.ENGLISH
    }
    val dateFormatter = remember { SimpleDateFormat("dd MMMM yyyy, HH:mm", locale) }
    val defaultSyncText = when (lang) {
        AppLanguage.AR -> "محدث ومخزن بالكامل"
        AppLanguage.FR -> "Entièrement à jour et mis en cache"
        AppLanguage.EN -> "Fully updated and cached"
    }
    val lastSyncFormatted = metadata?.lastCachedTimestamp?.let {
        dateFormatter.format(Date(it))
    } ?: defaultSyncText

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .heightIn(max = 680.dp)
                .testTag("offline_cache_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(RahaGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        val storageDesc = when (lang) {
                            AppLanguage.AR -> "التخزين المحلي Room"
                            AppLanguage.FR -> "Stockage local Room"
                            AppLanguage.EN -> "Room Local Storage"
                        }
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = storageDesc,
                            tint = RahaGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        val headerTitle = when (lang) {
                            AppLanguage.AR -> "الخرائط والتخزين بدون إنترنت"
                            AppLanguage.FR -> "Cartes & Stockage Hors-Ligne"
                            AppLanguage.EN -> "Offline Maps & Local Storage"
                        }
                        val headerSub = when (lang) {
                            AppLanguage.AR -> "تنزيل بيانات مناطق الجزائر للعمل أوفلاين 100%"
                            AppLanguage.FR -> "Téléchargez les régions d'Algérie pour usage hors-ligne"
                            AppLanguage.EN -> "Download Algeria regions for 100% offline access"
                        }
                        Text(
                            text = headerTitle,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = headerSub,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Navigation Tabs between Regional Maps and Database Info
                val tab1Title = when (lang) {
                    AppLanguage.AR -> "خرائط المناطق (${cachedRegionIds.size}/5)"
                    AppLanguage.FR -> "Régions (${cachedRegionIds.size}/5)"
                    AppLanguage.EN -> "Regions (${cachedRegionIds.size}/5)"
                }
                val tab2Title = when (lang) {
                    AppLanguage.AR -> "حالة التخزين"
                    AppLanguage.FR -> "Statut Stockage"
                    AppLanguage.EN -> "Storage Status"
                }

                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    contentColor = TealPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = {
                            Text(
                                text = tab1Title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = {
                            Text(
                                text = tab2Title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tab Content Body (Scrollable)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (selectedTabIndex == 0) {
                        // Regional Maps Download & Caching View
                        RegionalCacheSection(
                            lang = lang,
                            cachedRegionIds = cachedRegionIds,
                            regionDownloadProgress = regionDownloadProgress,
                            onDownloadRegion = onDownloadRegion,
                            onRemoveRegion = onRemoveRegion,
                            onDownloadAllRegions = onDownloadAllRegions
                        )
                    } else {
                        // General Room DB Cache Stats & Offline Simulator
                        DatabaseStatsSection(
                            lang = lang,
                            metadata = metadata,
                            totalRestroomsCount = totalRestroomsCount,
                            lastSyncFormatted = lastSyncFormatted,
                            isOfflineMode = isOfflineMode,
                            onToggleOfflineMode = onToggleOfflineMode
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val refreshLabel = when (lang) {
                        AppLanguage.AR -> "تحديث الكل"
                        AppLanguage.FR -> "Actualiser"
                        AppLanguage.EN -> "Refresh all"
                    }
                    OutlinedButton(
                        onClick = onRefreshCache,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("refresh_cache_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(refreshLabel, fontSize = 12.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RahaBlue),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("close_cache_dialog_button")
                    ) {
                        Text(AppStrings.close(lang), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RegionalCacheSection(
    lang: AppLanguage,
    cachedRegionIds: Set<String>,
    regionDownloadProgress: Map<String, Float>,
    onDownloadRegion: (AlgerianRegion) -> Unit,
    onRemoveRegion: (AlgerianRegion) -> Unit,
    onDownloadAllRegions: () -> Unit
) {
    // Quick summary card and bulk action
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = RahaGreen.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, RahaGreen.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    val statusTitle = when (lang) {
                        AppLanguage.AR -> "المناطق المحفوظة محلياً"
                        AppLanguage.FR -> "Régions enregistrées"
                        AppLanguage.EN -> "Cached Regions"
                    }
                    val statusCount = "${cachedRegionIds.size} / 5"
                    Text(
                        text = "$statusTitle: $statusCount",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E392A)
                    )
                    val noteText = when (lang) {
                        AppLanguage.AR -> "يمكنك تصفح المرافق والخرائط بدون اتصال إنترنت"
                        AppLanguage.FR -> "Accédez aux sanitaires et cartes sans internet"
                        AppLanguage.EN -> "Access restrooms and maps without internet"
                    }
                    Text(
                        text = noteText,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (cachedRegionIds.size < 5) {
                    val downloadAllText = when (lang) {
                        AppLanguage.AR -> "تنزيل الكل"
                        AppLanguage.FR -> "Tout charger"
                        AppLanguage.EN -> "Download all"
                    }
                    Button(
                        onClick = onDownloadAllRegions,
                        colors = ButtonDefaults.buttonColors(containerColor = RahaGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("download_all_regions_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(downloadAllText, fontSize = 11.sp)
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // List of 5 macro-regions of Algeria
    AlgerianRegions.allRegions.forEach { region ->
        val isCached = region.id in cachedRegionIds
        val progress = regionDownloadProgress[region.id]
        val isDownloading = progress != null

        RegionalCard(
            region = region,
            isCached = isCached,
            isDownloading = isDownloading,
            downloadProgress = progress ?: 0f,
            lang = lang,
            onDownload = { onDownloadRegion(region) },
            onRemove = { onRemoveRegion(region) }
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RegionalCard(
    region: AlgerianRegion,
    isCached: Boolean,
    isDownloading: Boolean,
    downloadProgress: Float,
    lang: AppLanguage,
    onDownload: () -> Unit,
    onRemove: () -> Unit
) {
    val regionIcon = when (region.id) {
        AlgerianRegions.REGION_CENTRE.id -> Icons.Default.LocationCity
        AlgerianRegions.REGION_OUEST.id -> Icons.Default.Landscape
        AlgerianRegions.REGION_EST.id -> Icons.Default.Terrain
        AlgerianRegions.REGION_HAUTS_PLATEAUX.id -> Icons.Default.Apartment
        AlgerianRegions.REGION_SUD.id -> Icons.Default.WbSunny
        else -> Icons.Default.Map
    }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isCached) RahaGreen.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (isCached) RahaGreen.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("region_card_${region.id}")
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Region Title & Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isCached) RahaGreen.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = regionIcon,
                            contentDescription = null,
                            tint = if (isCached) RahaGreen else Color.DarkGray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = region.getName(lang),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        val facilitiesLabel = when (lang) {
                            AppLanguage.AR -> "${region.facilitiesCount} مرافق • ${region.sizeFormatted}"
                            AppLanguage.FR -> "${region.facilitiesCount} lieux • ${region.sizeFormatted}"
                            AppLanguage.EN -> "${region.facilitiesCount} places • ${region.sizeFormatted}"
                        }
                        Text(
                            text = facilitiesLabel,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Cache status badge
                if (isCached) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = RahaGreen.copy(alpha = 0.15f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = RahaGreen,
                                modifier = Modifier.size(12.dp)
                            )
                            val cachedBadge = when (lang) {
                                AppLanguage.AR -> "محفوظة أوفلاين"
                                AppLanguage.FR -> "Hors-ligne"
                                AppLanguage.EN -> "Cached"
                            }
                            Text(
                                text = cachedBadge,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = RahaGreen
                            )
                        }
                    }
                }
            }

            // Region Summary
            Text(
                text = region.getSummary(lang),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 15.sp
            )

            // Covered Wilayas Tags
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                region.wilayasAr.take(6).forEach { wilaya ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Text(
                            text = wilaya,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                if (region.wilayasAr.size > 6) {
                    val moreCount = "+${region.wilayasAr.size - 6}"
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                    ) {
                        Text(
                            text = moreCount,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Download Progress / Action Buttons
            if (isDownloading) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    val percent = (downloadProgress * 100).toInt()
                    val downloadingText = when (lang) {
                        AppLanguage.AR -> "جاري حفظ بيانات وخريطة المنطقة محلياً... $percent%"
                        AppLanguage.FR -> "Téléchargement des données locales... $percent%"
                        AppLanguage.EN -> "Saving regional map data locally... $percent%"
                    }
                    Text(
                        text = downloadingText,
                        fontSize = 11.sp,
                        color = TealPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    LinearProgressIndicator(
                        progress = { downloadProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = TealPrimary,
                        trackColor = TealPrimary.copy(alpha = 0.2f)
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isCached) {
                        val removeText = when (lang) {
                            AppLanguage.AR -> "إلغاء التخزين"
                            AppLanguage.FR -> "Supprimer"
                            AppLanguage.EN -> "Clear cache"
                        }
                        OutlinedButton(
                            onClick = onRemove,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("remove_region_${region.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = removeText,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    } else {
                        val downloadText = when (lang) {
                            AppLanguage.AR -> "تنزيل خريطة المنطقة (${region.sizeFormatted})"
                            AppLanguage.FR -> "Télécharger (${region.sizeFormatted})"
                            AppLanguage.EN -> "Download region (${region.sizeFormatted})"
                        }
                        Button(
                            onClick = onDownload,
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("download_region_${region.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = downloadText, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DatabaseStatsSection(
    lang: AppLanguage,
    metadata: OfflineCacheMetadataEntity?,
    totalRestroomsCount: Int,
    lastSyncFormatted: String,
    isOfflineMode: Boolean,
    onToggleOfflineMode: () -> Unit
) {
    // Cache Stats Card
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = RahaGreen.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, RahaGreen.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CloudDone,
                    contentDescription = null,
                    tint = RahaGreen,
                    modifier = Modifier.size(18.dp)
                )
                val dbStatus = when (lang) {
                    AppLanguage.AR -> "حالة قاعدة البيانات: مخزنة محلياً 100%"
                    AppLanguage.FR -> "Statut base de données : 100% hors-ligne"
                    AppLanguage.EN -> "Database status: 100% cached locally"
                }
                Text(
                    text = dbStatus,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = RahaGreen
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val cachedCount = if (metadata != null && metadata.totalRestStopsCached > 0) metadata.totalRestStopsCached else totalRestroomsCount
                val placesUnit = when (lang) {
                    AppLanguage.AR -> "مرفق"
                    AppLanguage.FR -> "lieux"
                    AppLanguage.EN -> "places"
                }
                val wilayasCovered = if (metadata != null && metadata.cachedWilayasCount > 0) metadata.cachedWilayasCount else 15
                val wilayasUnit = when (lang) {
                    AppLanguage.AR -> "ولاية"
                    AppLanguage.FR -> "wilayas"
                    AppLanguage.EN -> "provinces"
                }
                val pill1Title = when (lang) {
                    AppLanguage.AR -> "المرافق المخزنة"
                    AppLanguage.FR -> "Lieux enregistrés"
                    AppLanguage.EN -> "Cached facilities"
                }
                val pill2Title = when (lang) {
                    AppLanguage.AR -> "الولايات المشمولة"
                    AppLanguage.FR -> "Wilayas couvertes"
                    AppLanguage.EN -> "Provinces covered"
                }
                StatPill(
                    title = pill1Title,
                    value = "$cachedCount $placesUnit"
                )
                StatPill(
                    title = pill2Title,
                    value = "$wilayasCovered+ $wilayasUnit"
                )
            }

            val lastUpdateText = when (lang) {
                AppLanguage.AR -> "آخر تحديث للتخزين: $lastSyncFormatted"
                AppLanguage.FR -> "Dernière synchro : $lastSyncFormatted"
                AppLanguage.EN -> "Last cache sync: $lastSyncFormatted"
            }
            Text(
                text = lastUpdateText,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Offline Mode Simulation Switch
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isOfflineMode) Color(0xFFFFF3E0) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = if (isOfflineMode) Icons.Default.WifiOff else Icons.Default.CloudDone,
                    contentDescription = null,
                    tint = if (isOfflineMode) Color(0xFFE65100) else RahaBlue,
                    modifier = Modifier.size(22.dp)
                )
                Column {
                    val switchTitle = if (isOfflineMode) when (lang) {
                        AppLanguage.AR -> "وضع عدم الاتصال مُفعّل"
                        AppLanguage.FR -> "Mode hors-ligne activé"
                        AppLanguage.EN -> "Offline mode enabled"
                    } else when (lang) {
                        AppLanguage.AR -> "محاكاة عدم الاتصال بالإنترنت"
                        AppLanguage.FR -> "Simuler le mode hors-ligne"
                        AppLanguage.EN -> "Simulate offline mode"
                    }
                    val switchSub = if (isOfflineMode) when (lang) {
                        AppLanguage.AR -> "تصفح البيانات والخرائط محلياً فقط"
                        AppLanguage.FR -> "Données et cartes locales uniquement"
                        AppLanguage.EN -> "Browse local data and maps only"
                    } else when (lang) {
                        AppLanguage.AR -> "اختبر التطبيق كأنك في مكان بدون شبكة"
                        AppLanguage.FR -> "Testez l'application sans réseau mobile"
                        AppLanguage.EN -> "Test app as if without cellular network"
                    }
                    Text(
                        text = switchTitle,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = switchSub,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Switch(
                checked = isOfflineMode,
                onCheckedChange = { onToggleOfflineMode() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFFE65100),
                    checkedTrackColor = Color(0xFFFFCC80)
                ),
                modifier = Modifier.testTag("offline_mode_switch")
            )
        }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Cached metadata features
    val featuresHeader = when (lang) {
        AppLanguage.AR -> "البيانات المتوفرة بدون إنترنت:"
        AppLanguage.FR -> "Données disponibles hors connexion :"
        AppLanguage.EN -> "Metadata available offline:"
    }
    Text(
        text = featuresHeader,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(6.dp))

    val feat1 = when (lang) {
        AppLanguage.AR -> "📍 الإحداثيات الجغرافية الدقيقة (Latitude / Longitude)"
        AppLanguage.FR -> "📍 Coordonnées GPS précises (Latitude / Longitude)"
        AppLanguage.EN -> "📍 Accurate GPS Coordinates (Latitude / Longitude)"
    }
    val feat2 = when (lang) {
        AppLanguage.AR -> "♿ جاهزية المرافق لذوي الاحتياجات الخاصة (PMR)"
        AppLanguage.FR -> "♿ Accessibilité PMR et équipements adaptés"
        AppLanguage.EN -> "♿ Wheelchair Accessibility (PRM) & equipment"
    }
    val feat3 = when (lang) {
        AppLanguage.AR -> "👶 توفر طاولات تغيير الحفاضات للرضع"
        AppLanguage.FR -> "👶 Espaces de change pour bébés"
        AppLanguage.EN -> "👶 Baby changing facilities"
    }
    val feat4 = when (lang) {
        AppLanguage.AR -> "💧 حالة المياه الصالحة ونقاط الوضوء والاستحمام"
        AppLanguage.FR -> "💧 Eau courante, ablutions et douches"
        AppLanguage.EN -> "💧 Running water, wudu points, and showers"
    }
    val feat5 = when (lang) {
        AppLanguage.AR -> "💰 الأسعار الرسمية ودورات المياه المجانية"
        AppLanguage.FR -> "💰 Tarifs d'accès et sanitaires gratuits"
        AppLanguage.EN -> "💰 Entry fees and free restrooms"
    }
    val feat6 = when (lang) {
        AppLanguage.AR -> "🗺️ خريطة متجهية تفاعلية مدمجة بدون اتصال خارجي"
        AppLanguage.FR -> "🗺️ Carte interactive vectorielle intégrée hors-ligne"
        AppLanguage.EN -> "🗺️ Built-in interactive offline vector map"
    }

    OfflineFeatureRow(feat1)
    OfflineFeatureRow(feat2)
    OfflineFeatureRow(feat3)
    OfflineFeatureRow(feat4)
    OfflineFeatureRow(feat5)
    OfflineFeatureRow(feat6)
}

@Composable
private fun StatPill(title: String, value: String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.8f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(title, fontSize = 10.sp, color = Color.Gray)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E392A))
    }
}

@Composable
private fun OfflineFeatureRow(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.padding(vertical = 3.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = RahaGreen,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = text,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
