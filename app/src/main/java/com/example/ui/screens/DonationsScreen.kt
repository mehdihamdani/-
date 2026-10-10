package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.ui.theme.RahaBlue
import com.example.ui.theme.RahaGreen
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.WaterBlue
import com.example.util.AppStrings

data class MosqueCharityProject(
    val id: String,
    val name: String,
    val wilaya: String,
    val commune: String,
    val projectNeed: String,
    val targetAmountDzd: Int,
    val collectedAmountDzd: Int,
    val baridiMobRip: String,
    val ccpNumber: String,
    val associationName: String
) {
    val progress: Float
        get() = (collectedAmountDzd.toFloat() / targetAmountDzd.toFloat()).coerceIn(0f, 1f)

    val remainingDzd: Int
        get() = (targetAmountDzd - collectedAmountDzd).coerceAtLeast(0)
}

/**
 * Screen dedicated to community donations:
 * 1. Supporting the DZ Restrooms ("راحة") platform (servers, maps, development).
 * 2. Sadaqah Jariyah for maintaining and sanitizing Mosque restrooms & ablution facilities across Algeria.
 */
@Composable
fun DonationsScreen(
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: App Support, 1: Mosque Facilities

    // App Donation State
    var selectedAmountDzd by remember { mutableIntStateOf(500) }
    var customAmountText by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("BARIDIMOB") } // BARIDIMOB, CCP, FLEXY
    var showAppDonationSuccessDialog by remember { mutableStateOf(false) }
    var showMosqueDonationDialog by remember { mutableStateOf<MosqueCharityProject?>(null) }
    var showMosqueThankYouDialog by remember { mutableStateOf(false) }

    val presetAmounts = listOf(200, 500, 1000, 2000, 5000)

    val registeredMosqueProjects = remember {
        listOf(
            MosqueCharityProject(
                id = "m1",
                name = "جامع الشهداء - المحمدية",
                wilaya = "الجزائر العاصمة",
                commune = "المحمدية",
                projectNeed = "صيانة وتجديد سخانات المياه الشتوية وأنابيب الوضوء",
                targetAmountDzd = 45000,
                collectedAmountDzd = 36000,
                baridiMobRip = "007 99999 0012345678 19",
                ccpNumber = "12345678 Clé 19",
                associationName = "لجنة مسجد الشهداء المعتمدة"
            ),
            MosqueCharityProject(
                id = "m2",
                name = "مسجد الفتح - سيدي بلعباس",
                wilaya = "سيدي بلعباس",
                commune = "وسط المدينة",
                projectNeed = "شراء صابون، معقمات ومواد تنظيف دورية لدورات المياه",
                targetAmountDzd = 20000,
                collectedAmountDzd = 14500,
                baridiMobRip = "007 99999 0023456789 22",
                ccpNumber = "23456789 Clé 22",
                associationName = "جمعية رعاية بيوت الله - سيدي بلعباس"
            ),
            MosqueCharityProject(
                id = "m3",
                name = "جامع القدس - السانية",
                wilaya = "وهران",
                commune = "السانية",
                projectNeed = "تهيئة ممر ودورة مياه مخصصة لذوي الهمم وكبار السن",
                targetAmountDzd = 60000,
                collectedAmountDzd = 41000,
                baridiMobRip = "007 99999 0034567890 31",
                ccpNumber = "34567890 Clé 31",
                associationName = "لجنة مسجد القدس - وهران"
            ),
            MosqueCharityProject(
                id = "m4",
                name = "مسجد الأمير عبد القادر",
                wilaya = "قسنطينة",
                commune = "قسنطينة",
                projectNeed = "تغيير خلاطات ومحابس مياه الوضوء وإصلاح التسربات",
                targetAmountDzd = 55000,
                collectedAmountDzd = 38000,
                baridiMobRip = "007 99999 0045678901 25",
                ccpNumber = "45678901 Clé 25",
                associationName = "لجنة أوقاف قسنطينة"
            ),
            MosqueCharityProject(
                id = "m5",
                name = "مسجد النور - سطيف",
                wilaya = "سطيف",
                commune = "سطيف",
                projectNeed = "شراء مضخة مياه وخزانات احتياطية لضمان استمرار المياه",
                targetAmountDzd = 35000,
                collectedAmountDzd = 27000,
                baridiMobRip = "007 99999 0056789012 19",
                ccpNumber = "56789012 Clé 19",
                associationName = "لجنة رعاية مسجد النور"
            )
        )
    }

    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "تم نسخ $label بنجاح ✓", Toast.LENGTH_SHORT).show()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("donations_screen"),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                TealPrimary,
                                RahaBlue.copy(alpha = 0.85f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 8.dp,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.VolunteerActivism,
                                contentDescription = null,
                                tint = TealPrimary,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = AppStrings.donationTitle(currentLanguage),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (currentLanguage == AppLanguage.AR)
                            "«الطهور شطر الإيمان» • ساهم في دعم التطبيق وصيانة مرافق الطهارة والمساجد في الجزائر 🇩🇿"
                        else if (currentLanguage == AppLanguage.FR)
                            "Soutenez l'application et l'entretien des installations sanitaires des mosquées"
                        else
                            "Support the app and maintain mosque sanitization facilities across Algeria",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }

        // Sub-tabs (تبرع للتطبيق | تبرع للمساجد)
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = TealPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = TealPrimary
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text(AppStrings.tabAppDonation(currentLanguage), fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.PhoneAndroid, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.testTag("tab_donation_app")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text(AppStrings.tabMosqueDonation(currentLanguage), fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Mosque, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.testTag("tab_donation_mosques")
                )
            }
        }

        if (selectedTab == 0) {
            // ==========================================
            // TAB 0: SUPPORT RAHA APP (تطبيق راحة)
            // ==========================================
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = Color(0xFFE11D48),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "لماذا يحتاج تطبيق «راحة» لدعمكم؟",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "تطبيق «راحة» مبادرة جزائرية مجانية 100% بدون إعلانات مزعجة. تبرعكم الرمزي يساهم مباشرة في تغطية تكاليف الخوادم السحابية، التحقق الميداني من المراحيض، وتطوير الخرائط التفاعلية بدون إنترنت لخدمة جميع المسافرين.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "اختر مبلغ المساهمة (دج):",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Preset Amounts Chips Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            presetAmounts.forEach { amount ->
                                val isSelected = selectedAmountDzd == amount && customAmountText.isEmpty()
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) TealPrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    shadowElevation = if (isSelected) 3.dp else 0.dp,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            selectedAmountDzd = amount
                                            customAmountText = ""
                                        }
                                        .testTag("donation_amount_$amount")
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    ) {
                                        Text(
                                            text = "$amount",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Custom Amount Input
                        OutlinedTextField(
                            value = customAmountText,
                            onValueChange = {
                                customAmountText = it
                                val parsed = it.toIntOrNull()
                                if (parsed != null) selectedAmountDzd = parsed
                            },
                            label = { Text("أو أدخل مبلغاً مخصصاً (دج)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "طرق التبرع المتاحة بالجزائر:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // BaridiMob Option Card
                        PaymentMethodCard(
                            title = "بريدي موب (BaridiMob)",
                            accountHolder = "مهدي حمداني (تطبيق راحة RAHA)",
                            identifier = "007 99999 0023456789 42",
                            codeType = "RIP BaridiMob",
                            icon = Icons.Default.CreditCard,
                            accentColor = Color(0xFF00965E),
                            onCopy = { copyToClipboard("رقم الـ RIP لبريدي موب", "00799999002345678942") }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // CCP Option Card
                        PaymentMethodCard(
                            title = "الحساب البريدي الجاري (CCP)",
                            accountHolder = "مهدي حمداني - الجزائر",
                            identifier = "CCP: 23456789 / Clé 42",
                            codeType = "رقم الحساب البريدي والمفتاح",
                            icon = Icons.Default.AccountBalance,
                            accentColor = Color(0xFF084C8D),
                            onCopy = { copyToClipboard("رقم الحساب الجاري CCP", "23456789 clé 42") }
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Action Button
                        Button(
                            onClick = { showAppDonationSuccessDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_confirm_app_donation")
                        ) {
                            Icon(imageVector = Icons.Default.Handshake, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "تأكيد مساهمتي بقيمة $selectedAmountDzd دج",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        } else {
            // ==========================================
            // TAB 1: MOSQUE RESTROOMS CHARITY (المساجد)
            // ==========================================
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Mosque,
                                contentDescription = null,
                                tint = RahaGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "صدقة جارية لصيانة بيوت الله والوضوء",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "قال رسول الله ﷺ: «من بنى مسجداً لله بنى الله له في الجنة بيتاً». صيانة دورات مياه المساجد وتوفير الماء الساخن والصابون للمصلين وعابري السبيل من أعظم الصدقات الجارية وأنفعها.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = RahaGreen.copy(alpha = 0.12f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = RahaGreen)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "جميع التبرعات تذهب مباشرة إلى اللجان الدينية المعتمدة لكل مسجد وبدون أي وسيط أو اقتطاع.",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = RahaGreen
                                )
                            }
                        }
                    }
                }
            }

            // Mosque Projects List
            items(registeredMosqueProjects, key = { it.id }) { project ->
                MosqueProjectCard(
                    project = project,
                    onDonateClick = { showMosqueDonationDialog = project },
                    onCopyRip = { copyToClipboard("RIP ${project.name}", project.baridiMobRip) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }

    // Modal: App Donation Confirmation & Thank You
    if (showAppDonationSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showAppDonationSuccessDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "شكراً جزيلاً لدعمكم! 🇩🇿💙",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "مساهمتك بقيمة $selectedAmountDzd دج تساعد في استمرار خوادم تطبيق «راحة» وإبقاء التطبيق مجانياً لجميع أبناء الجزائر والمسافرين.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = TealPrimary.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "تمت إضافة وسام «الداعم المجتمعي 🥇» إلى ملفك الشخصي!",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = TealPrimary,
                            modifier = Modifier.padding(8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showAppDonationSuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Text("تم")
                }
            }
        )
    }

    // Modal: Mosque Donation Details Dialog
    if (showMosqueDonationDialog != null) {
        val proj = showMosqueDonationDialog!!
        AlertDialog(
            onDismissRequest = { showMosqueDonationDialog = null },
            icon = {
                Icon(imageVector = Icons.Default.Mosque, contentDescription = null, tint = RahaGreen, modifier = Modifier.size(36.dp))
            },
            title = {
                Text(
                    text = "التبرع لـ ${proj.name}",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column {
                    Text(
                        text = "الهدف: ${proj.projectNeed}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "الحساب البريدي المباشر للجنة المسجد:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("RIP: ${proj.baridiMobRip}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("CCP: ${proj.ccpNumber}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(onClick = { copyToClipboard("RIP ${proj.name}", proj.baridiMobRip) }) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "نسخ", tint = TealPrimary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showMosqueDonationDialog = null
                        showMosqueThankYouDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RahaGreen)
                ) {
                    Text("تأكيد إرسال الصدقة")
                }
            },
            dismissButton = {
                TextButton(onClick = { showMosqueDonationDialog = null }) {
                    Text("إلغاء")
                }
            }
        )
    }

    // Modal: Mosque Thank-You Dua Dialog
    if (showMosqueThankYouDialog) {
        AlertDialog(
            onDismissRequest = { showMosqueThankYouDialog = false },
            icon = {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = RahaGreen, modifier = Modifier.size(44.dp))
            },
            title = {
                Text("تقبل الله صدقتكم 🤲", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            },
            text = {
                Text(
                    text = "جعلها الله صدقة جارية في ميزان حسناتكم ونفع بها مصلين وعابري سبيل في بيت من بيوت الله.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(onClick = { showMosqueThankYouDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = RahaGreen)) {
                    Text("آمين")
                }
            }
        )
    }
}

@Composable
private fun PaymentMethodCard(
    title: String,
    accountHolder: String,
    identifier: String,
    codeType: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onCopy: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(22.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Text(text = identifier, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = accentColor)
                    Text(text = accountHolder, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            IconButton(onClick = onCopy) {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "نسخ", tint = accentColor)
            }
        }
    }
}

@Composable
private fun MosqueProjectCard(
    project: MosqueCharityProject,
    onDonateClick: () -> Unit,
    onCopyRip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = RahaGreen.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "📍 ${project.wilaya} • ${project.commune}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = RahaGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = "${project.collectedAmountDzd} / ${project.targetAmountDzd} دج",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = project.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "المشروع: ${project.projectNeed}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { project.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = RahaGreen,
                trackColor = RahaGreen.copy(alpha = 0.15f)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "المتبقي لإتمام الصيانة: ${project.remainingDzd} دج",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "${(project.progress * 100).toInt()}% مكتمل",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = RahaGreen
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onDonateClick,
                    colors = ButtonDefaults.buttonColors(containerColor = RahaGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.VolunteerActivism, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("تبرع لهذا المسجد", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                TextButton(
                    onClick = onCopyRip,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("نسخ الـ RIP", fontSize = 12.sp)
                }
            }
        }
    }
}
