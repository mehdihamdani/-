package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class RestroomType(val arabicName: String, val frenchName: String) {
    MOSQUE("مسجد ووضوء", "Mosquée & Ablutions"),
    HAMMAM("حمام شعبي / دوش", "Hammam traditionnel"),
    NAFTAL_HIGHWAY("محطة نفطال / طريق سريع", "Station Naftal / Autoroute"),
    TRANSPORT_HUB("محطة نقل (سوجرال/ميترو/قطار)", "Gare / Station de transport"),
    MALL("مركز تجاري (مول)", "Centre commercial"),
    PUBLIC_MUNICIPAL("مرحاض عمومي بلدي", "Toilette publique municipale"),
    PRIVATE_COMMERCIAL("مرحاض خواص", "Toilettes privées")
}

@Entity(tableName = "restrooms")
data class RestroomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val nameFr: String = "",
    val wilaya: String,
    val commune: String,
    val type: RestroomType,
    val latitude: Double,
    val longitude: Double,
    val isFree: Boolean,
    val priceDzd: Int = 0,
    val rating: Float = 4.5f,
    val reviewsCount: Int = 12,
    val hasWater: Boolean = true,
    val hasSoapPaper: Boolean = true,
    val isAccessiblePmr: Boolean = false,
    val hasWomenSection: Boolean = true,
    val hasShower: Boolean = false,
    val hasWudu: Boolean = true,
    val hasChangingTable: Boolean = false,
    val cleanlinessRating: Float = 4.5f,
    val openingHours: String = "24/7",
    val address: String = "",
    val notes: String = "",
    val isFavorite: Boolean = false,
    val isUserAdded: Boolean = false,
    val waterCutReported: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val lastCachedTimestamp: Long = System.currentTimeMillis(),
    val isOfflineAvailable: Boolean = true
) {
    /**
     * Formats basic metadata for offline display when internet is unavailable.
     */
    val offlineMetadataSummary: String
        get() {
            val amenities = mutableListOf<String>()
            if (isAccessiblePmr) amenities.add("♿ مهيأ لذوي الهمم")
            if (hasChangingTable) amenities.add("👶 طاولة رضع")
            if (hasWater) amenities.add("💧 ماء متوفر") else amenities.add("⚠️ ماء منقطع")
            if (hasWomenSection) amenities.add("🧕 جناح نساء")
            if (isFree) amenities.add("مجاني") else amenities.add("$priceDzd دج")
            return amenities.joinToString(" • ")
        }

    val offlineCoordinatesString: String
        get() = String.format("%.4f° N, %.4f° E", latitude, longitude)
}

data class UserLocation(
    val latitude: Double = 36.7538, // Default Algiers Center
    val longitude: Double = 3.0588,
    val cityName: String = "الجزائر العاصمة (وسط)"
)
