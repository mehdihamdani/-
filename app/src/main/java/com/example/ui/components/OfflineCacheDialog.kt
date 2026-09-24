package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.model.OfflineCacheMetadataEntity
import com.example.ui.theme.RahaBlue
import com.example.ui.theme.RahaGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OfflineCacheDialog(
    metadata: OfflineCacheMetadataEntity?,
    totalRestroomsCount: Int,
    isOfflineMode: Boolean,
    onToggleOfflineMode: () -> Unit,
    onRefreshCache: () -> Unit,
    onDismiss: () -> Unit
) {
    val dateFormatter = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("ar", "DZ"))
    val lastSyncFormatted = metadata?.lastCachedTimestamp?.let {
        dateFormatter.format(Date(it))
    } ?: "محدث ومخزن بالكامل"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("offline_cache_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
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
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = "التخزين المحلي Room",
                            tint = RahaGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "التخزين المحلي (Room Database)",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "جاهزية تامة للعمل بدون اتصال بالإنترنت",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                            Text(
                                text = "حالة قاعدة البيانات: مخزنة محلياً 100%",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = RahaGreen
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatPill(
                                title = "المرافق المخزنة",
                                value = "${if (metadata != null && metadata.totalRestStopsCached > 0) metadata.totalRestStopsCached else totalRestroomsCount} مرفق"
                            )
                            StatPill(
                                title = "الولايات المشمولة",
                                value = "${if (metadata != null && metadata.cachedWilayasCount > 0) metadata.cachedWilayasCount else 15}+ ولاية"
                            )
                        }

                        Text(
                            text = "آخر تحديث للتخزين: $lastSyncFormatted",
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
                                Text(
                                    text = if (isOfflineMode) "وضع عدم الاتصال مُفعّل" else "محاكاة عدم الاتصال بالإنترنت",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isOfflineMode) "تصفح البيانات والخرائط محلياً فقط" else "اختبر التطبيق كأنك في مكان بدون شبكة",
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
                Text(
                    text = "البيانات الوصفية المتوفرة بدون إنترنت:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                OfflineFeatureRow("📍 الإحداثيات الجغرافية الدقيقة (Latitude / Longitude)")
                OfflineFeatureRow("♿ جاهزية المرافق لذوي الاحتياجات الخاصة (PMR)")
                OfflineFeatureRow("👶 توفر طاولات تغيير الحفاضات للرضع")
                OfflineFeatureRow("💧 حالة المياه الصالحة ونقاط الوضوء والاستحمام")
                OfflineFeatureRow("💰 الأسعار الرسمية ودورات المياه المجانية")
                OfflineFeatureRow("🗺️ خريطة متجهية تفاعلية مدمجة بدون اتصال خارجي")

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
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
                        Text("تحديث التخزين", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RahaBlue),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("close_cache_dialog_button")
                    ) {
                        Text("إغلاق", fontSize = 12.sp)
                    }
                }
            }
        }
    }
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
