package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Metadata representing the state of offline Room storage for rest stops.
 * Tracks total cached items, last sync/caching timestamp, and offline readiness.
 */
@Entity(tableName = "offline_cache_metadata")
data class OfflineCacheMetadataEntity(
    @PrimaryKey
    val id: Int = 1,
    val totalRestStopsCached: Int = 0,
    val lastCachedTimestamp: Long = System.currentTimeMillis(),
    val cachedWilayasCount: Int = 0,
    val isOfflineReady: Boolean = true,
    val storageEngine: String = "Room SQLite",
    val cacheVersion: String = "v1.2",
    val statusMessage: String = "تم تخزين جميع المواقع والبيانات الوصفية محلياً للعمل بدون إنترنت"
)
