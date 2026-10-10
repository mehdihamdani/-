package com.example.data.model

import androidx.compose.ui.graphics.Color

/**
 * 5 Distinct Themes for the DZ Restrooms ("راحة") application.
 */
enum class AppThemePalette(
    val id: String,
    val arabicName: String,
    val frenchName: String,
    val englishName: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val containerColor: Color
) {
    RAHA_EMERALD(
        id = "raha_emerald",
        arabicName = "زمرد راحة (الأصلي)",
        frenchName = "Émeraude Raha (Original)",
        englishName = "Raha Emerald (Original)",
        primaryColor = Color(0xFF00965E),
        secondaryColor = Color(0xFF084C8D),
        containerColor = Color(0xFFE6F7F0)
    ),
    ALGERIAN_SAHARA(
        id = "algerian_sahara",
        arabicName = "رمال الصحراء الذهبية",
        frenchName = "Dunes du Sahara",
        englishName = "Saharan Golden Dunes",
        primaryColor = Color(0xFFD97706),
        secondaryColor = Color(0xFF92400E),
        containerColor = Color(0xFFFEF3C7)
    ),
    MEDITERRANEAN_AZURE(
        id = "mediterranean_azure",
        arabicName = "أزرق البحر الأبيض المتوسط",
        frenchName = "Azur Méditerranéen",
        englishName = "Mediterranean Azure",
        primaryColor = Color(0xFF0284C7),
        secondaryColor = Color(0xFF0369A1),
        containerColor = Color(0xFFE0F2FE)
    ),
    ALGERIAN_RUBY(
        id = "algerian_ruby",
        arabicName = "الياقوت العنابي الملكي",
        frenchName = "Rubis Royal",
        englishName = "Royal Crimson Ruby",
        primaryColor = Color(0xFFE11D48),
        secondaryColor = Color(0xFF9F1239),
        containerColor = Color(0xFFFFE4E6)
    ),
    ATLAS_CEDAR(
        id = "atlas_cedar",
        arabicName = "أرز الأطلس العريق",
        frenchName = "Cèdre de l'Atlas",
        englishName = "Atlas Cedar Pine",
        primaryColor = Color(0xFF15803D),
        secondaryColor = Color(0xFF713F12),
        containerColor = Color(0xFFDCFCE7)
    )
}

/**
 * Dark Mode options.
 */
enum class DarkModeOption(
    val arabicName: String,
    val frenchName: String,
    val englishName: String
) {
    SYSTEM("تلقائي حسب النظام", "Système par défaut", "System Default"),
    LIGHT("الوضع النهاري (فاتح)", "Mode Clair", "Light Mode"),
    DARK("الوضع الليلي (داكن)", "Mode Sombre", "Dark Mode")
}

/**
 * 3 Supported Languages: Arabic, French, English.
 */
enum class AppLanguage(
    val code: String,
    val nativeName: String,
    val flag: String
) {
    AR("ar", "العربية", "🇩🇿"),
    FR("fr", "Français", "🇫🇷"),
    EN("en", "English", "🇬🇧")
}

/**
 * Holds global settings for the app.
 */
data class AppSettings(
    val themePalette: AppThemePalette = AppThemePalette.RAHA_EMERALD,
    val darkModeOption: DarkModeOption = DarkModeOption.SYSTEM,
    val language: AppLanguage = AppLanguage.AR
)
