package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Represents a community review & rating for a registered restroom.
 * Includes user comments, overall rating, and specific cleanliness evaluation.
 */
@Entity(
    tableName = "reviews",
    indices = [Index(value = ["restroomId"])]
)
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val restroomId: Long,
    val authorName: String,
    val rating: Float = 4.5f, // Overall rating (1.0 to 5.0)
    val cleanlinessRating: Float = 4.5f, // Cleanliness specific rating (1.0 to 5.0)
    val comment: String,
    val hasWaterAvailable: Boolean = true,
    val hasSoapPaper: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Formats timestamp into readable Arabic relative date or formatted string.
     */
    val formattedTime: String
        get() {
            val now = System.currentTimeMillis()
            val diff = now - timestamp
            val minutes = diff / (1000 * 60)
            val hours = diff / (1000 * 60 * 60)
            val days = diff / (1000 * 60 * 60 * 24)

            return when {
                minutes < 5 -> "الآن"
                minutes < 60 -> "منذ $minutes دقيقة"
                hours < 24 -> "منذ $hours ساعة"
                days == 1L -> "أمس"
                days in 2..10 -> "منذ $days أيام"
                days in 11..30 -> "منذ $days يوماً"
                else -> {
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("ar", "DZ"))
                    sdf.format(Date(timestamp))
                }
            }
        }
}
