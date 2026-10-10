package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AlgerianWilayas
import com.example.data.model.WilayaInfo
import com.example.ui.theme.RahaBlue
import com.example.ui.theme.RahaGreen
import com.example.ui.theme.TealPrimary

/**
 * Top floating search bar designed specifically for the Map View.
 * Allows searching by Algerian Wilaya (1-58), commune, or district,
 * with quick access chips for prominent Algerian cities.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onWilayaSelected: (WilayaInfo) -> Unit,
    currentWilayaName: String,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    // Popular Algerian cities for fast one-tap navigation
    val popularWilayas = remember {
        listOf(
            AlgerianWilayas.all58Wilayas.find { it.code == 16 } ?: AlgerianWilayas.all58Wilayas[15], // Alger
            AlgerianWilayas.all58Wilayas.find { it.code == 31 } ?: AlgerianWilayas.all58Wilayas[30], // Oran
            AlgerianWilayas.all58Wilayas.find { it.code == 25 } ?: AlgerianWilayas.all58Wilayas[24], // Constantine
            AlgerianWilayas.all58Wilayas.find { it.code == 19 } ?: AlgerianWilayas.all58Wilayas[18], // Setif
            AlgerianWilayas.all58Wilayas.find { it.code == 23 } ?: AlgerianWilayas.all58Wilayas[22], // Annaba
            AlgerianWilayas.all58Wilayas.find { it.code == 9 } ?: AlgerianWilayas.all58Wilayas[8],   // Blida
            AlgerianWilayas.all58Wilayas.find { it.code == 2 } ?: AlgerianWilayas.all58Wilayas[1],   // Chlef
            AlgerianWilayas.all58Wilayas.find { it.code == 13 } ?: AlgerianWilayas.all58Wilayas[12], // Tlemcen
            AlgerianWilayas.all58Wilayas.find { it.code == 5 } ?: AlgerianWilayas.all58Wilayas[4],   // Batna
            AlgerianWilayas.all58Wilayas.find { it.code == 6 } ?: AlgerianWilayas.all58Wilayas[5],   // Bejaia
            AlgerianWilayas.all58Wilayas.find { it.code == 30 } ?: AlgerianWilayas.all58Wilayas[29]  // Ouargla
        )
    }

    val matchingWilayas = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            emptyList()
        } else {
            val q = searchQuery.trim().lowercase()
            AlgerianWilayas.all58Wilayas.filter { wilaya ->
                wilaya.fullSearchableText.contains(q) ||
                wilaya.arabicName.contains(q) ||
                wilaya.frenchName.lowercase().contains(q) ||
                wilaya.code.toString() == q
            }.take(8)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // Main Search Bar Surface
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
            shadowElevation = 6.dp,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(TealPrimary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "بحث في الجزائر",
                            tint = TealPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            onSearchQueryChange(it)
                            isExpanded = it.isNotBlank()
                        },
                        placeholder = {
                            Text(
                                text = "ابحث بالولاية (مثلاً: وهران، سطيف...) أو الحي",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("map_search_bar_input")
                    )

                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                onSearchQueryChange("")
                                isExpanded = false
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "مسح البحث",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Dropdown Suggestions for matching Algerian Wilayas
                AnimatedVisibility(
                    visible = isExpanded && matchingWilayas.isNotEmpty(),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 220.dp)
                        ) {
                            items(matchingWilayas, key = { it.code }) { wilaya ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onWilayaSelected(wilaya)
                                            isExpanded = false
                                        }
                                        .padding(horizontal = 16.dp, vertical = 10.dp)
                                        .testTag("search_suggestion_${wilaya.code}")
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(RahaBlue.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = String.format("%02d", wilaya.code),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = RahaBlue
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "ولاية ${wilaya.arabicName}",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${wilaya.frenchName} • انقر للانتقال على الخريطة",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = TealPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                            }
                        }
                    }
                }
            }
        }

        // Quick City Suggestion Chips (Horizontally Scrollable)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                shadowElevation = 2.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationCity,
                        contentDescription = null,
                        tint = RahaGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "مدن سريعة:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            popularWilayas.forEach { wilaya ->
                val isSelected = currentWilayaName.contains(wilaya.arabicName)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) TealPrimary else MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    shadowElevation = if (isSelected) 3.dp else 1.dp,
                    modifier = Modifier
                        .clickable { onWilayaSelected(wilaya) }
                        .testTag("quick_city_chip_${wilaya.code}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "${wilaya.arabicName} (${wilaya.code})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }
        }
    }
}
