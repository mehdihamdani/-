package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HotTub
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shower
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AlgerianWilayas
import com.example.data.model.AppLanguage
import com.example.data.model.RestroomEntity
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AlertRed
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.HammamBronze
import com.example.ui.theme.HammamBronzeDark
import com.example.ui.theme.HammamBronzeLight
import com.example.ui.theme.HammamGoldAccent
import com.example.ui.theme.IftarAmber
import com.example.ui.theme.PersianGreen
import com.example.ui.theme.RahaBlue
import com.example.ui.theme.StarGold
import com.example.ui.theme.SteamAmber
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.ThermalSpringTeal
import com.example.ui.theme.WomenSectionViolet
import com.example.ui.viewmodel.RestroomWithDistance
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage

enum class HammamServiceFilter {
    ALL,
    THERMAL_SPRING,
    STEAM_SAUNA,
    KESSA_SCRUB,
    PRIVATE_SHOWER,
    WOMEN_SCHEDULE
}

/**
 * Dedicated Hammams & Thermal Baths Directory Screen ("صفحة الحمامات وخدماتهم")
 * showcasing Algerian traditional Hammams, modern spas, and mineral thermal springs.
 */
@Composable
fun HammamsDirectoryScreen(
    hammams: List<RestroomWithDistance>,
    onHammamSelected: (RestroomEntity) -> Unit,
    onNavigateClick: (RestroomEntity) -> Unit,
    onFavoriteToggle: (RestroomEntity) -> Unit,
    onAddHammamClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    var searchQuery by remember { mutableStateOf("") }
    var activeFilter by remember { mutableStateOf(HammamServiceFilter.ALL) }

    BackHandler {
        onBack()
    }

    val filteredHammams by remember(hammams, searchQuery, activeFilter) {
        derivedStateOf {
            hammams.filter { item ->
                val h = item.restroom
                val matchesService = when (activeFilter) {
                    HammamServiceFilter.ALL -> true
                    HammamServiceFilter.THERMAL_SPRING -> h.isThermalSpring
                    HammamServiceFilter.STEAM_SAUNA -> h.hasSteamSauna
                    HammamServiceFilter.KESSA_SCRUB -> h.hasKessaScrub
                    HammamServiceFilter.PRIVATE_SHOWER -> h.hasPrivateShowerRooms || h.hasShower
                    HammamServiceFilter.WOMEN_SCHEDULE -> h.hasWomenSection || h.hammamWomenHours.isNotBlank()
                }
                if (!matchesService) return@filter false

                if (searchQuery.isNotBlank()) {
                    val q = searchQuery.trim().lowercase()
                    val matchName = h.name.lowercase().contains(q) || h.nameFr.lowercase().contains(q)
                    val matchPlace = h.wilaya.lowercase().contains(q) || h.commune.lowercase().contains(q)
                    val matchServices = h.hammamServicesNotes.lowercase().contains(q) || h.notes.lowercase().contains(q)
                    if (!matchName && !matchPlace && !matchServices) return@filter false
                }
                true
            }
        }
    }

    val thermalCount = remember(hammams) { hammams.count { it.restroom.isThermalSpring } }
    val womenCount = remember(hammams) { hammams.count { it.restroom.hasWomenSection } }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("hammams_directory_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Hero Banner with Live Statistics & Add Hammam CTA
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(HammamBronzeDark, HammamBronze, HammamBronzeLight)
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.HotTub,
                                        contentDescription = null,
                                        tint = HammamGoldAccent,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = AppStrings.hammamsDirectoryTitle(lang),
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    color = Color.White
                                )
                                Text(
                                    text = AppStrings.hammamsDirectorySubtitle(lang),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        HammamStatBox(
                            value = hammams.size.toString(),
                            label = AppStrings.statTotalHammams(lang),
                            modifier = Modifier.weight(1f)
                        )
                        HammamStatBox(
                            value = thermalCount.toString(),
                            label = AppStrings.statThermalSprings(lang),
                            modifier = Modifier.weight(1f)
                        )
                        HammamStatBox(
                            value = womenCount.toString(),
                            label = AppStrings.statWomenFriendly(lang),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onAddHammamClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = HammamBronzeDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("add_hammam_directory_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.addNewHammamButton(lang),
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        // 2. Search Bar & Specialized Hammam Service Filter Chips
        item {
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = AppStrings.searchHammamsPlaceholder(lang),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = HammamBronze
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = AppStrings.close(lang)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hammams_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HammamFilterChipItem(
                        selected = activeFilter == HammamServiceFilter.ALL,
                        onClick = { activeFilter = HammamServiceFilter.ALL },
                        label = AppStrings.filterAll(lang),
                        selectedColor = HammamBronze,
                        tag = "hammam_filter_all"
                    )
                    HammamFilterChipItem(
                        selected = activeFilter == HammamServiceFilter.THERMAL_SPRING,
                        onClick = { activeFilter = HammamServiceFilter.THERMAL_SPRING },
                        label = "♨️ " + AppStrings.thermalSpringBadge(lang),
                        selectedColor = ThermalSpringTeal,
                        tag = "hammam_filter_thermal"
                    )
                    HammamFilterChipItem(
                        selected = activeFilter == HammamServiceFilter.STEAM_SAUNA,
                        onClick = { activeFilter = HammamServiceFilter.STEAM_SAUNA },
                        label = "🧖 " + AppStrings.steamSaunaBadge(lang),
                        selectedColor = SteamAmber,
                        tag = "hammam_filter_steam"
                    )
                    HammamFilterChipItem(
                        selected = activeFilter == HammamServiceFilter.KESSA_SCRUB,
                        onClick = { activeFilter = HammamServiceFilter.KESSA_SCRUB },
                        label = "🧼 " + AppStrings.kessaScrubBadge(lang),
                        selectedColor = HammamBronze,
                        tag = "hammam_filter_kessa"
                    )
                    HammamFilterChipItem(
                        selected = activeFilter == HammamServiceFilter.PRIVATE_SHOWER,
                        onClick = { activeFilter = HammamServiceFilter.PRIVATE_SHOWER },
                        label = "🚿 " + AppStrings.privateShowerRoomsBadge(lang),
                        selectedColor = RahaBlue,
                        tag = "hammam_filter_private_shower"
                    )
                    HammamFilterChipItem(
                        selected = activeFilter == HammamServiceFilter.WOMEN_SCHEDULE,
                        onClick = { activeFilter = HammamServiceFilter.WOMEN_SCHEDULE },
                        label = "🧕 " + AppStrings.hammamWomenHoursLabel(lang),
                        selectedColor = WomenSectionViolet,
                        tag = "hammam_filter_women"
                    )
                }
            }
        }

        // 3. Hammam Cards List or Empty State
        if (filteredHammams.isEmpty()) {
            item {
                EmptyStateNotice(
                    title = AppStrings.noResultsTitle(lang),
                    subtitle = AppStrings.noResultsSubtitle(lang)
                )
            }
        } else {
            items(filteredHammams, key = { it.restroom.id }) { item ->
                HammamDirectoryCard(
                    item = item,
                    lang = lang,
                    onOpenProfile = { onHammamSelected(item.restroom) },
                    onNavigate = { onNavigateClick(item.restroom) },
                    onFavoriteToggle = { onFavoriteToggle(item.restroom) },
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun HammamStatBox(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.16f),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = HammamGoldAccent
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun HammamFilterChipItem(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    selectedColor: Color,
    tag: String
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, fontWeight = FontWeight.SemiBold) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = selectedColor,
            selectedLabelColor = Color.White
        ),
        modifier = Modifier.testTag(tag)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HammamDirectoryCard(
    item: RestroomWithDistance,
    lang: AppLanguage,
    onOpenProfile: () -> Unit,
    onNavigate: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hammam = item.restroom
    val accentColor = if (hammam.isThermalSpring) ThermalSpringTeal else HammamBronze

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenProfile)
            .testTag("hammam_directory_card_${hammam.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.28f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(accentColor.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.HotTub,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = hammam.getDisplayName(lang),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${AlgerianWilayas.getLocalizedWilayaName(hammam.wilaya, lang)} • ${hammam.commune}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = accentColor.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.DirectionsWalk,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = item.formattedDistance,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = accentColor
                        )
                    }
                }

                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = if (hammam.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = AppStrings.navFavorites(lang),
                        tint = if (hammam.isFavorite) AlertRed else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Men & Women Hours Quick Summary
            if (hammam.hammamMenHours.isNotBlank() || hammam.hammamWomenHours.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (hammam.hammamMenHours.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = RahaBlue, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = hammam.hammamMenHours,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        if (hammam.hammamWomenHours.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Groups, contentDescription = null, tint = WomenSectionViolet, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = hammam.hammamWomenHours,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Services & Price FlowRow
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusBadge(
                    text = if (hammam.isFree) AppStrings.free(lang) else "${hammam.priceDzd} ${AppStrings.dzdCurrency(lang)}",
                    color = if (hammam.isFree) SuccessGreen else HammamBronze
                )
                StatusBadge(
                    text = "${hammam.rating} ★ (${hammam.reviewsCount})",
                    color = StarGold,
                    icon = Icons.Default.Star
                )
                if (hammam.isThermalSpring) {
                    StatusBadge(
                        text = AppStrings.thermalSpringBadge(lang),
                        color = ThermalSpringTeal,
                        icon = Icons.Default.HotTub
                    )
                }
                if (hammam.hasSteamSauna) {
                    StatusBadge(
                        text = AppStrings.steamSaunaBadge(lang),
                        color = SteamAmber,
                        icon = Icons.Default.HotTub
                    )
                }
                if (hammam.hasKessaScrub) {
                    StatusBadge(
                        text = AppStrings.kessaScrubBadge(lang),
                        color = HammamBronze,
                        icon = Icons.Default.Spa
                    )
                }
                if (hammam.hasPrivateShowerRooms || hammam.hasShower) {
                    StatusBadge(
                        text = AppStrings.privateShowerRoomsBadge(lang),
                        color = RahaBlue,
                        icon = Icons.Default.Shower
                    )
                }
                if (hammam.hasTowelRental) {
                    StatusBadge(
                        text = AppStrings.towelRentalBadge(lang),
                        color = PersianGreen,
                        icon = Icons.Default.Checkroom
                    )
                }
                if (hammam.hasTeaLounge) {
                    StatusBadge(
                        text = AppStrings.teaLoungeBadge(lang),
                        color = IftarAmber,
                        icon = Icons.Default.LocalCafe
                    )
                }
                if (hammam.hasWomenSection) {
                    StatusBadge(
                        text = AppStrings.womenSection(lang),
                        color = WomenSectionViolet,
                        icon = Icons.Default.Wc
                    )
                }
                if (hammam.hasRemotePayment) {
                    StatusBadge(
                        text = AppStrings.remotePaymentBadge(lang),
                        color = RahaBlue,
                        icon = Icons.Default.CreditCard
                    )
                }
                if (hammam.facebookUrl.isNotBlank()) {
                    StatusBadge(
                        text = "Facebook",
                        color = FacebookBlue,
                        icon = Icons.Default.Public
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onOpenProfile,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp)
                        .minimumInteractiveComponentSize()
                        .testTag("open_hammam_detail_${hammam.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.HotTub,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.openHammamPage(lang),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                OutlinedButton(
                    onClick = onNavigate,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, accentColor),
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .minimumInteractiveComponentSize()
                        .testTag("navigate_hammam_${hammam.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = AppStrings.navigate(lang),
                        color = accentColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
