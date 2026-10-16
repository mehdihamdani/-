package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AlgerianWilayas
import com.example.data.model.AppLanguage
import com.example.data.model.RestroomType
import com.example.ui.theme.MosqueEmerald
import com.example.ui.theme.TealPrimary
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRestroomDialog(
    onDismiss: () -> Unit,
    onSubmit: (
        name: String,
        nameFr: String,
        wilaya: String,
        commune: String,
        type: RestroomType,
        isFree: Boolean,
        priceDzd: Int,
        hasWater: Boolean,
        hasSoapPaper: Boolean,
        isAccessiblePmr: Boolean,
        hasWomenSection: Boolean,
        hasShower: Boolean,
        hasWudu: Boolean,
        hasChangingTable: Boolean,
        openingHours: String,
        address: String,
        notes: String,
        donationCcp: String
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var nameFr by remember { mutableStateOf("") }
    var wilaya by remember { mutableStateOf("الجزائر العاصمة") }
    var commune by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(RestroomType.MOSQUE) }
    var isFree by remember { mutableStateOf(true) }
    var priceText by remember { mutableStateOf("0") }
    var hasWater by remember { mutableStateOf(true) }
    var hasSoapPaper by remember { mutableStateOf(true) }
    var isAccessiblePmr by remember { mutableStateOf(false) }
    var hasWomenSection by remember { mutableStateOf(true) }
    var hasShower by remember { mutableStateOf(false) }
    var hasWudu by remember { mutableStateOf(true) }
    var hasChangingTable by remember { mutableStateOf(false) }
    var openingHours by remember { mutableStateOf("أوقات الصلوات") }
    var address by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var donationCcp by remember { mutableStateOf("") }

    var typeMenuExpanded by remember { mutableStateOf(false) }
    var wilayaMenuExpanded by remember { mutableStateOf(false) }
    var wilayaSearchQuery by remember { mutableStateOf("") }
    val algerianWilayas = AlgerianWilayas.all69Wilayas
    val lang = LocalAppLanguage.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("add_restroom_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AddLocationAlt,
                            contentDescription = null,
                            tint = TealPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.addDialogTitle(lang),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = AppStrings.cancel(lang))
                    }
                }

                Text(
                    text = AppStrings.addDialogSubtitle(lang),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Name
                val namePlaceholder = when (lang) {
                    AppLanguage.AR -> "اسم المكان (مثال: مسجد الفرقان أو حمام النور) *"
                    AppLanguage.FR -> "Nom du lieu (ex : Mosquée El Forkane ou Hammam Ennour) *"
                    AppLanguage.EN -> "Facility name (e.g. El Forkane Mosque or Ennour Hammam) *"
                }
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(namePlaceholder) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = typeMenuExpanded,
                    onExpandedChange = { typeMenuExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedType.getDisplayName(lang),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(AppStrings.facilityTypeLabel(lang)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeMenuExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = typeMenuExpanded,
                        onDismissRequest = { typeMenuExpanded = false }
                    ) {
                        RestroomType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.getDisplayName(lang)) },
                                onClick = {
                                    selectedType = type
                                    typeMenuExpanded = false
                                    if (type == RestroomType.HAMMAM) {
                                        hasShower = true
                                        isFree = false
                                        priceText = "200"
                                    } else if (type == RestroomType.MOSQUE || type == RestroomType.MUSALLA) {
                                        hasWudu = true
                                        isFree = true
                                        priceText = "0"
                                        openingHours = if (lang == AppLanguage.AR) "أوقات الصلوات" else "Heures de prière"
                                    } else if (type == RestroomType.PRIVATE_COMMERCIAL) {
                                        hasShower = false
                                        isFree = false
                                        priceText = "20"
                                        hasWater = true
                                        hasSoapPaper = true
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Wilaya Dropdown (69 Algerian Wilayas with search)
                ExposedDropdownMenuBox(
                    expanded = wilayaMenuExpanded,
                    onExpandedChange = { wilayaMenuExpanded = it }
                ) {
                    val currentWilayaDisplay = AlgerianWilayas.getLocalizedWilayaName(wilaya, lang)
                    OutlinedTextField(
                        value = currentWilayaDisplay,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(AppStrings.wilayaLabel(lang)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = wilayaMenuExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .testTag("wilaya_dropdown_field")
                    )
                    ExposedDropdownMenu(
                        expanded = wilayaMenuExpanded,
                        onDismissRequest = {
                            wilayaMenuExpanded = false
                            wilayaSearchQuery = ""
                        }
                    ) {
                        // Search box inside dropdown
                        val searchWilayaHint = when (lang) {
                            AppLanguage.AR -> "ابحث برقم أو اسم الولاية..."
                            AppLanguage.FR -> "Rechercher par code ou nom de wilaya..."
                            AppLanguage.EN -> "Search by code or province name..."
                        }
                        OutlinedTextField(
                            value = wilayaSearchQuery,
                            onValueChange = { wilayaSearchQuery = it },
                            placeholder = { Text(searchWilayaHint) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            trailingIcon = {
                                if (wilayaSearchQuery.isNotEmpty()) {
                                    IconButton(onClick = { wilayaSearchQuery = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        )

                        val filteredWilayas = if (wilayaSearchQuery.isBlank()) {
                            algerianWilayas
                        } else {
                            val q = wilayaSearchQuery.trim().lowercase()
                            algerianWilayas.filter {
                                it.fullSearchableText.contains(q)
                            }
                        }

                        if (filteredWilayas.isEmpty()) {
                            val noMatchText = when (lang) {
                                AppLanguage.AR -> "لا توجد ولاية مطابقة"
                                AppLanguage.FR -> "Aucune wilaya correspondante"
                                AppLanguage.EN -> "No matching province"
                            }
                            DropdownMenuItem(
                                text = { Text(noMatchText, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                                onClick = {}
                            )
                        } else {
                            filteredWilayas.forEach { w ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            val primaryName = if (lang == AppLanguage.AR) w.displayName else "${w.code} - ${w.frenchName}"
                                            val secondaryName = if (lang == AppLanguage.AR) w.frenchName else w.arabicName
                                            Text(
                                                text = primaryName,
                                                fontWeight = if (wilaya == w.arabicName) FontWeight.Bold else FontWeight.Normal
                                            )
                                            Text(
                                                text = secondaryName,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    },
                                    onClick = {
                                        wilaya = w.arabicName
                                        wilayaMenuExpanded = false
                                        wilayaSearchQuery = ""
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Commune
                OutlinedTextField(
                    value = commune,
                    onValueChange = { commune = it },
                    label = { Text(AppStrings.communeLabel(lang)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Pricing row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isFree,
                        onCheckedChange = {
                            isFree = it
                            if (it) priceText = "0"
                        }
                    )
                    Text(AppStrings.free(lang), style = MaterialTheme.typography.bodyMedium)

                    if (!isFree) {
                        Spacer(modifier = Modifier.width(16.dp))
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { priceText = it },
                            label = { Text(AppStrings.priceLabel(lang)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Checkboxes for Amenities
                Text(
                    text = AppStrings.availableAmenities(lang),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasWater, onCheckedChange = { hasWater = it })
                    Text("💧 " + AppStrings.waterAvailable(lang), style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasSoapPaper, onCheckedChange = { hasSoapPaper = it })
                    Text("🧼 " + AppStrings.filterSoapPaper(lang), style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasWomenSection, onCheckedChange = { hasWomenSection = it })
                    Text("🧕 " + AppStrings.womenSection(lang), style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isAccessiblePmr, onCheckedChange = { isAccessiblePmr = it })
                    Text("♿ " + AppStrings.accessiblePmr(lang), style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasWudu, onCheckedChange = { hasWudu = it })
                    Text("🕌 " + AppStrings.wuduAvailable(lang), style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasShower, onCheckedChange = { hasShower = it })
                    Text("🚿 " + AppStrings.hotShower(lang), style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasChangingTable, onCheckedChange = { hasChangingTable = it })
                    Text("👶 " + AppStrings.babyChanging(lang), style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Opening Hours
                OutlinedTextField(
                    value = openingHours,
                    onValueChange = { openingHours = it },
                    label = { Text(AppStrings.openingHours(lang)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(AppStrings.notes(lang)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Donation Current Account (CCP / BaridiMob)
                val ccpPlaceholder = when (lang) {
                    AppLanguage.AR -> "مثال: 0021458798 مفتاح 45 أو RIP BaridiMob"
                    AppLanguage.FR -> "Ex : 0021458798 clé 45 ou RIP BaridiMob"
                    AppLanguage.EN -> "e.g. 0021458798 key 45 or BaridiMob RIP"
                }
                OutlinedTextField(
                    value = donationCcp,
                    onValueChange = { donationCcp = it },
                    label = { Text(AppStrings.mosqueCcpNotice(lang)) },
                    placeholder = { Text(ccpPlaceholder) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.VolunteerActivism,
                            contentDescription = null,
                            tint = MosqueEmerald
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("donation_ccp_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Buttons
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
                            if (name.isNotBlank()) {
                                onSubmit(
                                    name,
                                    nameFr,
                                    wilaya,
                                    commune.ifBlank { if (lang == AppLanguage.AR) "وسط المدينة" else "Centre-ville" },
                                    selectedType,
                                    isFree,
                                    priceText.toIntOrNull() ?: 0,
                                    hasWater,
                                    hasSoapPaper,
                                    isAccessiblePmr,
                                    hasWomenSection,
                                    hasShower,
                                    hasWudu,
                                    hasChangingTable,
                                    openingHours,
                                    address,
                                    notes,
                                    donationCcp
                                )
                            }
                        },
                        enabled = name.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        modifier = Modifier.testTag("confirm_add_restroom_button")
                    ) {
                        Text(AppStrings.submitContribution(lang))
                    }
                }
            }
        }
    }
}
