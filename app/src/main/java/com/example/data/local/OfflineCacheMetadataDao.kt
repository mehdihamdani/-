package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.OfflineCacheMetadataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OfflineCacheMetadataDao {

    @Query("SELECT * FROM offline_cache_metadata WHERE id = 1 LIMIT 1")
    fun getMetadataFlow(): Flow<OfflineCacheMetadataEntity?>

    @Query("SELECT * FROM offline_cache_metadata WHERE id = 1 LIMIT 1")
    suspend fun getMetadataOnce(): OfflineCacheMetadataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMetadata(metadata: OfflineCacheMetadataEntity)

    @Query("UPDATE offline_cache_metadata SET totalRestStopsCached = :count, lastCachedTimestamp = :timestamp WHERE id = 1")
    suspend fun updateCacheStats(count: Int, timestamp: Long)
}
