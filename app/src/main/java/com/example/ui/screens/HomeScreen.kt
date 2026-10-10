package com.example.ui.screens

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.credentials.CredentialManager
import com.example.ui.auth.signOut
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.location.LocationHelper
import com.example.data.model.RestroomType
import androidx.compose.material.icons.filled.Person
import com.example.ui.components.MapSearchBar
import com.example.ui.components.AddRestroomDialog
import com.example.ui.components.FloatingFilterPanel
import com.example.ui.components.GoogleRestroomMap
import com.example.ui.components.InteractiveRestroomMap
import com.example.ui.components.OfflineCacheDialog
import com.example.ui.components.ProjectStudySheet
import com.example.ui.components.RestroomCard
import com.example.ui.components.RestroomDetailSheet
import com.example.ui.components.getTypeColor
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolunteerActivism
import com.example.ui.screens.DonationsScreen
import com.example.ui.screens.SettingsScreen
import com.example.util.AppStrings
import com.example.ui.screens.UserProfileScreen
import com.example.ui.theme.AlertRed
import com.example.ui.theme.RahaBlue
import com.example.ui.theme.RahaGreen
import com.example.ui.theme.RahaGreenContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealPrimary
import com.example.ui.viewmodel.MapProviderType
import com.example.ui.viewmodel.RestroomViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: RestroomViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val userLocation by viewModel.userLocation.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val filteredRestrooms by viewModel.filteredRestrooms.collectAsStateWithLifecycle()
    val nearestRestroom by viewModel.nearestRestroom.collectAsStateWithLifecycle()
    val selectedRestroom by viewModel.selectedRestroom.collectAsStateWithLifecycle()
    val selectedRestroomReviews by viewModel.selectedRestroomReviews.collectAsStateWithLifecycle()
    val showAddDialog by viewModel.showAddDialog.collectAsStateWithLifecycle()
    val showStudyDialog by viewModel.showStudyDialog.collectAsStateWithLifecycle()
    val mapProvider by viewModel.mapProvider.collectAsStateWithLifecycle()
    val isLocating by viewModel.isLocating.collectAsStateWithLifecycle()
    val locationMessage by viewModel.locationMessage.collectAsStateWithLifecycle()
    val offlineCacheMetadata by viewModel.offlineCacheMetadata.collectAsStateWithLifecycle()
    val isOfflineMode by viewModel.isOfflineMode.collectAsStateWithLifecycle()
    val showOfflineCacheDialog by viewModel.showOfflineCacheDialog.collectAsStateWithLifecycle()
    val showInvalidApiKeyDialog by viewModel.showInvalidApiKeyDialog.collectAsStateWithLifecycle()

    val appSettings by viewModel.appSettings.collectAsStateWithLifecycle()
    val currentLang = appSettings.language

    val locationHelper = remember { LocationHelper(context) }
    var hasLocationPermission by remember {
        mutableStateOf(locationHelper.hasLocationPermission())
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val granted = (perms[Manifest.permission.ACCESS_FINE_LOCATION] == true) ||
                (perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
        hasLocationPermission = granted
        if (granted) {
            viewModel.requestGpsLocation(locationHelper)
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(locationMessage) {
        val msg = locationMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearLocationMessage()
        }
    }

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Map, 1: List, 2: Favorites, 3: Donations, 4: Settings, 5: Profile
    var isSearchVisible by remember { mutableStateOf(false) }
    var showAccountDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val detailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val studySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Map, contentDescription = null) },
                    label = { Text(AppStrings.navMap(currentLang), fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = TealPrimary,
                        indicatorColor = TealPrimary
                    ),
                    modifier = Modifier.testTag("bottom_nav_map")
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.FormatListBulleted, contentDescription = null) },
                    label = { Text(AppStrings.navRestrooms(currentLang), fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = TealPrimary,
                        indicatorColor = TealPrimary
                    ),
                    modifier = Modifier.testTag("bottom_nav_list")
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                    label = { Text(AppStrings.navFavorites(currentLang), fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = TealPrimary,
                        indicatorColor = TealPrimary
                    ),
                    modifier = Modifier.testTag("bottom_nav_favorites")
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.VolunteerActivism, contentDescription = null) },
                    label = { Text(AppStrings.navDonations(currentLang), fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = TealPrimary,
                        indicatorColor = TealPrimary
                    ),
                    modifier = Modifier.testTag("bottom_nav_donations")
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text(AppStrings.navSettings(currentLang), fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = TealPrimary,
                        indicatorColor = TealPrimary
                    ),
                    modifier = Modifier.testTag("bottom_nav_settings")
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_raha_logo),
                            contentDescription = "شعار تطبيق راحة",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "راحة",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = RahaBlue
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = RahaGreenContainer
                                ) {
                                    Text(
                                        text = "RAHA",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp,
                                            color = RahaGreen
                                        ),
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "📍 موقعك: ${userLocation.cityName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                                ) {
                                    Text(
                                        text = "محفوظ محلياً",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                },
                actions = {
                    // Search toggle (only on map/list)
                    if (selectedTab in 0..2) {
                        IconButton(
                            onClick = { isSearchVisible = !isSearchVisible },
                            modifier = Modifier.testTag("search_toggle_button")
                        ) {
                            Icon(
                                imageVector = if (isSearchVisible) Icons.Default.Clear else Icons.Default.Search,
                                contentDescription = "بحث"
                            )
                        }
                    }

                    // Offline Cache & Local Storage Status Button
                    IconButton(
                        onClick = { viewModel.setShowOfflineCacheDialog(true) },
                        modifier = Modifier.testTag("offline_cache_button")
                    ) {
                        Icon(
                            imageVector = if (isOfflineMode) Icons.Default.CloudOff else Icons.Default.CloudDone,
                            contentDescription = "التخزين المحلي Room بدون إنترنت",
                            tint = if (isOfflineMode) Color(0xFFE65100) else RahaGreen
                        )
                    }

                    // Project Feasibility Study Button
                    IconButton(
                        onClick = { viewModel.setShowStudyDialog(true) },
                        modifier = Modifier.testTag("project_study_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "دراسة المشروع الشاملة",
                            tint = TealPrimary
                        )
                    }

                    // User Account & Profile Screen Button
                    IconButton(
                        onClick = { selectedTab = if (selectedTab == 5) 0 else 5 },
                        modifier = Modifier.testTag("account_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "الملف الشخصي والحساب",
                            tint = if (selectedTab == 5) RahaGreen else TealPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            if (selectedTab in 0..2) {
                FloatingActionButton(
                    onClick = { viewModel.setShowAddDialog(true) },
                    containerColor = TealPrimary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("fab_add_restroom")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "إضافة")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("إضافة", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Input Row (Expandable)
            AnimatedVisibility(visible = isSearchVisible) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = filterState.searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = { Text("ابحث بالاسم، البلدية، الحي أو المسجد...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (filterState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "مسح")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("search_input_field")
                    )
                }
            }

            // Offline Mode Active Banner
            AnimatedVisibility(visible = isOfflineMode) {
                Surface(
                    color = Color(0xFFFFF3E0),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.setShowOfflineCacheDialog(true) }
                        .testTag("offline_mode_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WifiOff,
                                contentDescription = null,
                                tint = Color(0xFFE65100),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "وضع عدم الاتصال: ${filteredRestrooms.size} مرافق مخزنة محلياً في Room",
                                fontSize = 12.sp,
                                color = Color(0xFFE65100),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = "إدارة التخزين",
                            fontSize = 11.sp,
                            color = Color(0xFFE65100),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Quick Emergency Nearest Card (Shown if available)
            if (nearestRestroom != null && selectedTab != 0) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { viewModel.selectRestroom(nearestRestroom?.restroom) }
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
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(TealPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ElectricBolt,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "الأقرب إليك الآن: ${nearestRestroom?.restroom?.name}",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "يبعد فقط ${nearestRestroom?.formattedDistance} • ${if (nearestRestroom?.restroom?.isFree == true) "مجاني" else "${nearestRestroom?.restroom?.priceDzd} دج"}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Button(
                            onClick = {
                                nearestRestroom?.restroom?.let {
                                    viewModel.openInGoogleMaps(context, it)
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("emergency_navigate_button")
                        ) {
                            Icon(imageVector = Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("انطلق", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }

            // Horizontal Filter Chips Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // All Types Chip
                FilterChip(
                    selected = filterState.selectedType == null,
                    onClick = { viewModel.setSelectedType(null) },
                    label = { Text("الكل") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TealPrimary,
                        selectedLabelColor = Color.White
                    )
                )

                // Mosque
                FilterChip(
                    selected = filterState.selectedType == RestroomType.MOSQUE,
                    onClick = { viewModel.setSelectedType(RestroomType.MOSQUE) },
                    label = { Text("مساجد ووضوء") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = getTypeColor(RestroomType.MOSQUE),
                        selectedLabelColor = Color.White
                    )
                )

                // Hammam
                FilterChip(
                    selected = filterState.selectedType == RestroomType.HAMMAM,
                    onClick = { viewModel.setSelectedType(RestroomType.HAMMAM) },
                    label = { Text("حمامات شعبية / دوش") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = getTypeColor(RestroomType.HAMMAM),
                        selectedLabelColor = Color.White
                    )
                )

                // Naftal / Highway
                FilterChip(
                    selected = filterState.selectedType == RestroomType.NAFTAL_HIGHWAY,
                    onClick = { viewModel.setSelectedType(RestroomType.NAFTAL_HIGHWAY) },
                    label = { Text("نفطال / طريق سريع") }
                )

                // Transport
                FilterChip(
                    selected = filterState.selectedType == RestroomType.TRANSPORT_HUB,
                    onClick = { viewModel.setSelectedType(RestroomType.TRANSPORT_HUB) },
                    label = { Text("محطات نقل وسوجرال") }
                )

                // Mall
                FilterChip(
                    selected = filterState.selectedType == RestroomType.MALL,
                    onClick = { viewModel.setSelectedType(RestroomType.MALL) },
                    label = { Text("مراكز تجارية") }
                )

                // Private Restroom (مراحيض خواص)
                FilterChip(
                    selected = filterState.selectedType == RestroomType.PRIVATE_COMMERCIAL,
                    onClick = { viewModel.setSelectedType(RestroomType.PRIVATE_COMMERCIAL) },
                    label = { Text("مراحيض خواص") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = getTypeColor(RestroomType.PRIVATE_COMMERCIAL),
                        selectedLabelColor = Color.White
                    )
                )

                // Free Only
                FilterChip(
                    selected = filterState.freeOnly,
                    onClick = { viewModel.toggleFreeOnly() },
                    label = { Text("مجاني فقط") }
                )

                // Women Section
                FilterChip(
                    selected = filterState.womenSectionOnly,
                    onClick = { viewModel.toggleWomenOnly() },
                    label = { Text("مخصص للنساء") }
                )

                // Accessible PMR
                FilterChip(
                    selected = filterState.accessiblePmrOnly,
                    onClick = { viewModel.toggleAccessibleOnly() },
                    label = { Text("ذوي الهمم (PMR)") }
                )

                // High Cleanliness
                FilterChip(
                    selected = filterState.highCleanlinessOnly,
                    onClick = { viewModel.toggleCleanlinessOnly() },
                    label = { Text("✨ نظافة عالية") }
                )

                // Baby Changing Table
                FilterChip(
                    selected = filterState.changingTableOnly,
                    onClick = { viewModel.toggleChangingTableOnly() },
                    label = { Text("👶 طاولة رضع") }
                )

                // Showers
                FilterChip(
                    selected = filterState.showerOnly,
                    onClick = { viewModel.toggleShowerOnly() },
                    label = { Text("دوش ساخن") }
                )
            }

            // Tab Navigation: Map vs List vs Favorites vs Profile
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = TealPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = TealPrimary
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Map, contentDescription = null) },
                    text = { Text("خريطة", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.testTag("tab_map")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.FormatListBulleted, contentDescription = null) },
                    text = { Text("الأقرب (${filteredRestrooms.size})", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.testTag("tab_list")
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                    text = { Text("المفضلة", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.testTag("tab_favorites")
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    text = { Text("الملف الشخصي", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.testTag("tab_profile")
                )
            }

            // Main Content Body based on Selected Tab
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when (selectedTab) {
                    0 -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            if (mapProvider == MapProviderType.GOOGLE_MAPS) {
                                GoogleRestroomMap(
                                    userLocation = userLocation,
                                    restrooms = filteredRestrooms,
                                    selectedRestroom = selectedRestroom,
                                    filterState = filterState,
                                    onRestroomSelected = { viewModel.selectRestroom(it) },
                                    onNavigateClick = { viewModel.openInGoogleMaps(context, it) },
                                    onWilayaChange = { name, lat, lng ->
                                        viewModel.setUserLocation(lat, lng, name)
                                    },
                                    onLocateUserClick = {
                                        if (hasLocationPermission) {
                                            viewModel.requestGpsLocation(locationHelper)
                                        } else {
                                            permissionLauncher.launch(
                                                arrayOf(
                                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                                )
                                            )
                                        }
                                    },
                                    isLocating = isLocating,
                                    hasLocationPermission = hasLocationPermission,
                                    onRequestPermission = {
                                        permissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                        )
                                    },
                                    onSwitchToOfflineMap = {
                                        viewModel.setMapProvider(MapProviderType.OFFLINE_VECTOR)
                                    },
                                    onToggleAccessible = { viewModel.toggleAccessibleOnly() },
                                    onToggleCleanliness = { viewModel.toggleCleanlinessOnly() },
                                    onToggleChangingTable = { viewModel.toggleChangingTableOnly() },
                                    onToggleFreeOnly = { viewModel.toggleFreeOnly() },
                                    onToggleWomenOnly = { viewModel.toggleWomenOnly() },
                                    onToggleWuduOnly = { viewModel.toggleWuduOnly() },
                                    onToggleShowerOnly = { viewModel.toggleShowerOnly() },
                                    onToggleMosques = { viewModel.toggleMosquesVisibility() },
                                    onToggleRestAreas = { viewModel.toggleRestAreasVisibility() },
                                    onTogglePublicToilets = { viewModel.togglePublicToiletsVisibility() },
                                    onResetAll = { viewModel.resetAllFilters() }
                                )
                            } else {
                                InteractiveRestroomMap(
                                    userLocation = userLocation,
                                    restrooms = filteredRestrooms,
                                    selectedRestroom = selectedRestroom,
                                    filterState = filterState,
                                    onRestroomSelected = { viewModel.selectRestroom(it) },
                                    onNavigateClick = { viewModel.openInGoogleMaps(context, it) },
                                    onWilayaChange = { name, lat, lng ->
                                        viewModel.setUserLocation(lat, lng, name)
                                    },
                                    onSwitchToGoogleMap = {
                                        viewModel.setMapProvider(MapProviderType.GOOGLE_MAPS)
                                    },
                                    onLocateUserClick = {
                                        if (hasLocationPermission) {
                                            viewModel.requestGpsLocation(locationHelper)
                                        } else {
                                            permissionLauncher.launch(
                                                arrayOf(
                                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                                )
                                            )
                                        }
                                    },
                                    isLocating = isLocating,
                                    hasLocationPermission = hasLocationPermission,
                                    onRequestPermission = {
                                        permissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                        )
                                    },
                                    onToggleAccessible = { viewModel.toggleAccessibleOnly() },
                                    onToggleCleanliness = { viewModel.toggleCleanlinessOnly() },
                                    onToggleChangingTable = { viewModel.toggleChangingTableOnly() },
                                    onToggleFreeOnly = { viewModel.toggleFreeOnly() },
                                    onToggleWomenOnly = { viewModel.toggleWomenOnly() },
                                    onToggleWuduOnly = { viewModel.toggleWuduOnly() },
                                    onToggleShowerOnly = { viewModel.toggleShowerOnly() },
                                    onToggleMosques = { viewModel.toggleMosquesVisibility() },
                                    onToggleRestAreas = { viewModel.toggleRestAreasVisibility() },
                                    onTogglePublicToilets = { viewModel.togglePublicToiletsVisibility() },
                                    onResetAll = { viewModel.resetAllFilters() }
                                )
                            }

                            // Top floating search bar on Map view (Search by Algerian Wilaya, city, or district)
                            MapSearchBar(
                                searchQuery = filterState.searchQuery,
                                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                onWilayaSelected = { wilaya ->
                                    viewModel.setUserLocation(wilaya.latitude, wilaya.longitude, "ولاية ${wilaya.arabicName}")
                                    viewModel.setSelectedWilaya(wilaya.arabicName)
                                    viewModel.setSearchQuery(wilaya.arabicName)
                                },
                                currentWilayaName = userLocation.cityName,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .testTag("map_view_top_search_bar")
                            )
                        }
                    }
                    1 -> {
                        // Nearest Restrooms List View
                        if (filteredRestrooms.isEmpty()) {
                            EmptyStateNotice(
                                title = "لا توجد نتائج مطابقة للبحث",
                                subtitle = "جرّب تغيير خيارات الفلترة أو اختر ولاية أخرى أو ساهم بإضافة مرفق جديد."
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .testTag("restrooms_list"),
                                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(filteredRestrooms, key = { it.restroom.id }) { item ->
                                    RestroomCard(
                                        item = item,
                                        onClick = { viewModel.selectRestroom(item.restroom) },
                                        onNavigateClick = { viewModel.openInGoogleMaps(context, item.restroom) },
                                        onFavoriteToggle = { viewModel.toggleFavorite(item.restroom) }
                                    )
                                }
                            }
                        }
                    }
                    2 -> {
                        // Favorites List View
                        val favItems = filteredRestrooms.filter { it.restroom.isFavorite }
                        if (favItems.isEmpty()) {
                            EmptyStateNotice(
                                title = "قائمة المفضلة فارغة",
                                subtitle = "انقر على رمز القلب في أي مرحاض أو حمام لإضافته إلى قائمة المفضلة والوصول إليه بسرعة."
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .testTag("favorites_list"),
                                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(favItems, key = { it.restroom.id }) { item ->
                                    RestroomCard(
                                        item = item,
                                        onClick = { viewModel.selectRestroom(item.restroom) },
                                        onNavigateClick = { viewModel.openInGoogleMaps(context, item.restroom) },
                                        onFavoriteToggle = { viewModel.toggleFavorite(item.restroom) }
                                    )
                                }
                            }
                        }
                    }
                    3 -> {
                        // Donations & Mosque Projects Screen (تبرعات لتطبيق راحة وللمساجد)
                        DonationsScreen(
                            currentLanguage = currentLang
                        )
                    }
                    4 -> {
                        // Settings Screen (5 Themes, Dark Mode, 3 Languages)
                        SettingsScreen(
                            settings = appSettings,
                            onThemePaletteChange = { palette ->
                                viewModel.setThemePalette(palette)
                            },
                            onDarkModeChange = { mode ->
                                viewModel.setDarkModeOption(mode)
                            },
                            onLanguageChange = { lang ->
                                viewModel.setLanguage(lang)
                            },
                            onOpenOfflineDialog = {
                                viewModel.setShowOfflineCacheDialog(true)
                            },
                            onOpenStudyDialog = {
                                viewModel.setShowStudyDialog(true)
                            }
                        )
                    }
                    5 -> {
                        // User Profile View
                        val userAddedRestrooms = filteredRestrooms
                            .map { it.restroom }
                            .filter { it.isUserAdded }

                        UserProfileScreen(
                            userRestrooms = userAddedRestrooms,
                            onRestroomClick = { restroom ->
                                viewModel.selectRestroom(restroom)
                                selectedTab = 0
                            },
                            onAddNewRestroomClick = {
                                viewModel.setShowAddDialog(true)
                            },
                            onSignOutClick = {
                                signOut(
                                    context = context,
                                    credentialManager = CredentialManager.create(context),
                                    onSignOutComplete = {
                                        selectedTab = 0
                                    },
                                    scope = coroutineScope
                                )
                            }
                        )
                    }
                }

                // Floating Category Filter Panel overlaying the bottom of the screen (only on Map, List, or Favorites: 0, 1, 2)
                if (selectedTab in 0..2) {
                    FloatingFilterPanel(
                        showMosques = filterState.showMosques,
                        showPublicToilets = filterState.showPublicToilets,
                        showRestAreas = filterState.showRestAreas,
                        onToggleMosques = { viewModel.toggleMosquesVisibility() },
                        onTogglePublicToilets = { viewModel.togglePublicToiletsVisibility() },
                        onToggleRestAreas = { viewModel.toggleRestAreasVisibility() },
                        onResetAll = { viewModel.resetVisibilityToggles() },
                        activeCount = filteredRestrooms.size,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                    )
                }
            }
        }
    }

    // Restroom Detail Bottom Sheet
    if (selectedRestroom != null) {
        val distText = filteredRestrooms.find { it.restroom.id == selectedRestroom!!.id }?.formattedDistance ?: ""
        RestroomDetailSheet(
            restroom = selectedRestroom!!,
            reviews = selectedRestroomReviews,
            distanceText = distText,
            sheetState = detailSheetState,
            onDismiss = { viewModel.selectRestroom(null) },
            onNavigateClick = { viewModel.openInGoogleMaps(context, selectedRestroom!!) },
            onShareClick = { viewModel.shareRestroom(context, selectedRestroom!!) },
            onFavoriteToggle = { viewModel.toggleFavorite(selectedRestroom!!) },
            onReportWaterCut = { isCut -> viewModel.reportWaterCut(selectedRestroom!!, isCut) },
            onRateRestroom = { rating -> viewModel.rateRestroom(selectedRestroom!!, rating) },
            onSubmitReview = { author, rating, cleanliness, comment, water, soap ->
                viewModel.submitReview(
                    restroomId = selectedRestroom!!.id,
                    authorName = author,
                    rating = rating,
                    cleanlinessRating = cleanliness,
                    comment = comment,
                    hasWaterAvailable = water,
                    hasSoapPaper = soap
                )
            }
        )
    }

    // Community Restroom Contribution Dialog
    if (showAddDialog) {
        AddRestroomDialog(
            onDismiss = { viewModel.setShowAddDialog(false) },
            onSubmit = { name, nameFr, wilaya, commune, type, isFree, price, hasWater, hasSoap, pmr, women, shower, wudu, changingTable, hours, addr, notes ->
                viewModel.addNewRestroom(
                    name, nameFr, wilaya, commune, type, isFree, price,
                    hasWater, hasSoap, pmr, women, shower, wudu, changingTable, 4.5f, hours, addr, notes
                )
            }
        )
    }

    // Project Feasibility Study Sheet
    if (showStudyDialog) {
        ProjectStudySheet(
            sheetState = studySheetState,
            onDismiss = { viewModel.setShowStudyDialog(false) }
        )
    }

    // Offline Cache & Local Storage Management Dialog
    if (showOfflineCacheDialog) {
        OfflineCacheDialog(
            metadata = offlineCacheMetadata,
            totalRestroomsCount = filteredRestrooms.size,
            isOfflineMode = isOfflineMode,
            onToggleOfflineMode = { viewModel.toggleOfflineMode() },
            onRefreshCache = { viewModel.refreshOfflineCache() },
            onDismiss = { viewModel.setShowOfflineCacheDialog(false) }
        )
    }

    // Invalid Google Maps API Key Explanatory Dialog
    if (showInvalidApiKeyDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowInvalidApiKeyDialog(false) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = AlertRed
                )
            },
            title = {
                Text(
                    text = "تنبيه: مفتاح Google Maps غير صالح",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "القيمة الحالية لمفتاح الخرائط هي: \"${com.example.BuildConfig.MAPS_API_KEY}\".",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "مفاتيح Google Maps الرسمية تبدأ دائماً بـ AIzaSy... وتتكون من 39 حرفاً من Google Cloud Console.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "يعمل تطبيق «راحة» حالياً بالخريطة التفاعلية المحلية البديلة 100% بدون إنترنت وبشكل مجاني تماماً وبدون الحاجة لأي مفتاح.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SuccessGreen
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.setShowInvalidApiKeyDialog(false) },
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Text("متابعة بالخريطة التفاعلية (بدون إنترنت)")
                }
            }
        )
    }
}

@Composable
fun EmptyStateNotice(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(TealPrimary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null,
                tint = TealPrimary,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
        )
    }
}
