package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.RestroomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RestroomDao {
    @Query("SELECT * FROM restrooms ORDER BY id ASC")
    fun getAllRestrooms(): Flow<List<RestroomEntity>>

    @Query("SELECT * FROM restrooms WHERE isFavorite = 1 ORDER BY id DESC")
    fun getFavoriteRestrooms(): Flow<List<RestroomEntity>>

    @Query("SELECT * FROM restrooms WHERE id = :id LIMIT 1")
    fun getRestroomById(id: Long): Flow<RestroomEntity?>

    @Query("SELECT * FROM restrooms WHERE id = :id LIMIT 1")
    suspend fun getRestroomByIdOnce(id: Long): RestroomEntity?

    @Query("SELECT * FROM restrooms WHERE isOfflineAvailable = 1 ORDER BY wilaya, name ASC")
    fun getOfflineAvailableRestrooms(): Flow<List<RestroomEntity>>

    @Query("SELECT DISTINCT wilaya FROM restrooms ORDER BY wilaya ASC")
    fun getCachedWilayas(): Flow<List<String>>

    @Query("SELECT COUNT(DISTINCT wilaya) FROM restrooms")
    suspend fun countCachedWilayas(): Int

    @Query("UPDATE restrooms SET lastCachedTimestamp = :timestamp")
    suspend fun updateAllCachedTimestamps(timestamp: Long)

    @Query("SELECT * FROM restrooms WHERE wilaya = :wilaya ORDER BY id ASC")
    fun getRestroomsByWilaya(wilaya: String): Flow<List<RestroomEntity>>

    @Query("SELECT COUNT(*) FROM restrooms")
    suspend fun countRestrooms(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestroom(restroom: RestroomEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(restrooms: List<RestroomEntity>)

    @Update
    suspend fun updateRestroom(restroom: RestroomEntity)

    @Query("UPDATE restrooms SET isFavorite = :isFav WHERE id = :id")
    suspend fun setFavorite(id: Long, isFav: Boolean)

    @Query("UPDATE restrooms SET waterCutReported = :waterCut WHERE id = :id")
    suspend fun setWaterCutStatus(id: Long, waterCut: Boolean)

    @Query("UPDATE restrooms SET rating = :rating, reviewsCount = reviewsCount + 1 WHERE id = :id")
    suspend fun addReviewRating(id: Long, rating: Float)

    @Query("UPDATE restrooms SET rating = :rating, cleanlinessRating = :cleanlinessRating, reviewsCount = :reviewsCount WHERE id = :id")
    suspend fun updateRatingsAndReviewsCount(id: Long, rating: Float, cleanlinessRating: Float, reviewsCount: Int)

    @Query("DELETE FROM restrooms WHERE id = :id")
    suspend fun deleteRestroom(id: Long)
}
