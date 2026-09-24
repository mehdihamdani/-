package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RestroomEntity
import com.example.data.model.UserLocation
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.WaterBlue
import com.example.ui.viewmodel.RestroomWithDistance
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import com.example.ui.theme.RahaBlue
import com.example.ui.theme.RahaGreen
import com.example.ui.viewmodel.FilterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveRestroomMap(
    userLocation: UserLocation,
    restrooms: List<RestroomWithDistance>,
    selectedRestroom: RestroomEntity?,
    filterState: FilterState? = null,
    onRestroomSelected: (RestroomEntity?) -> Unit,
    onNavigateClick: (RestroomEntity) -> Unit,
    onWilayaChange: (String, Double, Double) -> Unit,
    onSwitchToGoogleMap: (() -> Unit)? = null,
    onLocateUserClick: (() -> Unit)? = null,
    isLocating: Boolean = false,
    hasLocationPermission: Boolean = true,
    onRequestPermission: (() -> Unit)? = null,
    onToggleAccessible: (() -> Unit)? = null,
    onToggleCleanliness: (() -> Unit)? = null,
    onToggleChangingTable: (() -> Unit)? = null,
    onToggleFreeOnly: (() -> Unit)? = null,
    onToggleWomenOnly: (() -> Unit)? = null,
    onToggleWuduOnly: (() -> Unit)? = null,
    onToggleShowerOnly: (() -> Unit)? = null,
    onToggleMosques: (() -> Unit)? = null,
    onToggleRestAreas: (() -> Unit)? = null,
    onTogglePublicToilets: (() -> Unit)? = null,
    onResetAll: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var panOffsetY by remember { mutableFloatStateOf(0f) }
    var zoomScale by remember { mutableFloatStateOf(1.2f) }
    var showWilayaMenu by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    val filterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = 65f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseRadius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    val algerianCities = listOf(
        Triple("الجزائر العاصمة (Alger)", 36.7538, 3.0588),
        Triple("وهران (Oran)", 35.6987, -0.6345),
        Triple("قسنطينة (Constantine)", 36.3498, 6.6192),
        Triple("سطيف (Sétif)", 36.1912, 5.4078),
        Triple("عنابة (Annaba)", 36.8870, 7.7450),
        Triple("البليدة (Blida)", 36.4700, 2.8300),
        Triple("تلمسان (Tlemcen)", 34.8783, -1.3150)
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F1A20))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    panOffsetX += dragAmount.x
                    panOffsetY += dragAmount.y
                }
            }
            .testTag("interactive_map_canvas")
    ) {
        val canvasWidth = constraints.maxWidth.toFloat()
        val canvasHeight = constraints.maxHeight.toFloat()
        val centerX = canvasWidth / 2f + panOffsetX
        val centerY = canvasHeight / 2f + panOffsetY

        // Draw Map Grid and radar
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridColor = Color(0xFF1E323D)
            val roadColor = Color(0xFF223C4A)
            val coastlineColor = Color(0xFF16425B)

            // Coastline curve decorative banner (simulating Algerian Mediterranean Coast)
            val coastPath = Path().apply {
                moveTo(0f, centerY - 140f * zoomScale)
                cubicTo(
                    canvasWidth * 0.3f, centerY - 180f * zoomScale,
                    canvasWidth * 0.7f, centerY - 100f * zoomScale,
                    canvasWidth, centerY - 160f * zoomScale
                )
                lineTo(canvasWidth, 0f)
                lineTo(0f, 0f)
                close()
            }
            drawPath(
                path = coastPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0B2533), Color(0xFF10364A))
                )
            )

            // Grid lines
            val step = 60f * zoomScale
            var x = (centerX % step)
            while (x < canvasWidth) {
                drawLine(
                    color = gridColor.copy(alpha = 0.4f),
                    start = Offset(x, 0f),
                    end = Offset(x, canvasHeight),
                    strokeWidth = 1f
                )
                x += step
            }
            var y = (centerY % step)
            while (y < canvasHeight) {
                drawLine(
                    color = gridColor.copy(alpha = 0.4f),
                    start = Offset(0f, y),
                    end = Offset(canvasWidth, y),
                    strokeWidth = 1f
                )
                y += step
            }

            // Simulated Highways (East-West Highway & City Arterials)
            drawLine(
                color = roadColor,
                start = Offset(0f, centerY + 80f * zoomScale),
                end = Offset(canvasWidth, centerY + 60f * zoomScale),
                strokeWidth = 4f * zoomScale
            )
            drawLine(
                color = roadColor,
                start = Offset(centerX - 100f * zoomScale, 0f),
                end = Offset(centerX + 60f * zoomScale, canvasHeight),
                strokeWidth = 3f * zoomScale
            )

            // Radar rings around user
            val ringRadii = listOf(80f, 160f, 260f, 380f)
            ringRadii.forEach { r ->
                drawCircle(
                    color = WaterBlue.copy(alpha = 0.15f),
                    radius = r * zoomScale,
                    center = Offset(centerX, centerY),
                    style = Stroke(width = 1.5f)
                )
            }

            // Radar sweep pulse animation
            drawCircle(
                color = WaterBlue.copy(alpha = pulseAlpha),
                radius = pulseRadius * zoomScale,
                center = Offset(centerX, centerY),
                style = Stroke(width = 2.5f)
            )

            // User location center dot
            drawCircle(
                color = Color.White,
                radius = 7f * zoomScale,
                center = Offset(centerX, centerY)
            )
            drawCircle(
                color = TealPrimary,
                radius = 5f * zoomScale,
                center = Offset(centerX, centerY)
            )
        }

        // Map Pins for Restrooms
        // Conversion scale: 1 degree latitude ~= 111 km, longitude ~= 90 km
        // Scale to pixels based on zoom
        val latScale = 2200f * zoomScale
        val lonScale = 2200f * zoomScale

        restrooms.forEach { item ->
            val restroom = item.restroom
            // Delta from user location
            val dLat = (restroom.latitude - userLocation.latitude).toFloat()
            val dLon = (restroom.longitude - userLocation.longitude).toFloat()

            // In screen coordinates, positive latitude goes UP (-Y), positive lon goes RIGHT (+X)
            val pinX = centerX + (dLon * lonScale)
            val pinY = centerY - (dLat * latScale)

            // Check if within bounds
            if (pinX in -50f..(canvasWidth + 50f) && pinY in -50f..(canvasHeight + 50f)) {
                val isSelected = selectedRestroom?.id == restroom.id
                val color = getTypeColor(restroom.type)
                val icon = getTypeIcon(restroom.type)

                Box(
                    modifier = Modifier
                        .offset { IntOffset((pinX - 22.dp.toPx()).roundToInt(), (pinY - 44.dp.toPx()).roundToInt()) }
                        .clickable { onRestroomSelected(if (isSelected) null else restroom) }
                        .testTag("pin_${restroom.id}")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Pin Head Bubble
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 46.dp else 36.dp)
                                .shadow(8.dp, CircleShape)
                                .clip(CircleShape)
                                .background(if (isSelected) Color.White else color)
                                .border(
                                    width = if (isSelected) 3.dp else 1.5.dp,
                                    color = if (isSelected) color else Color.White,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = restroom.name,
                                tint = if (isSelected) color else Color.White,
                                modifier = Modifier.size(if (isSelected) 24.dp else 18.dp)
                            )
                        }

                        // Pin Needle
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(8.dp)
                                .background(if (isSelected) Color.White else color)
                        )

                        // Distance label
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.Black.copy(alpha = 0.75f),
                            modifier = Modifier.padding(top = 1.dp)
                        ) {
                            Text(
                                text = item.formattedDistance,
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }
        }

        // Top Overlay Header: City Selector + Offline Badge + Google Maps switcher + Filter Chips
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 14.dp, start = 12.dp, end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    shadowElevation = 6.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { showWilayaMenu = true }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = null,
                            tint = TealPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "المدينة: ${userLocation.cityName}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )

                        DropdownMenu(
                            expanded = showWilayaMenu,
                            onDismissRequest = { showWilayaMenu = false }
                        ) {
                            algerianCities.forEach { (city, lat, lng) ->
                                DropdownMenuItem(
                                    text = { Text(city) },
                                    onClick = {
                                        showWilayaMenu = false
                                        panOffsetX = 0f
                                        panOffsetY = 0f
                                        onWilayaChange(city, lat, lng)
                                    }
                                )
                            }
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Offline Room Cache Indicator Badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF1E392A).copy(alpha = 0.92f),
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = "محفوظ محلياً بدون إنترنت",
                                tint = Color(0xFF52B788),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "أوفلاين",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = Color(0xFFD8F3DC)
                            )
                        }
                    }

                    if (onSwitchToGoogleMap != null) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                            shadowElevation = 4.dp
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { onSwitchToGoogleMap() }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Map,
                                    contentDescription = "التبديل إلى خرائط Google",
                                    tint = RahaBlue,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "خرائط Google",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Quick Map Filter Chips Row
            if (filterState != null) {
                MapQuickFilterChips(
                    filterState = filterState,
                    matchCount = restrooms.size,
                    onOpenFilterSheet = { showFilterSheet = true },
                    onToggleAccessible = { onToggleAccessible?.invoke() },
                    onToggleCleanliness = { onToggleCleanliness?.invoke() },
                    onToggleChangingTable = { onToggleChangingTable?.invoke() },
                    onToggleFreeOnly = { onToggleFreeOnly?.invoke() },
                    onResetAll = { onResetAll?.invoke() }
                )
            }

            // Status or No Match Warning
            if (restrooms.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.95f),
                    shadowElevation = 3.dp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "⚠️ لا توجد مراحيض تطابق هذه الفلاتر",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "إلغاء الفلاتر",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.clickable { onResetAll?.invoke() }
                        )
                    }
                }
            }
        }

        // Floating Map Controls: Locate Me, Zoom in, Zoom out, Recenter
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Locate Me Button (using play-services-location FusedLocationProviderClient)
            if (onLocateUserClick != null) {
                FilledIconButton(
                    onClick = {
                        if (hasLocationPermission) {
                            onLocateUserClick()
                        } else {
                            onRequestPermission?.invoke()
                        }
                    },
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (hasLocationPermission) RahaGreen else RahaBlue
                    ),
                    modifier = Modifier
                        .size(46.dp)
                        .testTag("interactive_locate_me_button")
                ) {
                    if (isLocating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "تحديد موقعي الحالي عبر GPS",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            FilledIconButton(
                onClick = { zoomScale = (zoomScale * 1.3f).coerceAtMost(3.0f) },
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ),
                modifier = Modifier.size(42.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "تكبير الخريطة",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            FilledIconButton(
                onClick = { zoomScale = (zoomScale / 1.3f).coerceAtLeast(0.6f) },
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ),
                modifier = Modifier.size(42.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "تصغير الخريطة",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            FilledIconButton(
                onClick = {
                    panOffsetX = 0f
                    panOffsetY = 0f
                    zoomScale = 1.2f
                },
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = TealPrimary
                ),
                modifier = Modifier.size(42.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationSearching,
                    contentDescription = "إعادة ضبط الموقع",
                    tint = Color.White
                )
            }
        }

        // Bottom Selected Restroom Card preview (if a pin is selected)
        if (selectedRestroom != null) {
            val distItem = restrooms.find { it.restroom.id == selectedRestroom.id }
            Card(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 100.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                                    .background(getTypeColor(selectedRestroom.type).copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = getTypeIcon(selectedRestroom.type),
                                    contentDescription = null,
                                    tint = getTypeColor(selectedRestroom.type),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = selectedRestroom.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${selectedRestroom.wilaya} • ${selectedRestroom.commune} • ${distItem?.formattedDistance ?: ""}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(
                            onClick = { onRestroomSelected(null) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusBadge(
                            text = if (selectedRestroom.isFree) "مجاني" else "${selectedRestroom.priceDzd} دج",
                            color = if (selectedRestroom.isFree) Color(0xFF2B9348) else MaterialTheme.colorScheme.tertiary
                        )
                        StatusBadge(
                            text = "${selectedRestroom.rating} ★",
                            color = Color(0xFFE67E22)
                        )
                        if (selectedRestroom.hasWater && !selectedRestroom.waterCutReported) {
                            StatusBadge(text = "ماء متوفر", color = WaterBlue)
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        TextButton(
                            onClick = { onNavigateClick(selectedRestroom) }
                        ) {
                            Icon(imageVector = Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("انطلق", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Modal Filter Bottom Sheet
        if (showFilterSheet && filterState != null) {
            MapFilterSheet(
                filterState = filterState,
                matchCount = restrooms.size,
                sheetState = filterSheetState,
                onDismiss = { showFilterSheet = false },
                onToggleAccessible = { onToggleAccessible?.invoke() },
                onToggleCleanliness = { onToggleCleanliness?.invoke() },
                onToggleChangingTable = { onToggleChangingTable?.invoke() },
                onToggleFreeOnly = { onToggleFreeOnly?.invoke() },
                onToggleWomenOnly = { onToggleWomenOnly?.invoke() },
                onToggleWuduOnly = { onToggleWuduOnly?.invoke() },
                onToggleShowerOnly = { onToggleShowerOnly?.invoke() },
                onToggleMosques = { onToggleMosques?.invoke() },
                onToggleRestAreas = { onToggleRestAreas?.invoke() },
                onTogglePublicToilets = { onTogglePublicToilets?.invoke() },
                onResetAll = { onResetAll?.invoke() }
            )
        }
    }
}
