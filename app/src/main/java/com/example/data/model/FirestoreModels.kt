package com.example.data.model

import com.google.firebase.Timestamp

data class FirestoreRestroom(
    val id: String = "",
    val name: String = "",
    val nameAr: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val province: String = "",
    val address: String = "",
    val type: String = "public",
    val isFree: Boolean = true,
    val priceDzd: Double = 0.0,
    val hasWater: Boolean = true,
    val hasSoap: Boolean = false,
    val hasPaper: Boolean = false,
    val hasAccessibleToilet: Boolean = false,
    val hasSquatToilet: Boolean = false,
    val hasSittingToilet: Boolean = false,
    val rating: Double = 5.0,
    val reviewsCount: Long = 0L,
    val cleanlinessRating: Double = 5.0,
    val donationCcp: String = "",
    val createdBy: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)

data class FirestoreReview(
    val id: String = "",
    val restroomId: String = "",
    val userId: String = "",
    val authorName: String = "",
    val rating: Double = 5.0,
    val cleanlinessRating: Double = 5.0,
    val comment: String = "",
    val hasWaterAvailable: Boolean = true,
    val hasSoapPaper: Boolean = false,
    val createdAt: Timestamp? = null
)

data class FirestoreUserProfile(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val isAdmin: Boolean = false,
    val createdAt: Timestamp? = null
)

data class FirestoreFavorite(
    val restroomId: String = "",
    val addedAt: Timestamp? = null
)

/**
 * Mosque donation account records under review by administrators.
 * NOTE: Stored as strings to strictly preserve leading zeros.
 * Not linked to map restrooms or payment destinations until verified.
 */
data class FirestoreMosqueDonationAccount(
    val id: String = "",
    val mosque_name: String = "",
    val location_description: String = "",
    val ccp: String = "",
    val account_key: String = "",
    val rip: String? = null,
    val source: String = "",
    val account_holder_name: String? = null,
    val source_checked_at: String = "2026-10-13",
    val verification_status: String = "pending",
    val donations_enabled: Boolean = false,
    val verified_at: Timestamp? = null,
    val committee_consent_confirmed: Boolean = false,
    val document_name: String? = null,
    val document_attached_at: Timestamp? = null,
    val admin_notes: String = "",
    val created_at: Timestamp? = null,
    val updated_at: Timestamp? = null
)
