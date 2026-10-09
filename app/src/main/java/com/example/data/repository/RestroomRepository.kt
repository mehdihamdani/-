package com.example.data.repository

import com.example.data.local.CachedLocationDao
import com.example.data.local.InitialRestroomsData
import com.example.data.local.OfflineCacheMetadataDao
import com.example.data.local.RestroomDao
import com.example.data.local.ReviewDao
import com.example.data.model.CachedUserLocationEntity
import com.example.data.model.OfflineCacheMetadataEntity
import com.example.data.model.RestroomEntity
import com.example.data.model.ReviewEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext

class RestroomRepository(
    private val dao: RestroomDao,
    private val cachedLocationDao: CachedLocationDao,
    private val cacheMetadataDao: OfflineCacheMetadataDao? = null,
    private val reviewDao: ReviewDao? = null
) {

    val allRestrooms: Flow<List<RestroomEntity>> = dao.getAllRestrooms()
    val favoriteRestrooms: Flow<List<RestroomEntity>> = dao.getFavoriteRestrooms()
    val offlineRestrooms: Flow<List<RestroomEntity>> = dao.getOfflineAvailableRestrooms()
    val cachedLocationFlow: Flow<CachedUserLocationEntity?> = cachedLocationDao.getCachedLocationFlow()
    val cacheMetadataFlow: Flow<OfflineCacheMetadataEntity?> = cacheMetadataDao?.getMetadataFlow() ?: emptyFlow()

    fun getReviewsForRestroom(restroomId: Long): Flow<List<ReviewEntity>> {
        return reviewDao?.getReviewsForRestroom(restroomId) ?: emptyFlow()
    }

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

        // Seed initial community reviews if none exist
        if (reviewDao != null && reviewDao.countAllReviews() == 0) {
            reviewDao.insertAll(InitialRestroomsData.sampleReviews)
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

    suspend fun submitReview(
        restroomId: Long,
        authorName: String,
        rating: Float,
        cleanlinessRating: Float,
        comment: String,
        hasWaterAvailable: Boolean,
        hasSoapPaper: Boolean
    ): Long = withContext(Dispatchers.IO) {
        val review = ReviewEntity(
            restroomId = restroomId,
            authorName = authorName.ifBlank { "مواطن" },
            rating = rating.coerceIn(1.0f, 5.0f),
            cleanlinessRating = cleanlinessRating.coerceIn(1.0f, 5.0f),
            comment = comment.trim(),
            hasWaterAvailable = hasWaterAvailable,
            hasSoapPaper = hasSoapPaper,
            timestamp = System.currentTimeMillis()
        )
        val insertedId = reviewDao?.insertReview(review) ?: 0L

        // Recompute average overall rating and cleanliness rating from database
        if (reviewDao != null) {
            val avgRating = reviewDao.getAverageRating(restroomId) ?: rating
            val avgCleanliness = reviewDao.getAverageCleanlinessRating(restroomId) ?: cleanlinessRating
            val count = reviewDao.countReviewsForRestroom(restroomId)
            val roundedRating = Math.round(avgRating * 10f) / 10f
            val roundedCleanliness = Math.round(avgCleanliness * 10f) / 10f
            dao.updateRatingsAndReviewsCount(
                id = restroomId,
                rating = roundedRating,
                cleanlinessRating = roundedCleanliness,
                reviewsCount = count
            )
        } else {
            dao.addReviewRating(restroomId, rating)
        }
        insertedId
    }

    suspend fun deleteRestroom(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteRestroom(id)
    }
}
