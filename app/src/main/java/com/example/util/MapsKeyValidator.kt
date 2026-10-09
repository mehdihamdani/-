package com.example.util

import com.example.BuildConfig

/**
 * Validates Google Maps SDK API key configuration.
 * A valid Google Cloud API key starts with "AIza" and has length of at least 35 characters.
 */
object MapsKeyValidator {

    fun isKeyValid(): Boolean {
        val key = BuildConfig.MAPS_API_KEY.trim()
        return key.isNotBlank() &&
                key != "DEFAULT_MAPS_API_KEY" &&
                key.startsWith("AIza") &&
                key.length >= 35
    }

    fun getErrorMessage(): String {
        val key = BuildConfig.MAPS_API_KEY.trim()
        return when {
            key.isBlank() || key == "DEFAULT_MAPS_API_KEY" -> {
                "لم يتم تكوين مفتاح Google Maps API في لوحة Secrets."
            }
            !key.startsWith("AIza") -> {
                "مفتاح Google Maps الحالي ('$key') غير صالح. يجب أن يبدأ المفتاح بـ AIzaSy ويتكون من 39 حرفاً من Google Cloud Console."
            }
            key.length < 35 -> {
                "مفتاح Google Maps غير مكتمل (أقل من 35 حرفاً)."
            }
            else -> {
                "مفتاح Google Maps يحتاج للتحقق في Google Developer Console."
            }
        }
    }
}
