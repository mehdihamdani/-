package com.example.data.repository

import com.example.data.local.CachedLocationDao
import com.example.data.local.InitialRestroomsData
import com.example.data.local.OfflineCacheMetadataDao
import com.example.data.local.RestroomDao
import com.example.data.model.CachedUserLocationEntity
import com.example.data.model.OfflineCacheMetadataEntity
import com.example.data.model.RestroomEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext

class RestroomRepository(
    private val dao: RestroomDao,
    private val cachedLocationDao: CachedLocationDao,
    private val cacheMetadataDao: OfflineCacheMetadataDao? = null
) {

    val allRestrooms: Flow<List<RestroomEntity>> = dao.getAllRestrooms()
    val favoriteRestrooms: Flow<List<RestroomEntity>> = dao.getFavoriteRestrooms()
    val offlineRestrooms: Flow<List<RestroomEntity>> = dao.getOfflineAvailableRestrooms()
    val cachedLocationFlow: Flow<CachedUserLocationEntity?> = cachedLocationDao.getCachedLocationFlow()
    val cacheMetadataFlow: Flow<OfflineCacheMetadataEntity?> = cacheMetadataDao?.getMetadataFlow() ?: emptyFlow()

    suspend fun checkAndSeedInitialData() = withContext(Dispatchers.IO) {
        val count = dao.countRestrooms()
        if (count == 0) {
            val items = InitialRestroomsData.sampleAlgerianRestrooms
            dao.insertAll(items)
            updateOfflineCacheStats(items.size)
        } else {
            // Ensure metadata exists
            if (cacheMetadataDao != null && cacheMetadataDao.getMetadataOnce() == null) {
                updateOfflineCacheStats(count)
            }
        }
    }

    suspend fun refreshOfflineCache() = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        dao.updateAllCachedTimestamps(now)
        val count = dao.countRestrooms()
        updateOfflineCacheStats(count, now)
    }

    suspend fun cacheRestStops(items: List<RestroomEntity>) = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val stamped = items.map { it.copy(lastCachedTimestamp = now, isOfflineAvailable = true) }
        dao.insertAll(stamped)
        val total = dao.countRestrooms()
        updateOfflineCacheStats(total, now)
    }

    private suspend fun updateOfflineCacheStats(count: Int, timestamp: Long = System.currentTimeMillis()) {
        val wilayasCount = dao.countCachedWilayas()
        cacheMetadataDao?.saveMetadata(
            OfflineCacheMetadataEntity(
                id = 1,
                totalRestStopsCached = count,
                lastCachedTimestamp = timestamp,
                cachedWilayasCount = wilayasCount,
                isOfflineReady = true,
                statusMessage = "تم حفظ $count مرفقاً و $wilayasCount ولاية محلياً في قاعدة بيانات Room"
            )
        )
    }

    suspend fun getOfflineMetadataOnce(): OfflineCacheMetadataEntity? = withContext(Dispatchers.IO) {
        cacheMetadataDao?.getMetadataOnce()
    }

    suspend fun getRestroomById(id: Long): RestroomEntity? = withContext(Dispatchers.IO) {
        dao.getRestroomByIdOnce(id)
    }

    suspend fun getCachedLocationOnce(): CachedUserLocationEntity? = withContext(Dispatchers.IO) {
        cachedLocationDao.getCachedLocationOnce()
    }

    suspend fun saveCachedLocation(lat: Double, lng: Double, cityName: String) = withContext(Dispatchers.IO) {
        cachedLocationDao.saveCachedLocation(
            CachedUserLocationEntity(
                id = 1,
                latitude = lat,
                longitude = lng,
                cityName = cityName,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun addRestroom(restroom: RestroomEntity): Long = withContext(Dispatchers.IO) {
        dao.insertRestroom(restroom)
    }

    suspend fun toggleFavorite(id: Long, isFav: Boolean) = withContext(Dispatchers.IO) {
        dao.setFavorite(id, isFav)
    }

    suspend fun setWaterCutStatus(id: Long, isCut: Boolean) = withContext(Dispatchers.IO) {
        dao.setWaterCutStatus(id, isCut)
    }

    suspend fun submitRating(id: Long, rating: Float) = withContext(Dispatchers.IO) {
        dao.addReviewRating(id, rating)
    }

    suspend fun deleteRestroom(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteRestroom(id)
    }
}
