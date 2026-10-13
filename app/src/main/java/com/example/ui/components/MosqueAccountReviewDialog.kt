package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FirestoreMosqueDonationAccount
import com.example.ui.theme.AlertRed
import com.example.ui.theme.RahaBlue
import com.example.ui.theme.RahaGreen
import com.example.ui.theme.TealPrimary

/**
 * Administrator-only management dialog for reviewing pending mosque donation accounts.
 *
 * Requirements & Invariants:
 * 1. Read-only for public users; accessible strictly to administrators.
 * 2. Displays "Pending verification" (قيد المراجعة والتدقيق) and the source link.
 * 3. Does NOT present accounts as payment destinations or donation targets.
 * 4. Shows NO financial progress, target amounts, or collected amounts.
 * 5. Not linked to ablution facilities or map locations.
 * 6. Allows administrators to attach account documents, record beneficiary legal name,
 *    and confirm committee consent.
 * 7. Uploading a document does NOT automatically enable donations (donations_enabled remains false).
 */
@Composable
fun MosqueAccountReviewDialog(
    accounts: List<FirestoreMosqueDonationAccount>,
    isAdmin: Boolean,
    onDismiss: () -> Unit,
    onSaveAudit: (
        accountId: String,
        holderName: String?,
        consentConfirmed: Boolean,
        docName: String?,
        notes: String
    ) -> Unit
) {
    val context = LocalContext.current
    var selectedAccountForAudit by remember { mutableStateOf<FirestoreMosqueDonationAccount?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFFF3E0),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = Color(0xFFE65100),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "تدقيق حسابات التبرع للمساجد",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "قسم مخصص للإدارة للمراجعة فقط • غير متاح للعامة",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFE65100)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_mosque_review_dialog")
            ) {
                // Regulatory Notice Banner
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF8E1),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD54F)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Color(0xFFF57F17),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "هذه الحسابات نُشرت عبر الإنترنت ولم يتم تأكيدها مباشرة من لجان المساجد أو بريد الجزائر. الحسابات مضافة للمراجعة الإدارية فقط، والتبرعات غير مفعلة.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = Color(0xFF5D4037)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (!isAdmin) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AlertRed.copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = AlertRed)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "عذراً، هذا القسم محمي ومخصص لمديري النظام فقط. الحسابات غير معروضة للمستخدمين العاديين.",
                                style = MaterialTheme.typography.bodySmall,
                                color = AlertRed
                            )
                        }
                    }
                } else if (accounts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "لا توجد حسابات مسجلة للمراجعة حالياً.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(380.dp)
                    ) {
                        items(accounts, key = { it.id }) { item ->
                            MosqueAccountReviewItemCard(
                                account = item,
                                onOpenSource = {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.source))
                                        context.startActivity(intent)
                                    } catch (_: Exception) {
                                        Toast.makeText(context, "تعذر فتح الرابط", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onAuditClick = {
                                    selectedAccountForAudit = item
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
            ) {
                Text("إغلاق")
            }
        }
    )

    // Audit and Document Attachment Dialog
    if (selectedAccountForAudit != null) {
        AuditEditSubDialog(
            account = selectedAccountForAudit!!,
            onDismiss = { selectedAccountForAudit = null },
            onConfirm = { holderName, consent, docName, notes ->
                onSaveAudit(
                    selectedAccountForAudit!!.id,
                    holderName,
                    consent,
                    docName,
                    notes
                )
                selectedAccountForAudit = null
                Toast.makeText(context, "تم حفظ بيانات التدقيق بنجاح ✓ (التبرعات تظل غير مفعلة)", Toast.LENGTH_LONG).show()
            }
        )
    }
}

@Composable
private fun MosqueAccountReviewItemCard(
    account: FirestoreMosqueDonationAccount,
    onOpenSource: () -> Unit,
    onAuditClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("mosque_account_card_${account.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header: Name & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = account.mosque_name,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "📍 ${account.location_description}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFFF3E0),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB74D))
                ) {
                    Text(
                        text = "قيد المراجعة والتدقيق",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        color = Color(0xFFE65100),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))

            // Account Details (Leading zeros preserved strictly as strings)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "رقم الـ CCP:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = account.ccp,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }

                Column {
                    Text(
                        text = "المفتاح (Clé):",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = account.account_key,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }

                if (!account.rip.isNullOrBlank()) {
                    Column {
                        Text(
                            text = "رقم الـ RIP:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = account.rip,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Metadata row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "تاريخ فحص المصدر: ${account.source_checked_at}",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Safety Invariant Tag
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFEEEEEE)
                ) {
                    Text(
                        text = if (!account.donations_enabled) "التبرعات غير مفعلة 🔒" else "مفعل",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Color(0xFF616161),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            if (!account.account_holder_name.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "اسم المستفيد القانوني: ${account.account_holder_name}",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = RahaGreen
                )
            }

            if (!account.document_name.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "المستند المرفق: 📄 ${account.document_name}",
                    style = MaterialTheme.typography.labelSmall,
                    color = RahaBlue
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onOpenSource,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("btn_open_source_${account.id}")
                ) {
                    Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "رابط المصدر", fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onAuditClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
                    modifier = Modifier.testTag("btn_audit_${account.id}")
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "تدقيق وإرفاق مستند", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun AuditEditSubDialog(
    account: FirestoreMosqueDonationAccount,
    onDismiss: () -> Unit,
    onConfirm: (
        holderName: String?,
        consentConfirmed: Boolean,
        documentName: String?,
        adminNotes: String
    ) -> Unit
) {
    var holderName by remember { mutableStateOf(account.account_holder_name ?: "") }
    var consentConfirmed by remember { mutableStateOf(account.committee_consent_confirmed) }
    var docName by remember { mutableStateOf(account.document_name ?: "") }
    var adminNotes by remember { mutableStateOf(account.admin_notes) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "تدقيق: ${account.mosque_name}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "الحساب: CCP ${account.ccp} / المفتاح ${account.account_key}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Legal Name
                OutlinedTextField(
                    value = holderName,
                    onValueChange = { holderName = it },
                    label = { Text("الاسم القانوني للمستفيد (الجمعية / اللجنة الدينية)") },
                    placeholder = { Text("مثال: الجمعية الدينية لمسجد...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_holder_name")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Document Attachment
                OutlinedTextField(
                    value = docName,
                    onValueChange = { docName = it },
                    label = { Text("المستند البنكي / كشف الـ CCP المرفق") },
                    placeholder = { Text("مثال: releve_compte_ccp_2026.pdf") },
                    leadingIcon = { Icon(Icons.Default.AttachFile, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_doc_name")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Committee Consent Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { consentConfirmed = !consentConfirmed }
                        .padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = consentConfirmed,
                        onCheckedChange = { consentConfirmed = it },
                        modifier = Modifier.testTag("checkbox_committee_consent")
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "تأكيد موافقة لجنة المسجد المعتمدة كتابياً",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Admin Notes
                OutlinedTextField(
                    value = adminNotes,
                    onValueChange = { adminNotes = it },
                    label = { Text("ملاحظات المدقق الإداري") },
                    placeholder = { Text("سجل تفاصيل المراجعة وجهة التأكيد...") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth().testTag("input_admin_notes")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFECEFF1),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🔒 تنبيه نظام: إرفاق المستند وتأكيد الموافقة لا يفعل التبرعات تلقائياً. يظل الحساب قيد المراجعة.",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Color(0xFF455A64),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        holderName.ifBlank { null },
                        consentConfirmed,
                        docName.ifBlank { null },
                        adminNotes
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                modifier = Modifier.testTag("btn_save_audit")
            ) {
                Text("حفظ بيانات التدقيق")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}
