package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CachedUserLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedLocationDao {
    @Query("SELECT * FROM cached_location WHERE id = 1 LIMIT 1")
    fun getCachedLocationFlow(): Flow<CachedUserLocationEntity?>

    @Query("SELECT * FROM cached_location WHERE id = 1 LIMIT 1")
    suspend fun getCachedLocationOnce(): CachedUserLocationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCachedLocation(cachedLocation: CachedUserLocationEntity)
}
