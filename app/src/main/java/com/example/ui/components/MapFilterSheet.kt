package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Shower
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MosqueEmerald
import com.example.ui.theme.NaftalFlame
import com.example.ui.theme.RahaBlue
import com.example.ui.theme.RahaGreen
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.WaterBlue
import com.example.ui.viewmodel.FilterState
import com.example.data.model.AppLanguage
import com.example.util.LocalAppLanguage
import com.example.util.AppStrings

@Composable
fun MapQuickFilterChips(
    filterState: FilterState,
    matchCount: Int,
    onOpenFilterSheet: () -> Unit,
    onToggleAccessible: () -> Unit,
    onToggleCleanliness: () -> Unit,
    onToggleChangingTable: () -> Unit,
    onToggleFreeOnly: () -> Unit,
    onResetAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val lang = LocalAppLanguage.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Master Filter Sheet Button with badge
        Surface(
            onClick = onOpenFilterSheet,
            shape = RoundedCornerShape(20.dp),
            color = if (filterState.activeFilterCount > 0) RahaGreen else MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
            shadowElevation = 4.dp,
            border = BorderStroke(
                width = 1.dp,
                color = if (filterState.activeFilterCount > 0) RahaGreen else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            ),
            modifier = Modifier.testTag("map_filter_sheet_trigger")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                val allFiltersDesc = when (lang) {
                    AppLanguage.AR -> "كل الفلاتر"
                    AppLanguage.FR -> "Tous les filtres"
                    AppLanguage.EN -> "All filters"
                }
                val filterText = when (lang) {
                    AppLanguage.AR -> "تصفية"
                    AppLanguage.FR -> "Filtrer"
                    AppLanguage.EN -> "Filter"
                }
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = allFiltersDesc,
                    tint = if (filterState.activeFilterCount > 0) Color.White else RahaBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = filterText,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (filterState.activeFilterCount > 0) Color.White else MaterialTheme.colorScheme.onSurface
                )
                if (filterState.activeFilterCount > 0) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${filterState.activeFilterCount}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = RahaGreen
                        )
                    }
                }
            }
        }

        // 1. Accessibility Chip
        val accessibleTitle = when (lang) {
            AppLanguage.AR -> "ذوي الهمم"
            AppLanguage.FR -> "Accès PMR"
            AppLanguage.EN -> "Accessible"
        }
        MapFilterQuickChip(
            title = accessibleTitle,
            icon = Icons.AutoMirrored.Filled.Accessible,
            isActive = filterState.accessiblePmrOnly,
            activeColor = Color(0xFF0077B6),
            onClick = onToggleAccessible,
            testTag = "chip_filter_accessible"
        )

        // 2. High Cleanliness Chip
        val cleanTitle = when (lang) {
            AppLanguage.AR -> "نظافة عالية"
            AppLanguage.FR -> "Très propre"
            AppLanguage.EN -> "High cleanliness"
        }
        MapFilterQuickChip(
            title = cleanTitle,
            icon = Icons.Default.CleaningServices,
            isActive = filterState.highCleanlinessOnly,
            activeColor = Color(0xFF2E7D32),
            onClick = onToggleCleanliness,
            testTag = "chip_filter_cleanliness"
        )

        // 3. Changing Table Chip
        val babyTitle = when (lang) {
            AppLanguage.AR -> "طاولة رضع"
            AppLanguage.FR -> "Table à langer"
            AppLanguage.EN -> "Baby changing"
        }
        MapFilterQuickChip(
            title = babyTitle,
            icon = Icons.Default.ChildCare,
            isActive = filterState.changingTableOnly,
            activeColor = Color(0xFFE91E63),
            onClick = onToggleChangingTable,
            testTag = "chip_filter_changing_table"
        )

        // 4. Free Only Chip
        MapFilterQuickChip(
            title = AppStrings.free(lang),
            icon = Icons.Default.AttachMoney,
            isActive = filterState.freeOnly,
            activeColor = SuccessGreen,
            onClick = onToggleFreeOnly,
            testTag = "chip_filter_free"
        )

        // Reset All Chip if any filter active
        if (filterState.activeFilterCount > 0) {
            Surface(
                onClick = onResetAll,
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.85f),
                modifier = Modifier.testTag("chip_filter_reset_all")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    val resetDesc = when (lang) {
                        AppLanguage.AR -> "مسح الفلاتر"
                        AppLanguage.FR -> "Effacer les filtres"
                        AppLanguage.EN -> "Clear filters"
                    }
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = resetDesc,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = resetDesc,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@Composable
fun MapFilterQuickChip(
    title: String,
    icon: ImageVector,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isActive) activeColor else MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        shadowElevation = if (isActive) 3.dp else 2.dp,
        border = BorderStroke(
            width = if (isActive) 1.5.dp else 1.dp,
            color = if (isActive) activeColor else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        ),
        modifier = modifier.testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isActive) Color.White else activeColor,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (isActive) Color.White else MaterialTheme.colorScheme.onSurface
            )
            if (isActive) {
                Spacer(modifier = Modifier.width(3.dp))
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapFilterSheet(
    filterState: FilterState,
    matchCount: Int,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onToggleAccessible: () -> Unit,
    onToggleCleanliness: () -> Unit,
    onToggleChangingTable: () -> Unit,
    onToggleFreeOnly: () -> Unit,
    onToggleWomenOnly: () -> Unit,
    onToggleWuduOnly: () -> Unit,
    onToggleShowerOnly: () -> Unit,
    onToggleMosques: () -> Unit,
    onToggleRestAreas: () -> Unit,
    onTogglePublicToilets: () -> Unit,
    onResetAll: () -> Unit
) {
    val lang = LocalAppLanguage.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("map_filter_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(RahaGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = RahaGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        val sheetTitle = when (lang) {
                            AppLanguage.AR -> "تصفية المعالم على الخريطة"
                            AppLanguage.FR -> "Filtrer les lieux sur la carte"
                            AppLanguage.EN -> "Filter places on map"
                        }
                        val sheetSubtitle = when (lang) {
                            AppLanguage.AR -> "$matchCount نقطة راحة مطابقة للاختيارات"
                            AppLanguage.FR -> "$matchCount points relais trouvés"
                            AppLanguage.EN -> "$matchCount matching facilities found"
                        }
                        Text(
                            text = sheetTitle,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = sheetSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                TextButton(
                    onClick = onResetAll,
                    enabled = filterState.activeFilterCount > 0
                ) {
                    val resetText = when (lang) {
                        AppLanguage.AR -> "إعادة ضبط"
                        AppLanguage.FR -> "Réinitialiser"
                        AppLanguage.EN -> "Reset"
                    }
                    Text(
                        text = resetText,
                        fontWeight = FontWeight.Bold,
                        color = if (filterState.activeFilterCount > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Section 1: Main Requested Criteria (Accessibility, Cleanliness, Changing Tables)
            val sec1Title = when (lang) {
                AppLanguage.AR -> "معايير الراحة والتجهيزات الخاصة"
                AppLanguage.FR -> "Critères de confort et accessibilité"
                AppLanguage.EN -> "Comfort & accessibility criteria"
            }
            Text(
                text = sec1Title,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 1. Accessibility
            val pmrTitle = when (lang) {
                AppLanguage.AR -> "سهولة الوصول لذوي الاحتياجات (PMR)"
                AppLanguage.FR -> "Accès personnes à mobilité réduite (PMR)"
                AppLanguage.EN -> "Wheelchair Accessible (PRM)"
            }
            val pmrDesc = when (lang) {
                AppLanguage.AR -> "مداخل ممهدة، أبواب متسعة، ومرافق خاصة بالكراسي المتحركة"
                AppLanguage.FR -> "Rampes d'accès, portes larges et sanitaires adaptés"
                AppLanguage.EN -> "Ramps, wide doors, and adapted facilities"
            }
            FilterToggleRow(
                title = pmrTitle,
                description = pmrDesc,
                icon = Icons.AutoMirrored.Filled.Accessible,
                iconColor = Color(0xFF0077B6),
                checked = filterState.accessiblePmrOnly,
                onCheckedChange = { onToggleAccessible() },
                testTag = "switch_filter_accessible"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 2. Cleanliness
            val cleanSheetTitle = when (lang) {
                AppLanguage.AR -> "مستوى نظافة ممتاز ومضمون"
                AppLanguage.FR -> "Excellente propreté garantie"
                AppLanguage.EN -> "High cleanliness guaranteed"
            }
            val cleanSheetDesc = when (lang) {
                AppLanguage.AR -> "تقييم نظافة 4.2★ فما فوق مع توفر ماء دائم ومواد تنظيف"
                AppLanguage.FR -> "Note 4.2★+ avec eau courante et savon permanents"
                AppLanguage.EN -> "Rating 4.2★+ with continuous running water and soap"
            }
            FilterToggleRow(
                title = cleanSheetTitle,
                description = cleanSheetDesc,
                icon = Icons.Default.CleaningServices,
                iconColor = Color(0xFF2E7D32),
                checked = filterState.highCleanlinessOnly,
                onCheckedChange = { onToggleCleanliness() },
                testTag = "switch_filter_cleanliness"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 3. Changing Tables
            val changingSheetTitle = when (lang) {
                AppLanguage.AR -> "طاولات تغيير حفاضات الأطفال"
                AppLanguage.FR -> "Table à langer pour bébés"
                AppLanguage.EN -> "Baby changing tables"
            }
            val changingSheetDesc = when (lang) {
                AppLanguage.AR -> "مخصصة لرعاية الرضع وتغيير الحفاضات للأمهات والعائلات"
                AppLanguage.FR -> "Espace dédié pour les parents et nourrissons"
                AppLanguage.EN -> "Dedicated space for infants and parents"
            }
            FilterToggleRow(
                title = changingSheetTitle,
                description = changingSheetDesc,
                icon = Icons.Default.ChildCare,
                iconColor = Color(0xFFE91E63),
                checked = filterState.changingTableOnly,
                onCheckedChange = { onToggleChangingTable() },
                testTag = "switch_filter_changing_table"
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Section 2: Extra Amenities
            val sec2Title = when (lang) {
                AppLanguage.AR -> "الخدمات والمرافق الإضافية"
                AppLanguage.FR -> "Services et équipements additionnels"
                AppLanguage.EN -> "Additional services & amenities"
            }
            Text(
                text = sec2Title,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            val freeTitle = when (lang) {
                AppLanguage.AR -> "دخول مجاني بالكامل"
                AppLanguage.FR -> "Accès 100% gratuit"
                AppLanguage.EN -> "100% Free Access"
            }
            val freeDesc = when (lang) {
                AppLanguage.AR -> "مرافق بدون أي رسوم دفع (0 دج)"
                AppLanguage.FR -> "Sanitaires sans frais (0 DZD)"
                AppLanguage.EN -> "Facilities with no fees (0 DZD)"
            }
            FilterToggleRow(
                title = freeTitle,
                description = freeDesc,
                icon = Icons.Default.AttachMoney,
                iconColor = SuccessGreen,
                checked = filterState.freeOnly,
                onCheckedChange = { onToggleFreeOnly() },
                testTag = "switch_filter_free"
            )

            Spacer(modifier = Modifier.height(8.dp))

            val womenDesc = when (lang) {
                AppLanguage.AR -> "دورات مياه وقاعات وضوء مستقلة خاصة بالنساء"
                AppLanguage.FR -> "Sanitaires et ablutions séparés pour femmes"
                AppLanguage.EN -> "Separate restrooms and ablutions for women"
            }
            FilterToggleRow(
                title = AppStrings.womenSection(lang),
                description = womenDesc,
                icon = Icons.Default.Wc,
                iconColor = Color(0xFF9C27B0),
                checked = filterState.womenSectionOnly,
                onCheckedChange = { onToggleWomenOnly() },
                testTag = "switch_filter_women"
            )

            Spacer(modifier = Modifier.height(8.dp))

            val wuduTitle = when (lang) {
                AppLanguage.AR -> "أحواض وضوء مهيأة"
                AppLanguage.FR -> "Espace ablutions aménagé"
                AppLanguage.EN -> "Equipped wudu area"
            }
            val wuduDesc = when (lang) {
                AppLanguage.AR -> "صنابير ومقاعد مخصصة للوضوء والطهارة"
                AppLanguage.FR -> "Robinets et sièges dédiés aux ablutions"
                AppLanguage.EN -> "Taps and seating dedicated to ablution"
            }
            FilterToggleRow(
                title = wuduTitle,
                description = wuduDesc,
                icon = Icons.Default.WaterDrop,
                iconColor = WaterBlue,
                checked = filterState.wuduOnly,
                onCheckedChange = { onToggleWuduOnly() },
                testTag = "switch_filter_wudu"
            )

            Spacer(modifier = Modifier.height(8.dp))

            val showerDesc = when (lang) {
                AppLanguage.AR -> "حمامات أو محطات تحتوي على كبائن دوش"
                AppLanguage.FR -> "Douches individuelles avec eau chaude"
                AppLanguage.EN -> "Individual showers with hot water"
            }
            FilterToggleRow(
                title = AppStrings.hotShower(lang),
                description = showerDesc,
                icon = Icons.Default.Shower,
                iconColor = Color(0xFFB07D62),
                checked = filterState.showerOnly,
                onCheckedChange = { onToggleShowerOnly() },
                testTag = "switch_filter_shower"
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Section 3: Facility Category Visibility
            val sec3Title = when (lang) {
                AppLanguage.AR -> "فئات المرافق على الخريطة"
                AppLanguage.FR -> "Catégories visibles sur la carte"
                AppLanguage.EN -> "Visible categories on map"
            }
            Text(
                text = sec3Title,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val mosquesChipLabel = when (lang) {
                    AppLanguage.AR -> "المساجد والمصليات"
                    AppLanguage.FR -> "Mosquées"
                    AppLanguage.EN -> "Mosques"
                }
                FilterChip(
                    selected = filterState.showMosques,
                    onClick = onToggleMosques,
                    label = { Text(mosquesChipLabel) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Mosque,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MosqueEmerald
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                val restAreasChipLabel = when (lang) {
                    AppLanguage.AR -> "الاستراحات"
                    AppLanguage.FR -> "Aires de repos"
                    AppLanguage.EN -> "Rest Areas"
                }
                FilterChip(
                    selected = filterState.showRestAreas,
                    onClick = onToggleRestAreas,
                    label = { Text(restAreasChipLabel) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocalGasStation,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = NaftalFlame
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                val publicToiletsChipLabel = when (lang) {
                    AppLanguage.AR -> "دورات المياه"
                    AppLanguage.FR -> "Sanitaires"
                    AppLanguage.EN -> "Restrooms"
                }
                FilterChip(
                    selected = filterState.showPublicToilets,
                    onClick = onTogglePublicToilets,
                    label = { Text(publicToiletsChipLabel) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Wc,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = TealPrimary
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Apply Button
            val applyBtnText = when (lang) {
                AppLanguage.AR -> "عرض $matchCount نقطة راحة مطابقة على الخريطة"
                AppLanguage.FR -> "Afficher $matchCount résultats sur la carte"
                AppLanguage.EN -> "Show $matchCount matching results on map"
            }
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RahaGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("map_filter_apply_button")
            ) {
                Text(
                    text = applyBtnText,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun FilterToggleRow(
    title: String,
    description: String,
    icon: ImageVector,
    iconColor: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = { onCheckedChange(!checked) },
        shape = RoundedCornerShape(16.dp),
        color = if (checked) iconColor.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(
            width = if (checked) 1.5.dp else 1.dp,
            color = if (checked) iconColor else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (checked) iconColor else MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (checked) Color.White else iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = iconColor
                )
            )
        }
    }
}
