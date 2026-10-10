package com.example.util

import com.example.data.model.AppLanguage

/**
 * Localization dictionary supporting Arabic, French, and English.
 */
object AppStrings {

    fun navMap(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الخريطة"
        AppLanguage.FR -> "Carte"
        AppLanguage.EN -> "Map"
    }

    fun navRestrooms(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "المراحيض"
        AppLanguage.FR -> "Toilettes"
        AppLanguage.EN -> "Restrooms"
    }

    fun navFavorites(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "المفضلة"
        AppLanguage.FR -> "Favoris"
        AppLanguage.EN -> "Favorites"
    }

    fun navDonations(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "التبرعات"
        AppLanguage.FR -> "Dons"
        AppLanguage.EN -> "Donations"
    }

    fun navSettings(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الإعدادات"
        AppLanguage.FR -> "Paramètres"
        AppLanguage.EN -> "Settings"
    }

    fun navProfile(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الملف الشخصي"
        AppLanguage.FR -> "Profil"
        AppLanguage.EN -> "Profile"
    }

    fun searchPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "ابحث بالولاية (مثلاً: وهران، سطيف...) أو الحي"
        AppLanguage.FR -> "Rechercher par wilaya (ex: Oran, Sétif) ou quartier"
        AppLanguage.EN -> "Search by province (e.g. Oran, Setif) or district"
    }

    fun donationTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "التبرعات ودعم المبادرة"
        AppLanguage.FR -> "Dons & Soutien"
        AppLanguage.EN -> "Donations & Support"
    }

    fun tabAppDonation(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "دعم تطبيق راحة 💙"
        AppLanguage.FR -> "Soutenir l'Appli 💙"
        AppLanguage.EN -> "Support App 💙"
    }

    fun tabMosqueDonation(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "صيانة دورات المساجد 🕌"
        AppLanguage.FR -> "Entretien Mosquées 🕌"
        AppLanguage.EN -> "Mosque Facilities 🕌"
    }

    fun settingsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إعدادات التطبيق"
        AppLanguage.FR -> "Paramètres de l'Application"
        AppLanguage.EN -> "App Settings"
    }

    fun themesSection(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "ألوان وثيمات التطبيق (5 ثيمات)"
        AppLanguage.FR -> "Thèmes & Couleurs (5 thèmes)"
        AppLanguage.EN -> "Color Themes (5 themes)"
    }

    fun darkModeSection(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مظهر العرض والوضع الليلي"
        AppLanguage.FR -> "Affichage & Mode Sombre"
        AppLanguage.EN -> "Display & Dark Mode"
    }

    fun languageSection(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "لغة التطبيق (3 لغات)"
        AppLanguage.FR -> "Langue de l'Application (3 langues)"
        AppLanguage.EN -> "App Language (3 languages)"
    }
}
