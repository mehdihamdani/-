package com.example.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.OfflineCacheMetadataEntity
import com.example.data.model.RestroomEntity
import com.example.data.model.RestroomType
import com.example.data.model.ReviewEntity
import com.example.data.model.UserLocation
import com.example.data.repository.RestroomRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class RestroomWithDistance(
    val restroom: RestroomEntity,
    val distanceMeters: Double,
    val formattedDistance: String
)

data class FilterState(
    val searchQuery: String = "",
    val selectedWilaya: String = "الكل",
    val selectedType: RestroomType? = null,
    val showMosques: Boolean = true,
    val showPublicToilets: Boolean = true,
    val showRestAreas: Boolean = true,
    val freeOnly: Boolean = false,
    val accessiblePmrOnly: Boolean = false,
    val highCleanlinessOnly: Boolean = false,
    val changingTableOnly: Boolean = false,
    val womenSectionOnly: Boolean = false,
    val wuduOnly: Boolean = false,
    val showerOnly: Boolean = false,
    val favoritesOnly: Boolean = false
) {
    val activeFilterCount: Int
        get() {
            var count = 0
            if (selectedWilaya != "الكل") count++
            if (selectedType != null) count++
            if (!showMosques || !showPublicToilets || !showRestAreas) count++
            if (freeOnly) count++
            if (accessiblePmrOnly) count++
            if (highCleanlinessOnly) count++
            if (changingTableOnly) count++
            if (womenSectionOnly) count++
            if (wuduOnly) count++
            if (showerOnly) count++
            if (favoritesOnly) count++
            return count
        }
}

enum class MapProviderType {
    GOOGLE_MAPS,
    OFFLINE_VECTOR
}

class RestroomViewModel(private val repository: RestroomRepository) : ViewModel() {

    private val _userLocation = MutableStateFlow(
        UserLocation(36.7538, 3.0588, "الجزائر العاصمة (وسط)")
    )
    val userLocation: StateFlow<UserLocation> = _userLocation

    private val _mapProvider = MutableStateFlow(
        if (com.example.util.MapsKeyValidator.isKeyValid()) MapProviderType.GOOGLE_MAPS else MapProviderType.OFFLINE_VECTOR
    )
    val mapProvider: StateFlow<MapProviderType> = _mapProvider

    private val _showInvalidApiKeyDialog = MutableStateFlow(false)
    val showInvalidApiKeyDialog: StateFlow<Boolean> = _showInvalidApiKeyDialog

    fun setShowInvalidApiKeyDialog(show: Boolean) {
        _showInvalidApiKeyDialog.value = show
    }

    private val _isLocating = MutableStateFlow(false)
    val isLocating: StateFlow<Boolean> = _isLocating

    private val _locationMessage = MutableStateFlow<String?>(null)
    val locationMessage: StateFlow<String?> = _locationMessage

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState

    private val _selectedRestroom = MutableStateFlow<RestroomEntity?>(null)
    val selectedRestroom: StateFlow<RestroomEntity?> = _selectedRestroom

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedRestroomReviews: StateFlow<List<ReviewEntity>> = _selectedRestroom
        .flatMapLatest { restroom ->
            if (restroom != null) {
                repository.getReviewsForRestroom(restroom.id)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog

    private val _showStudyDialog = MutableStateFlow(false)
    val showStudyDialog: StateFlow<Boolean> = _showStudyDialog

    private val _isOfflineReady = MutableStateFlow(true)
    val isOfflineReady: StateFlow<Boolean> = _isOfflineReady

    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode

    private val _showOfflineCacheDialog = MutableStateFlow(false)
    val showOfflineCacheDialog: StateFlow<Boolean> = _showOfflineCacheDialog

    val offlineCacheMetadata: StateFlow<OfflineCacheMetadataEntity?> = repository.cacheMetadataFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
            // Restore cached location from Room if available
            val cachedLoc = repository.getCachedLocationOnce()
            if (cachedLoc != null) {
                _userLocation.value = UserLocation(
                    latitude = cachedLoc.latitude,
                    longitude = cachedLoc.longitude,
                    cityName = cachedLoc.cityName
                )
            }
        }
    }

    // Combine raw restrooms with filters and user location
    val filteredRestrooms: StateFlow<List<RestroomWithDistance>> = combine(
        repository.allRestrooms,
        _filterState,
        _userLocation
    ) { restrooms, filters, location ->
        filterRestroomList(restrooms, filters, location)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun applyCurrentFilters(items: List<RestroomEntity>): List<RestroomWithDistance> {
        return filterRestroomList(items, _filterState.value, _userLocation.value)
    }

    val nearestRestroom: StateFlow<RestroomWithDistance?> = combine(filteredRestrooms) { list ->
        list.firstOrNull()?.firstOrNull()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun setSearchQuery(query: String) {
        _filterState.value = _filterState.value.copy(searchQuery = query)
    }

    fun setSelectedWilaya(wilaya: String) {
        _filterState.value = _filterState.value.copy(selectedWilaya = wilaya)
    }

    fun setSelectedType(type: RestroomType?) {
        _filterState.value = _filterState.value.copy(
            selectedType = if (_filterState.value.selectedType == type) null else type
        )
    }

    fun toggleMosquesVisibility() {
        val current = _filterState.value.showMosques
        // If turning off would leave all 3 turned off, keep it or allow toggling
        _filterState.value = _filterState.value.copy(showMosques = !current)
    }

    fun togglePublicToiletsVisibility() {
        val current = _filterState.value.showPublicToilets
        _filterState.value = _filterState.value.copy(showPublicToilets = !current)
    }

    fun toggleRestAreasVisibility() {
        val current = _filterState.value.showRestAreas
        _filterState.value = _filterState.value.copy(showRestAreas = !current)
    }

    fun resetVisibilityToggles() {
        _filterState.value = _filterState.value.copy(
            showMosques = true,
            showPublicToilets = true,
            showRestAreas = true
        )
    }

    fun toggleFreeOnly() {
        _filterState.value = _filterState.value.copy(freeOnly = !_filterState.value.freeOnly)
    }

    fun toggleAccessibleOnly() {
        _filterState.value = _filterState.value.copy(accessiblePmrOnly = !_filterState.value.accessiblePmrOnly)
    }

    fun toggleCleanlinessOnly() {
        _filterState.value = _filterState.value.copy(highCleanlinessOnly = !_filterState.value.highCleanlinessOnly)
    }

    fun toggleChangingTableOnly() {
        _filterState.value = _filterState.value.copy(changingTableOnly = !_filterState.value.changingTableOnly)
    }

    fun resetAllFilters() {
        _filterState.value = FilterState(
            searchQuery = _filterState.value.searchQuery,
            selectedWilaya = "الكل"
        )
    }

    fun toggleWomenOnly() {
        _filterState.value = _filterState.value.copy(womenSectionOnly = !_filterState.value.womenSectionOnly)
    }

    fun toggleWuduOnly() {
        _filterState.value = _filterState.value.copy(wuduOnly = !_filterState.value.wuduOnly)
    }

    fun toggleShowerOnly() {
        _filterState.value = _filterState.value.copy(showerOnly = !_filterState.value.showerOnly)
    }

    fun toggleFavoritesOnly() {
        _filterState.value = _filterState.value.copy(favoritesOnly = !_filterState.value.favoritesOnly)
    }

    fun selectRestroom(restroom: RestroomEntity?) {
        _selectedRestroom.value = restroom
    }

    fun setShowAddDialog(show: Boolean) {
        _showAddDialog.value = show
    }

    fun setShowStudyDialog(show: Boolean) {
        _showStudyDialog.value = show
    }

    fun setShowOfflineCacheDialog(show: Boolean) {
        _showOfflineCacheDialog.value = show
    }

    fun toggleOfflineMode() {
        val newMode = !_isOfflineMode.value
        _isOfflineMode.value = newMode
        if (newMode) {
            // When user switches to offline mode, prefer the vector offline map provider
            _mapProvider.value = MapProviderType.OFFLINE_VECTOR
        }
    }

    fun refreshOfflineCache() {
        viewModelScope.launch {
            repository.refreshOfflineCache()
        }
    }

    fun setUserLocation(lat: Double, lng: Double, cityName: String) {
        _userLocation.value = UserLocation(lat, lng, cityName)
        viewModelScope.launch {
            repository.saveCachedLocation(lat, lng, cityName)
        }
    }

    fun setMapProvider(provider: MapProviderType) {
        if (provider == MapProviderType.GOOGLE_MAPS && !com.example.util.MapsKeyValidator.isKeyValid()) {
            _showInvalidApiKeyDialog.value = true
            _mapProvider.value = MapProviderType.OFFLINE_VECTOR
        } else {
            _mapProvider.value = provider
        }
    }

    fun requestGpsLocation(locationHelper: com.example.data.location.LocationHelper) {
        _isLocating.value = true
        locationHelper.getCurrentLocationHighAccuracy(
            onSuccess = { loc ->
                _isLocating.value = false
                setUserLocation(loc.latitude, loc.longitude, loc.cityName)
                _locationMessage.value = "تم تحديد موقعك بدقة: ${loc.cityName}"
            },
            onError = {
                _isLocating.value = false
                _locationMessage.value = "تم استخدام آخر موقع معروف."
            }
        )
    }

    fun clearLocationMessage() {
        _locationMessage.value = null
    }

    fun toggleFavorite(restroom: RestroomEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(restroom.id, !restroom.isFavorite)
            if (_selectedRestroom.value?.id == restroom.id) {
                _selectedRestroom.value = _selectedRestroom.value?.copy(isFavorite = !restroom.isFavorite)
            }
        }
    }

    fun reportWaterCut(restroom: RestroomEntity, isCut: Boolean) {
        viewModelScope.launch {
            repository.setWaterCutStatus(restroom.id, isCut)
            if (_selectedRestroom.value?.id == restroom.id) {
                _selectedRestroom.value = _selectedRestroom.value?.copy(waterCutReported = isCut)
            }
        }
    }

    fun rateRestroom(restroom: RestroomEntity, rating: Float) {
        viewModelScope.launch {
            repository.submitRating(restroom.id, rating)
        }
    }

    fun submitReview(
        restroomId: Long,
        authorName: String,
        rating: Float,
        cleanlinessRating: Float,
        comment: String,
        hasWaterAvailable: Boolean,
        hasSoapPaper: Boolean
    ) {
        viewModelScope.launch {
            repository.submitReview(
                restroomId = restroomId,
                authorName = authorName,
                rating = rating,
                cleanlinessRating = cleanlinessRating,
                comment = comment,
                hasWaterAvailable = hasWaterAvailable,
                hasSoapPaper = hasSoapPaper
            )
            val updated = repository.getRestroomById(restroomId)
            if (updated != null && _selectedRestroom.value?.id == restroomId) {
                _selectedRestroom.value = updated
            }
        }
    }

    fun addNewRestroom(
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
        hasChangingTable: Boolean = false,
        cleanlinessRating: Float = 4.5f,
        openingHours: String,
        address: String,
        notes: String
    ) {
        viewModelScope.launch {
            val userLoc = _userLocation.value
            val newRestroom = RestroomEntity(
                name = name,
                nameFr = nameFr,
                wilaya = wilaya,
                commune = commune,
                type = type,
                latitude = userLoc.latitude + (Math.random() - 0.5) * 0.015,
                longitude = userLoc.longitude + (Math.random() - 0.5) * 0.015,
                isFree = isFree,
                priceDzd = priceDzd,
                rating = cleanlinessRating,
                reviewsCount = 1,
                hasWater = hasWater,
                hasSoapPaper = hasSoapPaper,
                isAccessiblePmr = isAccessiblePmr,
                hasWomenSection = hasWomenSection,
                hasShower = hasShower,
                hasWudu = hasWudu,
                hasChangingTable = hasChangingTable,
                cleanlinessRating = cleanlinessRating,
                openingHours = openingHours.ifBlank { "24/7" },
                address = address,
                notes = notes,
                isUserAdded = true
            )
            repository.addRestroom(newRestroom)
            _showAddDialog.value = false
        }
    }

    fun openInGoogleMaps(context: Context, restroom: RestroomEntity) {
        try {
            val gmmIntentUri = Uri.parse("geo:${restroom.latitude},${restroom.longitude}?q=${restroom.latitude},${restroom.longitude}(${Uri.encode(restroom.name)})")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
            } else {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=${restroom.latitude},${restroom.longitude}"))
                context.startActivity(browserIntent)
            }
        } catch (e: Exception) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=${restroom.latitude},${restroom.longitude}"))
            context.startActivity(browserIntent)
        }
    }

    fun shareRestroom(context: Context, restroom: RestroomEntity) {
        val shareText = "📍 ${restroom.name}\nالولاية: ${restroom.wilaya} (${restroom.commune})\nالنوع: ${restroom.type.arabicName}\nالخدمات: ${if (restroom.isFree) "مجاني" else "${restroom.priceDzd} دج"}\nالموقع: https://maps.google.com/?q=${restroom.latitude},${restroom.longitude}"
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "مشاركة موقع المرحاض / الحمام"))
    }

    companion object {
        fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val r = 6371000.0 // Earth radius in meters
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                    Math.sin(dLon / 2) * Math.sin(dLon / 2)
            val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
            return r * c
        }

        fun formatDistance(meters: Double): String {
            return if (meters < 1000) {
                "${meters.toInt()} متر"
            } else {
                String.format("%.1f كم", meters / 1000)
            }
        }

        fun filterRestroomList(
            restrooms: List<RestroomEntity>,
            filters: FilterState,
            location: UserLocation
        ): List<RestroomWithDistance> {
            return restrooms
                .filter { item ->
                    // Favorites filter
                    if (filters.favoritesOnly && !item.isFavorite) return@filter false
                    // Wilaya filter
                    if (filters.selectedWilaya != "الكل" && !item.wilaya.contains(filters.selectedWilaya)) return@filter false
                    // Category filter
                    if (filters.selectedType != null && item.type != filters.selectedType) return@filter false
                    // Group visibility toggles (Mosques, Public Toilets, Rest Areas)
                    val isMosque = item.type == RestroomType.MOSQUE
                    val isPublicToilet = item.type == RestroomType.PUBLIC_MUNICIPAL || item.type == RestroomType.MALL || item.type == RestroomType.HAMMAM || item.type == RestroomType.PRIVATE_COMMERCIAL
                    val isRestArea = item.type == RestroomType.NAFTAL_HIGHWAY || item.type == RestroomType.TRANSPORT_HUB

                    if (isMosque && !filters.showMosques) return@filter false
                    if (isPublicToilet && !filters.showPublicToilets) return@filter false
                    if (isRestArea && !filters.showRestAreas) return@filter false
                    // Boolean amenities
                    if (filters.freeOnly && !item.isFree) return@filter false
                    if (filters.accessiblePmrOnly && !item.isAccessiblePmr) return@filter false
                    if (filters.highCleanlinessOnly && (item.rating < 4.2f || item.cleanlinessRating < 4.2f || !item.hasWater || item.waterCutReported)) return@filter false
                    if (filters.changingTableOnly && !item.hasChangingTable) return@filter false
                    if (filters.womenSectionOnly && !item.hasWomenSection) return@filter false
                    if (filters.wuduOnly && !item.hasWudu) return@filter false
                    if (filters.showerOnly && !item.hasShower) return@filter false
                    // Search query
                    if (filters.searchQuery.isNotBlank()) {
                        val q = filters.searchQuery.trim().lowercase()
                        val matchName = item.name.lowercase().contains(q) || item.nameFr.lowercase().contains(q)
                        val matchPlace = item.wilaya.lowercase().contains(q) || item.commune.lowercase().contains(q)
                        val matchNotes = item.notes.lowercase().contains(q) || item.address.lowercase().contains(q)
                        if (!matchName && !matchPlace && !matchNotes) return@filter false
                    }
                    true
                }
                .map { item ->
                    val dist = calculateDistance(location.latitude, location.longitude, item.latitude, item.longitude)
                    RestroomWithDistance(
                        restroom = item,
                        distanceMeters = dist,
                        formattedDistance = formatDistance(dist)
                    )
                }
                .sortedBy { it.distanceMeters }
        }
    }
}

class RestroomViewModelFactory(private val repository: RestroomRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RestroomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RestroomViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
