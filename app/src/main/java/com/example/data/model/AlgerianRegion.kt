package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a major geographical region of Algeria for offline caching of map
 * data, restrooms, mosques, highway service areas, and navigation points.
 */
data class AlgerianRegion(
    val id: String,
    val nameAr: String,
    val nameFr: String,
    val nameEn: String,
    val wilayasCodes: List<Int>,
    val wilayasAr: List<String>,
    val summaryAr: String,
    val summaryFr: String,
    val summaryEn: String,
    val estimatedSizeMb: Double,
    val facilitiesCount: Int
) {
    fun getName(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> nameAr
        AppLanguage.FR -> nameFr
        AppLanguage.EN -> nameEn
    }

    fun getSummary(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> summaryAr
        AppLanguage.FR -> summaryFr
        AppLanguage.EN -> summaryEn
    }

    val sizeFormatted: String
        get() = String.format("%.1f MB", estimatedSizeMb)
}

/**
 * Registry of the 5 macro regions of Algeria covering all 69 wilayas.
 */
object AlgerianRegions {

    val REGION_CENTRE = AlgerianRegion(
        id = "reg_centre",
        nameAr = "الوسط والعاصمة (Centre & Algérois)",
        nameFr = "Région Centre & Algérois",
        nameEn = "Central & Algiers Region",
        wilayasCodes = listOf(16, 9, 35, 42, 26, 15, 10, 44, 2, 67),
        wilayasAr = listOf(
            "الجزائر العاصمة", "البليدة", "بومرداس", "تيبازة",
            "المدية", "تيزي وزو", "البويرة", "عين الدفلى", "الشلف", "قصر البخاري"
        ),
        summaryAr = "العاصمة وضواحيها، الطريق السيار شرق-غرب الوسطي، متيجة والساحل الأوسط",
        summaryFr = "Alger métropole, Blida, Mitidja, littoral central et autoroute Est-Ouest",
        summaryEn = "Algiers metropolis, Blida, coastal corridor, and central highway rest stops",
        estimatedSizeMb = 3.2,
        facilitiesCount = 42
    )

    val REGION_OUEST = AlgerianRegion(
        id = "reg_ouest",
        nameAr = "الغرب والوهراني (Ouest & Oranie)",
        nameFr = "Région Ouest & Oranie",
        nameEn = "Western & Oran Region",
        wilayasCodes = listOf(31, 13, 27, 22, 29, 48, 20, 46, 63),
        wilayasAr = listOf(
            "وهران", "تلمسان", "مستغانم", "سيدي بلعباس",
            "معسكر", "غليزان", "سعيدة", "عين تموشنت", "العريشة"
        ),
        summaryAr = "وهران الكبرى، تلمسان التاريخية، موانئ مستغانم وباحات طريق الغرب السريع",
        summaryFr = "Oran, Tlemcen, Mostaganem, Sidi Bel Abbès et corridor autoroutier ouest",
        summaryEn = "Oran, Tlemcen, Mostaganem, Sidi Bel Abbès and western highway services",
        estimatedSizeMb = 2.8,
        facilitiesCount = 36
    )

    val REGION_EST = AlgerianRegion(
        id = "reg_est",
        nameAr = "الشرق والقسنطيني (Est & Constantinois)",
        nameFr = "Région Est & Constantinois",
        nameEn = "Eastern & Constantine Region",
        wilayasCodes = listOf(25, 19, 23, 5, 21, 6, 18, 24, 41, 12, 43, 4, 34, 40, 36, 60),
        wilayasAr = listOf(
            "قسنطينة", "سطيف", "عنابة", "باتنة", "سكيكدة", "بجاية",
            "جيجل", "قالمة", "سوق أهراس", "تبسة", "ميلة", "أم البواقي",
            "برج بوعريريج", "خنشلة", "الطارف", "بريكة"
        ),
        summaryAr = "قسنطينة الجسور، سطيف العالي، عنابة الساحلية، وباحات جبال البابور والأوراس",
        summaryFr = "Constantine, Sétif, Annaba, Batna, Kabylie maritime et massif des Aurès",
        summaryEn = "Constantine, Sétif, Annaba, Batna, coastal cities and Aurès mountain areas",
        estimatedSizeMb = 3.6,
        facilitiesCount = 48
    )

    val REGION_HAUTS_PLATEAUX = AlgerianRegion(
        id = "reg_hauts_plateaux",
        nameAr = "الهضاب العليا والسهوب (Hauts-Plateaux & Steppes)",
        nameFr = "Hauts-Plateaux & Steppes",
        nameEn = "High Plateaus & Steppes",
        wilayasCodes = listOf(17, 28, 14, 3, 7, 32, 45, 38, 51, 57, 59, 61, 62, 64, 65, 66, 68, 69),
        wilayasAr = listOf(
            "الجلفة", "المسيلة", "تيارت", "الأغواط", "بسكرة", "البيض",
            "النعامة", "تسمسيلت", "أولاد جلال", "المغير", "أفلو", "القنطرة",
            "بئر العاتر", "قصر الشلالة", "عين وسارة", "مسعد", "بوسعادة", "الأبيض سيدي الشيخ"
        ),
        summaryAr = "محاور السفر الوطنية الرابطة بين الشمال والجنوب، الجلفة، بوسعادة، وبوابة الصحراء بسكرة",
        summaryFr = "Djelfa, M'Sila, Bou Saâda, Tiaret, Biskra et axes de transit Nord-Sud",
        summaryEn = "Transit crossroads between North and South, Djelfa, Bou Saâda, and Biskra",
        estimatedSizeMb = 2.4,
        facilitiesCount = 30
    )

    val REGION_SUD = AlgerianRegion(
        id = "reg_sud",
        nameAr = "الجنوب والصحراء الكبرى (Sud & Grand Sahara)",
        nameFr = "Sud & Grand Sahara",
        nameEn = "Southern & Grand Sahara",
        wilayasCodes = listOf(47, 30, 1, 8, 39, 49, 50, 52, 53, 54, 55, 56, 58, 11, 33, 37),
        wilayasAr = listOf(
            "غرداية", "ورقلة", "أدرار", "بشار", "الوادي", "تيميمون",
            "برج باجي مختار", "بني عباس", "إن صالح", "إن قزام", "تقرت",
            "جانت", "المنيعة", "تمنراست", "إليزي", "تندوف"
        ),
        summaryAr = "وادي ميزاب، واحات النخيل، طاسيلي والهقار، ومحطات طرق قوافل السفر الصحراوي",
        summaryFr = "Ghardaïa, Ouargla, Adrar, oasis du Souf, Tassili, Hoggar et axes sahariens",
        summaryEn = "Ghardaïa, Ouargla, Adrar, desert oasis routes, Tassili and Hoggar paths",
        estimatedSizeMb = 2.9,
        facilitiesCount = 32
    )

    val allRegions: List<AlgerianRegion> = listOf(
        REGION_CENTRE,
        REGION_OUEST,
        REGION_EST,
        REGION_HAUTS_PLATEAUX,
        REGION_SUD
    )

    fun getRegionById(id: String): AlgerianRegion? {
        return allRegions.find { it.id.equals(id, ignoreCase = true) }
    }

    fun findRegionForWilaya(wilayaName: String): AlgerianRegion? {
        val trimmed = wilayaName.trim().removePrefix("ولاية ").trim()
        return allRegions.find { region ->
            region.wilayasAr.any { it.equals(trimmed, ignoreCase = true) || it.equals(wilayaName, ignoreCase = true) }
        }
    }
}
