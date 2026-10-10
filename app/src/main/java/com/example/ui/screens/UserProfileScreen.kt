package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.AlgerianWilayas
import com.example.data.model.RestroomEntity
import com.example.data.model.UserProfileData
import com.example.ui.components.NationalIdScannerDialog
import com.example.ui.theme.AlertRed
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.RahaBlue
import com.example.ui.theme.RahaGreen
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.WaterBlue
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

/**
 * Full User Profile Screen for the DZ Restrooms application.
 * Allows users to:
 * 1. View restrooms they have contributed to the map.
 * 2. Manage their personal information (name, phone, wilaya, commune, NIN).
 * 3. Review their contribution history and badges.
 * 4. Register or link accounts via Google, Facebook, TikTok.
 * 5. Scan an Algerian Biometric National ID Card to auto-populate profile details.
 */
@Composable
fun UserProfileScreen(
    userRestrooms: List<RestroomEntity>,
    onRestroomClick: (RestroomEntity) -> Unit,
    onAddNewRestroomClick: () -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val firebaseUser = remember { FirebaseAuth.getInstance().currentUser }

    // User Profile state
    var profileData by remember {
        mutableStateOf(
            UserProfileData(
                userId = firebaseUser?.uid ?: "user_default",
                fullName = firebaseUser?.displayName ?: "مهدي حمداني",
                fullNameFr = "HAMDANI MEHDI",
                email = firebaseUser?.email ?: "mehdi.hamdani.net@gmail.com",
                phone = "0550 12 34 56",
                wilaya = "16 - الجزائر العاصمة",
                commune = "الجزائر الوسطى",
                ninNumber = "199416010023456789",
                dateOfBirth = "12/05/1994",
                bloodGroup = "O+",
                isIdCardVerified = true,
                isGoogleLinked = firebaseUser != null,
                isFacebookLinked = false,
                isTikTokLinked = false,
                avatarUrl = firebaseUser?.photoUrl?.toString(),
                reputationScore = 210,
                contributionsCount = userRestrooms.size.coerceAtLeast(4),
                reviewsCount = 14,
                waterReportsCount = 3
            )
        )
    }

    // Editable form state
    var editFullName by remember(profileData.fullName) { mutableStateOf(profileData.fullName) }
    var editPhone by remember(profileData.phone) { mutableStateOf(profileData.phone) }
    var editWilaya by remember(profileData.wilaya) { mutableStateOf(profileData.wilaya) }
    var editCommune by remember(profileData.commune) { mutableStateOf(profileData.commune) }
    var editNin by remember(profileData.ninNumber) { mutableStateOf(profileData.ninNumber) }
    var editDateOfBirth by remember(profileData.dateOfBirth) { mutableStateOf(profileData.dateOfBirth) }
    var editBloodGroup by remember(profileData.bloodGroup) { mutableStateOf(profileData.bloodGroup) }

    var isWilayaDropdownOpen by remember { mutableStateOf(false) }
    var wilayaFilterText by remember { mutableStateOf("") }
    var showIdScannerDialog by remember { mutableStateOf(false) }
    var showSignOutConfirmDialog by remember { mutableStateOf(false) }
    var showSocialLinkSuccessDialog by remember { mutableStateOf<String?>(null) }
    var saveSuccessMessage by remember { mutableStateOf<String?>(null) }

    // Internal sub-tab: 0 = Profile & Identity, 1 = My Added Restrooms, 2 = Contribution History
    var activeSubTab by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("user_profile_screen"),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // 1. Profile Hero Banner & Identity Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                RahaBlue,
                                TealPrimary.copy(alpha = 0.85f),
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
                    // Profile Avatar
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 8.dp,
                            modifier = Modifier.size(92.dp)
                        ) {
                            if (!profileData.avatarUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = profileData.avatarUrl,
                                    contentDescription = "صورة الحساب",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(TealPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = profileData.fullName.take(2),
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }

                        // Verified National ID Checkmark Badge
                        if (profileData.isIdCardVerified) {
                            Surface(
                                shape = CircleShape,
                                color = RahaGreen,
                                shadowElevation = 4.dp,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "هوية معتمدة",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = profileData.fullName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )

                    Text(
                        text = profileData.email,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.85f))
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Contributor & Verification Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD54F),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = profileData.badgeTitle,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (profileData.isIdCardVerified) RahaGreen else Color(0xFFFF9800)
                        ) {
                            Text(
                                text = if (profileData.isIdCardVerified) "هوية بيومترية موثقة 🇩🇿" else "هوية غير مسحوبة",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Quick Stats Grid
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 6.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatBoxItem(
                                title = "مراحيض مضافة",
                                value = profileData.contributionsCount.toString(),
                                icon = Icons.Default.Add,
                                tint = TealPrimary
                            )
                            Divider(
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(1.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                            StatBoxItem(
                                title = "تقييمات ومراجعات",
                                value = profileData.reviewsCount.toString(),
                                icon = Icons.Default.RateReview,
                                tint = RahaBlue
                            )
                            Divider(
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(1.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                            StatBoxItem(
                                title = "نقاط المساهمة",
                                value = profileData.reputationScore.toString(),
                                icon = Icons.Default.Star,
                                tint = Color(0xFFFFA000)
                            )
                        }
                    }
                }
            }
        }

        // 2. Sub-Tabs Navigation (بياناتي الشخصية | مراحيضي المضافة | سجل المساهمات)
        item {
            TabRow(
                selectedTabIndex = activeSubTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = TealPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[activeSubTab]),
                        color = TealPrimary
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Tab(
                    selected = activeSubTab == 0,
                    onClick = { activeSubTab = 0 },
                    text = { Text("البيانات والهوية", fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.testTag("tab_personal_info")
                )
                Tab(
                    selected = activeSubTab == 1,
                    onClick = { activeSubTab = 1 },
                    text = { Text("مراحيضي (${profileData.contributionsCount})", fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.testTag("tab_my_restrooms")
                )
                Tab(
                    selected = activeSubTab == 2,
                    onClick = { activeSubTab = 2 },
                    text = { Text("سجل النشاط", fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.testTag("tab_contribution_history")
                )
            }
        }

        // Feedback Snackbar / Notification if saved
        if (saveSuccessMessage != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SuccessGreen.copy(alpha = 0.15f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = saveSuccessMessage ?: "",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = SuccessGreen
                        )
                    }
                }
            }
        }

        // Sub-Tab Content
        when (activeSubTab) {
            0 -> {
                // TAB 0: PERSONAL INFORMATION & REGISTRATION / ID SCAN
                item {
                    // Registration & Social Account Linking Card
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "تسجيل وربط الحسابات المتاحة",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "يمكنك التسجيل وربط حسابك عبر Google أو Facebook أو TikTok أو بالمسح المباشر لبطاقة الهوية.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Google Linking Row
                            SocialLinkRow(
                                title = "حساب Google",
                                subtitle = if (profileData.isGoogleLinked) profileData.email else "غير متصل",
                                isConnected = profileData.isGoogleLinked,
                                badgeColor = Color(0xFF4285F4),
                                iconLabel = "G",
                                onToggle = {
                                    if (!profileData.isGoogleLinked) {
                                        profileData = profileData.copy(isGoogleLinked = true)
                                        showSocialLinkSuccessDialog = "Google"
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Facebook Linking Row
                            SocialLinkRow(
                                title = "حساب Facebook",
                                subtitle = if (profileData.isFacebookLinked) "مرتبط بنجاح (Mehdi Hamdani)" else "انقر للربط وتسجيل الدخول",
                                isConnected = profileData.isFacebookLinked,
                                badgeColor = Color(0xFF1877F2),
                                iconLabel = "f",
                                onToggle = {
                                    val nextState = !profileData.isFacebookLinked
                                    profileData = profileData.copy(isFacebookLinked = nextState)
                                    if (nextState) showSocialLinkSuccessDialog = "Facebook"
                                }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // TikTok Linking Row
                            SocialLinkRow(
                                title = "حساب TikTok",
                                subtitle = if (profileData.isTikTokLinked) "مرتبط بنجاح (@mehdi_dz)" else "انقر للربط بحساب تيك توك",
                                isConnected = profileData.isTikTokLinked,
                                badgeColor = Color(0xFF000000),
                                iconLabel = "TT",
                                onToggle = {
                                    val nextState = !profileData.isTikTokLinked
                                    profileData = profileData.copy(isTikTokLinked = nextState)
                                    if (nextState) showSocialLinkSuccessDialog = "TikTok"
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // National ID Card Scanner Banner
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = RahaGreen.copy(alpha = 0.12f),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, RahaGreen.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showIdScannerDialog = true }
                                    .testTag("scan_national_id_card_banner")
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(CircleShape)
                                            .background(RahaGreen),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DocumentScanner,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "مسح بطاقة التعريف الوطنية (NIN)",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = RahaGreen
                                            )
                                        )
                                        Text(
                                            text = "استخراج تلقائي للاسم، اللقب، رقم التعريف، وتاريخ ومكان الميلاد",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Button(
                                        onClick = { showIdScannerDialog = true },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = RahaGreen),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text("مسح الآن", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                // Personal Information Form
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "إدارة البيانات الشخصية",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                ) {
                                    Text(
                                        text = "قابلة للتعديل",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Full Name Input
                            OutlinedTextField(
                                value = editFullName,
                                onValueChange = { editFullName = it },
                                label = { Text("الاسم واللقب") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_profile_name")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Phone Number Input
                            OutlinedTextField(
                                value = editPhone,
                                onValueChange = { editPhone = it },
                                label = { Text("رقم الهاتف (الجزائر)") },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_profile_phone")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Wilaya Dropdown (58 Wilayas)
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = editWilaya,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("الولاية (من بين 58 ولاية)") },
                                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                                    trailingIcon = {
                                        IconButton(onClick = { isWilayaDropdownOpen = true }) {
                                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { isWilayaDropdownOpen = true }
                                        .testTag("input_profile_wilaya")
                                )

                                DropdownMenu(
                                    expanded = isWilayaDropdownOpen,
                                    onDismissRequest = { isWilayaDropdownOpen = false },
                                    modifier = Modifier
                                        .fillMaxWidth(0.85f)
                                        .height(300.dp)
                                ) {
                                    OutlinedTextField(
                                        value = wilayaFilterText,
                                        onValueChange = { wilayaFilterText = it },
                                        placeholder = { Text("ابحث في الـ 58 ولاية...") },
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                    )

                                    val filteredList = if (wilayaFilterText.isBlank()) {
                                        AlgerianWilayas.all58Wilayas
                                    } else {
                                        val q = wilayaFilterText.lowercase()
                                        AlgerianWilayas.all58Wilayas.filter { it.fullSearchableText.contains(q) }
                                    }

                                    filteredList.forEach { wilaya ->
                                        DropdownMenuItem(
                                            text = { Text(wilaya.displayName) },
                                            onClick = {
                                                editWilaya = wilaya.displayName
                                                isWilayaDropdownOpen = false
                                                wilayaFilterText = ""
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Commune / District
                            OutlinedTextField(
                                value = editCommune,
                                onValueChange = { editCommune = it },
                                label = { Text("البلدية أو الحي") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_profile_commune")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // National Identification Number (NIN)
                            OutlinedTextField(
                                value = editNin,
                                onValueChange = { editNin = it },
                                label = { Text("رقم التعريف الوطني البيومتري (NIN - 18 رقماً)") },
                                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_profile_nin")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Date of Birth & Blood Group Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = editDateOfBirth,
                                    onValueChange = { editDateOfBirth = it },
                                    label = { Text("تاريخ الميلاد") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1.3f)
                                )
                                OutlinedTextField(
                                    value = editBloodGroup,
                                    onValueChange = { editBloodGroup = it },
                                    label = { Text("فصيلة الدم") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(0.7f)
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Save Button
                            Button(
                                onClick = {
                                    profileData = profileData.copy(
                                        fullName = editFullName,
                                        phone = editPhone,
                                        wilaya = editWilaya,
                                        commune = editCommune,
                                        ninNumber = editNin,
                                        dateOfBirth = editDateOfBirth,
                                        bloodGroup = editBloodGroup
                                    )
                                    saveSuccessMessage = "تم حفظ التعديلات وتحديث البيانات الشخصية بنجاح! ✅"
                                    Toast.makeText(context, "تم حفظ البيانات بنجاح", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_save_profile_info")
                            ) {
                                Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("حفظ التعديلات", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Sign Out Option
                item {
                    OutlinedButton(
                        onClick = { showSignOutConfirmDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AlertRed),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .testTag("btn_profile_sign_out")
                    ) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("تسجيل الخروج من الحساب", fontWeight = FontWeight.Bold)
                    }
                }
            }

            1 -> {
                // TAB 1: RESTROOMS ADDED BY USER
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "المراحيض التي ساهمت بإضافتها",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "مساهماتك تساعد المسافرين والمواطنين في الجزائر",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = onAddNewRestroomClick,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("btn_add_restroom_from_profile")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("إضافة مرفق", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Sample or actual user added restrooms
                val displayList = if (userRestrooms.isNotEmpty()) {
                    userRestrooms
                } else {
                    listOf(
                        RestroomEntity(
                            id = 901,
                            name = "مرحاض خواص عصري - شارع ديدوش مراد",
                            nameFr = "Toilettes Privées Didouche Mourad",
                            wilaya = "الجزائر العاصمة",
                            commune = "سيدي امحمد",
                            type = com.example.data.model.RestroomType.PRIVATE_COMMERCIAL,
                            latitude = 36.7650,
                            longitude = 3.0550,
                            isFree = false,
                            priceDzd = 30,
                            rating = 4.8f,
                            reviewsCount = 18,
                            hasWater = true,
                            hasSoapPaper = true,
                            isAccessiblePmr = true,
                            hasWomenSection = true,
                            cleanlinessRating = 4.9f,
                            openingHours = "07:00 - 21:00",
                            address = "شارع ديدوش مراد، مقابل البريد المركزي",
                            notes = "مرفق خواص مكيف ومجهز بمغاسل حديثة وصابون متوفر دائماً.",
                            isUserAdded = true
                        ),
                        RestroomEntity(
                            id = 902,
                            name = "مرافق وضوء ومصلى جامع القدس - بئر خادم",
                            nameFr = "Mosquée El Qods - Birkhadem",
                            wilaya = "الجزائر العاصمة",
                            commune = "بئر خادم",
                            type = com.example.data.model.RestroomType.MOSQUE,
                            latitude = 36.7150,
                            longitude = 3.0620,
                            isFree = true,
                            priceDzd = 0,
                            rating = 4.7f,
                            reviewsCount = 24,
                            hasWater = true,
                            hasSoapPaper = true,
                            isAccessiblePmr = true,
                            hasWomenSection = true,
                            cleanlinessRating = 4.6f,
                            openingHours = "أوقات الصلوات الخمس",
                            address = "بئر خادم، بجانب الطريق الوطني",
                            notes = "مرافق وضوء نظيفة جداً مع سخانات ماء شتوية.",
                            isUserAdded = true
                        )
                    )
                }

                items(displayList, key = { it.id }) { restroom ->
                    UserAddedRestroomCard(
                        restroom = restroom,
                        onClick = { onRestroomClick(restroom) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }

            2 -> {
                // TAB 2: CONTRIBUTION HISTORY & COMMUNITY BADGES
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "سجل الأنشطة والمساهمات المجتمعية",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "تتبع نقاطك ومشاركاتك في تحسين وتحديث شبكة المراحيض بالجزائر",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Badges Showcase
                        Text(
                            text = "الأوسمة والشهادات المكتسبة 🏆",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = TealPrimary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            BadgeCard(title = "مساهم معتمد", subtitle = "تم التحقق بالهوية 🇩🇿", icon = Icons.Default.Verified, tint = RahaGreen)
                            BadgeCard(title = "سفير النظافة", subtitle = "أكثر من 10 تقييمات", icon = Icons.Default.Star, tint = Color(0xFFFFA000))
                            BadgeCard(title = "حارس المياه", subtitle = "بلاغات دقيقة للحالة", icon = Icons.Default.WaterDrop, tint = WaterBlue)
                            BadgeCard(title = "الرائد الأول", subtitle = "من أوائل المكتشفين", icon = Icons.Default.Badge, tint = RahaBlue)
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Contribution Timeline
                        Text(
                            text = "الأنشطة الأخيرة:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        TimelineItem(
                            action = "أضفت مرحاض خواص جديد",
                            place = "شارع ديدوش مراد (الجزائر العاصمة)",
                            date = "منذ يومين",
                            points = "+50 نقطة",
                            icon = Icons.Default.Add,
                            tint = TealPrimary
                        )

                        TimelineItem(
                            action = "قيّمت مستوى النظافة 5 نجوم",
                            place = "جامع الجزائر الأعظم (المحمدية)",
                            date = "منذ 4 أيام",
                            points = "+15 نقطة",
                            icon = Icons.Default.RateReview,
                            tint = RahaBlue
                        )

                        TimelineItem(
                            action = "أبلغت عن حالة المياه (متوفرة)",
                            place = "محطة النقل خروبة (سوجرال)",
                            date = "منذ أسبوع",
                            points = "+10 نقاط",
                            icon = Icons.Default.WaterDrop,
                            tint = RahaGreen
                        )

                        TimelineItem(
                            action = "مسح وتوثيق بطاقة الهوية الوطنية",
                            place = "نظام التحقق البيومتري الجزائري",
                            date = "منذ أسبوعين",
                            points = "+100 نقطة",
                            icon = Icons.Default.DocumentScanner,
                            tint = Color(0xFF673AB7)
                        )
                    }
                }
            }
        }
    }

    // Modal: National ID Card Scanner
    if (showIdScannerDialog) {
        NationalIdScannerDialog(
            onDismiss = { showIdScannerDialog = false },
            onDataExtracted = { extracted ->
                editFullName = extracted.fullNameAr
                editNin = extracted.ninNumber
                editWilaya = extracted.wilaya
                editCommune = extracted.commune
                editDateOfBirth = extracted.dateOfBirth
                editBloodGroup = extracted.bloodGroup

                profileData = profileData.copy(
                    fullName = extracted.fullNameAr,
                    fullNameFr = extracted.fullNameFr,
                    ninNumber = extracted.ninNumber,
                    wilaya = extracted.wilaya,
                    commune = extracted.commune,
                    dateOfBirth = extracted.dateOfBirth,
                    bloodGroup = extracted.bloodGroup,
                    isIdCardVerified = true
                )
                saveSuccessMessage = "تم استخراج بيانات بطاقة التعريف الوطنية وتحديث الملف الشخصي بنجاح! 🇩🇿"
                Toast.makeText(context, "تم استيراد بيانات الهوية تلقائياً!", Toast.LENGTH_LONG).show()
            }
        )
    }

    // Modal: Sign Out Confirmation Dialog
    if (showSignOutConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutConfirmDialog = false },
            title = { Text("تأكيد تسجيل الخروج", fontWeight = FontWeight.Bold) },
            text = { Text("هل أنت متأكد من رغبتك في تسجيل الخروج من تطبيق «راحة»؟ يمكنك تسجيل الدخول مجدداً في أي وقت.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSignOutConfirmDialog = false
                        onSignOutClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AlertRed)
                ) {
                    Text("نعم، تسجيل الخروج")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutConfirmDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }

    // Dialog: Social account linking notification
    if (showSocialLinkSuccessDialog != null) {
        AlertDialog(
            onDismissRequest = { showSocialLinkSuccessDialog = null },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = RahaGreen) },
            title = { Text("تم ربط الحساب بنجاح", fontWeight = FontWeight.Bold) },
            text = { Text("تم ربط حساب ${showSocialLinkSuccessDialog} بملفك الشخصي في راحة بنجاح.") },
            confirmButton = {
                Button(onClick = { showSocialLinkSuccessDialog = null }, colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)) {
                    Text("تم")
                }
            }
        )
    }
}

@Composable
private fun StatBoxItem(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SocialLinkRow(
    title: String,
    subtitle: String,
    isConnected: Boolean,
    badgeColor: Color,
    iconLabel: String,
    onToggle: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(badgeColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iconLabel,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Button(
                onClick = onToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isConnected) RahaGreen else MaterialTheme.colorScheme.outline
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (isConnected) "متصل ✓" else "ربط",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun UserAddedRestroomCard(
    restroom: RestroomEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
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
                        text = "معتمد ومتاح للجميع ✓",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = RahaGreen,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = if (restroom.isFree) "مجاني" else "${restroom.priceDzd} دج",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = TealPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = restroom.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "📍 ${restroom.wilaya} • ${restroom.commune}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⭐ ${restroom.rating} (${restroom.reviewsCount} تقييم)",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFFFFA000)
                )

                Text(
                    text = "عرض على الخريطة ←",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = TealPrimary
                )
            }
        }
    }
}

@Composable
private fun BadgeCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = tint.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.dp, tint.copy(alpha = 0.3f)),
        modifier = Modifier.width(130.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(tint),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = tint,
                fontSize = 12.sp
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun TimelineItem(
    action: String,
    place: String,
    date: String,
    points: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(tint.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = action,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "$place • $date",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = tint.copy(alpha = 0.15f)
            ) {
                Text(
                    text = points,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = tint,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}
