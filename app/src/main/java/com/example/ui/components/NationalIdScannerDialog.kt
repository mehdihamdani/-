package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.UserProfileData
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.RahaBlue
import com.example.ui.theme.RahaGreen
import com.example.ui.theme.TealPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Result data extracted from the Algerian Biometric National ID Card.
 */
data class ExtractedIdCardData(
    val fullNameAr: String,
    val fullNameFr: String,
    val ninNumber: String,
    val dateOfBirth: String,
    val wilaya: String,
    val commune: String,
    val bloodGroup: String
)

/**
 * Modal dialog for scanning an Algerian National ID Card (بطاقة التعريف الوطنية البيومترية).
 * Performs simulated OCR / biometric extraction and automatically populates the user profile.
 */
@Composable
fun NationalIdScannerDialog(
    onDismiss: () -> Unit,
    onDataExtracted: (ExtractedIdCardData) -> Unit
) {
    var isScanning by remember { mutableStateOf(false) }
    var scanCompleted by remember { mutableStateOf(false) }
    var scanStatusText by remember { mutableStateOf("وجّه كاميرا الهاتف نحو بطاقة التعريف الوطنية") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val extractedResult = remember {
        ExtractedIdCardData(
            fullNameAr = "حمداوي مهدي",
            fullNameFr = "HAMDANI MEHDI",
            ninNumber = "199416010023456789",
            dateOfBirth = "12/05/1994",
            wilaya = "16 - الجزائر العاصمة",
            commune = "الجزائر الوسطى",
            bloodGroup = "O+"
        )
    }

    // Photo picker launcher (Zero-permission Android Photo Picker)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            startScanProcess(
                onStatusChange = { scanStatusText = it },
                onComplete = { scanCompleted = true; isScanning = false },
                onStart = { isScanning = true }
            )
        }
    }

    // Scanner beam animation
    val infiniteTransition = rememberInfiniteTransition(label = "scanner_laser")
    val laserOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 180f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_y"
    )

    fun triggerLiveScan() {
        coroutineScope.launch {
            isScanning = true
            scanStatusText = "جاري الكشف عن بطاقة التعريف البيومترية..."
            delay(900)
            scanStatusText = "جاري قراءة الشفرة الضوئية (MRZ) ورقم NIN..."
            delay(1100)
            scanStatusText = "استخراج الاسم واللقب والولاية..."
            delay(800)
            isScanning = false
            scanCompleted = true
            scanStatusText = "تم استخراج البيانات بنجاح! ✅"
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 24.dp)
                .testTag("national_id_scanner_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(RahaGreen.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DocumentScanner,
                                contentDescription = null,
                                tint = RahaGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "مسح بطاقة التعريف الوطنية",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "الجمهورية الجزائرية الديمقراطية الشعبية",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Interactive Scanner Frame Viewfinder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF1A1C1E))
                        .border(2.dp, if (scanCompleted) RahaGreen else TealPrimary, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // ID Card Silhouette Representation
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🇩🇿 CARTE NATIONALE D'IDENTITÉ",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Middle silhouette
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp, 75.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.White.copy(alpha = 0.15f))
                                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("صورة 👤", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Box(
                                    modifier = Modifier
                                        .size(140.dp, 10.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(Color.White.copy(alpha = 0.25f))
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(110.dp, 8.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(Color.White.copy(alpha = 0.2f))
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(150.dp, 8.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(Color.White.copy(alpha = 0.18f))
                                )
                            }
                        }

                        // Bottom MRZ lines
                        Text(
                            text = "<< DZA199416010023456789 <<<<<<<<<< HAMDANI<MEHDI <<<<",
                            color = Color(0xFF64B5F6).copy(alpha = 0.8f),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Active animated laser scan line
                    if (isScanning) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .offset(y = laserOffset.dp - 90.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            Color.Transparent,
                                            Color(0xFF00E676),
                                            Color(0xFF00F2FE),
                                            Color(0xFF00E676),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )
                    }

                    // Completed overlay
                    if (scanCompleted) {
                        Surface(
                            shape = CircleShape,
                            color = RahaGreen,
                            shadowElevation = 6.dp,
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scan status text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isScanning) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = TealPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = scanStatusText,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = if (scanCompleted) RahaGreen else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Scan Trigger Buttons (Camera vs Photo Picker)
                if (!scanCompleted) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { triggerLiveScan() },
                            enabled = !isScanning,
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_trigger_camera_scan")
                        ) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("مسح بالكاميرا")
                        }

                        OutlinedButton(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            enabled = !isScanning,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_pick_id_photo")
                        ) {
                            Icon(imageVector = Icons.Default.Image, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("اختيار صورة")
                        }
                    }
                }

                // Extracted Information Card (Shown when scan is complete)
                if (scanCompleted) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = RahaGreen.copy(alpha = 0.08f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = RahaGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "البيانات المستخرجة من البطاقة الوطنية:",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = RahaGreen
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            ExtractedDataRow(label = "الاسم واللقب:", value = "${extractedResult.fullNameAr} (${extractedResult.fullNameFr})")
                            ExtractedDataRow(label = "رقم التعريف الوطني (NIN):", value = extractedResult.ninNumber)
                            ExtractedDataRow(label = "تاريخ الميلاد:", value = extractedResult.dateOfBirth)
                            ExtractedDataRow(label = "الولاية والبلدية:", value = "${extractedResult.wilaya} • ${extractedResult.commune}")
                            ExtractedDataRow(label = "فصيلة الدم:", value = extractedResult.bloodGroup)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Confirm and Populate Button
                    Button(
                        onClick = {
                            onDataExtracted(extractedResult)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RahaGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_confirm_extracted_id_data")
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("تأكيد واستيراد البيانات تلقائياً للملف الشخصي", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

private fun startScanProcess(
    onStatusChange: (String) -> Unit,
    onComplete: () -> Unit,
    onStart: () -> Unit
) {
    // Helper used for photo picker completion
    onStart()
    onStatusChange("جاري معالجة صورة بطاقة التعريف الوطنية...")
    onComplete()
}

@Composable
private fun ExtractedDataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
