package com.example.data.model

/**
 * Represents the Algerian user profile, identity verification data,
 * and account linking status in the DZ Restrooms ("راحة") platform.
 */
data class UserProfileData(
    val userId: String = "user_default",
    val fullName: String = "مهدي حمداني",
    val fullNameFr: String = "HAMDANI MEHDI",
    val email: String = "mehdi.hamdani@example.dz",
    val phone: String = "0550 12 34 56",
    val wilaya: String = "16 - الجزائر العاصمة",
    val commune: String = "الجزائر الوسطى",
    val ninNumber: String = "199416010023456789", // 18-digit Algerian National Identification Number
    val dateOfBirth: String = "12/05/1994",
    val bloodGroup: String = "O+",
    val isIdCardVerified: Boolean = true,
    val isGoogleLinked: Boolean = true,
    val isFacebookLinked: Boolean = false,
    val isTikTokLinked: Boolean = false,
    val avatarUrl: String? = null,
    val reputationScore: Int = 185,
    val badgeTitle: String = "مساهم معتمد 🇩🇿",
    val contributionsCount: Int = 8,
    val reviewsCount: Int = 14,
    val waterReportsCount: Int = 3
)
