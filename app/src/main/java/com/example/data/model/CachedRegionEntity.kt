package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity storing the cached status and metadata of an Algerian Region or Wilaya.
 */
@Entity(tableName = "cached_regions")
data class CachedRegionEntity(
    @PrimaryKey
    val regionId: String,
    val nameAr: String,
    val nameFr: String,
    val wilayasListString: String,
    val isCached: Boolean = true,
    val cachedTimestamp: Long = System.currentTimeMillis(),
    val restroomsCount: Int = 0,
    val estimatedSizeBytes: Long = 0L,
    val downloadProgress: Float = 1.0f
) {
    val sizeMb: Double
        get() = estimatedSizeBytes.toDouble() / (1024 * 1024)

    val sizeFormatted: String
        get() = String.format("%.1f MB", sizeMb)
}
