package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_location")
data class CachedUserLocationEntity(
    @PrimaryKey
    val id: Int = 1,
    val latitude: Double,
    val longitude: Double,
    val cityName: String,
    val updatedAt: Long = System.currentTimeMillis()
)
