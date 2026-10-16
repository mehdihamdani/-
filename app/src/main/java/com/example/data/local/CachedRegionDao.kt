package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CachedRegionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedRegionDao {

    @Query("SELECT * FROM cached_regions ORDER BY regionId ASC")
    fun getAllCachedRegionsFlow(): Flow<List<CachedRegionEntity>>

    @Query("SELECT * FROM cached_regions WHERE isCached = 1")
    fun getActiveCachedRegionsFlow(): Flow<List<CachedRegionEntity>>

    @Query("SELECT * FROM cached_regions WHERE regionId = :regionId LIMIT 1")
    suspend fun getCachedRegionOnce(regionId: String): CachedRegionEntity?

    @Query("SELECT isCached FROM cached_regions WHERE regionId = :regionId LIMIT 1")
    fun isRegionCachedFlow(regionId: String): Flow<Boolean?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(region: CachedRegionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(regions: List<CachedRegionEntity>)

    @Query("UPDATE cached_regions SET isCached = :isCached, cachedTimestamp = :timestamp WHERE regionId = :regionId")
    suspend fun setRegionCachedStatus(regionId: String, isCached: Boolean, timestamp: Long)

    @Query("DELETE FROM cached_regions WHERE regionId = :regionId")
    suspend fun deleteRegion(regionId: String)

    @Query("DELETE FROM cached_regions")
    suspend fun clearAll()
}
