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
import androidx.compose.material.icons.filled.Close
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
import com.example.data.model.RestroomType
import com.example.ui.theme.TealPrimary

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
        notes: String
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

    var typeMenuExpanded by remember { mutableStateOf(false) }
    var wilayaMenuExpanded by remember { mutableStateOf(false) }

    val algerianWilayas = listOf(
        "الجزائر العاصمة", "وهران", "قسنطينة", "سطيف", "عنابة", "البليدة", "تلمسان",
        "تيزي وزو", "بجاية", "باتنة", "الشلف", "بسكرة", "سكيكدة", "سيدي بلعباس",
        "مستغانم", "المسيلة", "معسكر", "ورقلة", "بومرداس", "تيبازة", "ميلة", "عين الدفلى"
    )

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
                            text = "إضافة مرحاض أو حمام جديد",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "إلغاء")
                    }
                }

                Text(
                    text = "ساهم في إثراء الدليل ومساعدة المواطنين والمسافرين في الجزائر.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("اسم المكان (مثال: مسجد الفرقان أو حمام النور) *") },
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
                        value = selectedType.arabicName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("نوع المرفق *") },
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
                                text = { Text(type.arabicName) },
                                onClick = {
                                    selectedType = type
                                    typeMenuExpanded = false
                                    if (type == RestroomType.HAMMAM) {
                                        hasShower = true
                                        isFree = false
                                        priceText = "200"
                                    } else if (type == RestroomType.MOSQUE) {
                                        hasWudu = true
                                        isFree = true
                                        priceText = "0"
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Wilaya Dropdown
                ExposedDropdownMenuBox(
                    expanded = wilayaMenuExpanded,
                    onExpandedChange = { wilayaMenuExpanded = it }
                ) {
                    OutlinedTextField(
                        value = wilaya,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("الولاية *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = wilayaMenuExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = wilayaMenuExpanded,
                        onDismissRequest = { wilayaMenuExpanded = false }
                    ) {
                        algerianWilayas.forEach { w ->
                            DropdownMenuItem(
                                text = { Text(w) },
                                onClick = {
                                    wilaya = w
                                    wilayaMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Commune
                OutlinedTextField(
                    value = commune,
                    onValueChange = { commune = it },
                    label = { Text("البلدية أو الحي (مثال: بئر مراد رايس)") },
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
                    Text("دخول مجاني", style = MaterialTheme.typography.bodyMedium)

                    if (!isFree) {
                        Spacer(modifier = Modifier.width(16.dp))
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { priceText = it },
                            label = { Text("السعر (دج)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Checkboxes for Amenities
                Text(
                    text = "المرافق المتوفرة:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasWater, onCheckedChange = { hasWater = it })
                    Text("ماء جاري متوفر", style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasSoapPaper, onCheckedChange = { hasSoapPaper = it })
                    Text("صابون ومجفف أيدي", style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasWomenSection, onCheckedChange = { hasWomenSection = it })
                    Text("قسم مخصص للنساء", style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isAccessiblePmr, onCheckedChange = { isAccessiblePmr = it })
                    Text("مهيأ لذوي الاحتياجات الخاصة", style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasWudu, onCheckedChange = { hasWudu = it })
                    Text("مرافق وضوء", style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasShower, onCheckedChange = { hasShower = it })
                    Text("دوش / استحمام ساخن", style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasChangingTable, onCheckedChange = { hasChangingTable = it })
                    Text("طاولة تغيير حفاضات للأطفال", style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Opening Hours
                OutlinedTextField(
                    value = openingHours,
                    onValueChange = { openingHours = it },
                    label = { Text("أوقات العمل (مثال: 24/7 أو 08:00 - 20:00)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملاحظات إضافية حول النظافة أو الوصف") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("إلغاء")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onSubmit(
                                    name,
                                    nameFr,
                                    wilaya,
                                    commune.ifBlank { "وسط المدينة" },
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
                                    notes
                                )
                            }
                        },
                        enabled = name.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        modifier = Modifier.testTag("confirm_add_restroom_button")
                    ) {
                        Text("نشر وإضافة للمجتمع")
                    }
                }
            }
        }
    }
}
