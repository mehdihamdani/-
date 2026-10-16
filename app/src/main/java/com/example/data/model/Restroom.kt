package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class RestroomType(
    val arabicName: String,
    val frenchName: String,
    val englishName: String = frenchName
) {
    MOSQUE("مسجد", "Mosquée", "Mosque"),
    MUSALLA("مصلى", "Salle de prière / Musalla", "Prayer Room / Musalla"),
    HAMMAM("حمام شعبي / دوش", "Hammam traditionnel", "Traditional Bath / Shower"),
    NAFTAL_HIGHWAY("محطة نفطال / طريق سريع", "Station Naftal / Autoroute", "Naftal Station / Highway"),
    TRANSPORT_HUB("محطة نقل (سوجرال/ميترو/قطار)", "Gare / Station de transport", "Transport Hub (Sogral/Metro/Train)"),
    MALL("مركز تجاري (مول)", "Centre commercial", "Shopping Mall"),
    PUBLIC_MUNICIPAL("دورة مياه عمومية بلدية", "Toilette publique municipale", "Municipal Public Toilet"),
    PRIVATE_COMMERCIAL("دورة مياه خواص", "Toilettes privées", "Private Commercial Restroom");

    fun getDisplayName(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> arabicName
        AppLanguage.FR -> frenchName
        AppLanguage.EN -> englishName
    }
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
    val donationCcp: String = "",
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

    fun getDisplayName(lang: AppLanguage): String {
        return if (lang != AppLanguage.AR && nameFr.isNotBlank()) nameFr else name
    }

    val offlineCoordinatesString: String
        get() = String.format("%.4f° N, %.4f° E", latitude, longitude)
}

data class UserLocation(
    val latitude: Double = 36.7538, // Default Algiers Center
    val longitude: Double = 3.0588,
    val cityName: String = "الجزائر العاصمة (وسط)"
)
