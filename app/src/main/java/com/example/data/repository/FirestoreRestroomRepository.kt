package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.firebase.OperationType
import com.example.data.firebase.handleFirestoreError
import com.example.data.model.FirestoreFavorite
import com.example.data.model.FirestoreMosqueDonationAccount
import com.example.data.model.FirestoreRestroom
import com.example.data.model.FirestoreReview
import com.example.data.model.FirestoreUserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class FirestoreRestroomRepository(
    private val db: FirebaseFirestore
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private fun requireUserId(userId: String): String {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        check(currentUid != null && currentUid == userId) {
            "User is not authenticated or UID mismatch: current=$currentUid, target=$userId"
        }
        return currentUid
    }

    fun observeRestrooms(): Flow<List<FirestoreRestroom>> {
        return db.collection("restrooms")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(
                    FirestoreRestroom::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            }
            .catch { e ->
                handleFirestoreError(e as? Exception ?: Exception(e), OperationType.LIST, "restrooms")
                emit(emptyList())
            }
            .flowOn(Dispatchers.IO)
    }

    fun observeReviews(restroomId: String): Flow<List<FirestoreReview>> {
        return db.collection("reviews")
            .whereEqualTo("restroomId", restroomId)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(
                    FirestoreReview::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            }
            .catch { e ->
                handleFirestoreError(e as? Exception ?: Exception(e), OperationType.LIST, "reviews")
                emit(emptyList())
            }
            .flowOn(Dispatchers.IO)
    }

    fun observeUserFavorites(userId: String): Flow<List<String>> {
        val verifiedUid = requireUserId(userId)
        return db.collection("users")
            .document(verifiedUid)
            .collection("favorites")
            .snapshots()
            .map { snapshot ->
                snapshot.documents.map { it.id }
            }
            .catch { e ->
                handleFirestoreError(e as? Exception ?: Exception(e), OperationType.LIST, "users/$verifiedUid/favorites")
                emit(emptyList())
            }
            .flowOn(Dispatchers.IO)
    }

    suspend fun addRestroom(userId: String, restroom: FirestoreRestroom): String = withContext(Dispatchers.IO) {
        val verifiedUid = requireUserId(userId)
        val docId = if (restroom.id.isNotBlank()) restroom.id else UUID.randomUUID().toString()
        val docRef = db.collection("restrooms").document(docId)

        val data = mutableMapOf<String, Any>(
            "id" to docId,
            "name" to restroom.name.trim().take(150),
            "latitude" to restroom.latitude,
            "longitude" to restroom.longitude,
            "province" to restroom.province.trim().take(100),
            "createdBy" to verifiedUid,
            "createdAt" to FieldValue.serverTimestamp(),
            "rating" to restroom.rating.coerceIn(0.0, 5.0),
            "reviewsCount" to restroom.reviewsCount.coerceAtLeast(0),
            "cleanlinessRating" to restroom.cleanlinessRating.coerceIn(0.0, 5.0),
            "isFree" to restroom.isFree,
            "priceDzd" to restroom.priceDzd,
            "hasWater" to restroom.hasWater,
            "hasSoap" to restroom.hasSoap,
            "hasPaper" to restroom.hasPaper,
            "hasAccessibleToilet" to restroom.hasAccessibleToilet,
            "hasSquatToilet" to restroom.hasSquatToilet,
            "hasSittingToilet" to restroom.hasSittingToilet,
            "type" to restroom.type.trim().take(50)
        )
        if (restroom.nameAr.isNotBlank()) data["nameAr"] = restroom.nameAr.trim().take(150)
        if (restroom.address.isNotBlank()) data["address"] = restroom.address.trim().take(300)
        if (restroom.donationCcp.isNotBlank()) data["donationCcp"] = restroom.donationCcp.trim().take(100)

        try {
            docRef.set(data).await()
            docId
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    suspend fun addReview(userId: String, review: FirestoreReview): String = withContext(Dispatchers.IO) {
        val verifiedUid = requireUserId(userId)
        val reviewId = if (review.id.isNotBlank()) review.id else UUID.randomUUID().toString()
        val reviewRef = db.collection("reviews").document(reviewId)

        val data = mutableMapOf<String, Any>(
            "id" to reviewId,
            "restroomId" to review.restroomId.trim().take(128),
            "userId" to verifiedUid,
            "authorName" to review.authorName.trim().take(100).ifBlank { "مواطن" },
            "rating" to review.rating.coerceIn(1.0, 5.0),
            "cleanlinessRating" to review.cleanlinessRating.coerceIn(1.0, 5.0),
            "hasWaterAvailable" to review.hasWaterAvailable,
            "hasSoapPaper" to review.hasSoapPaper,
            "createdAt" to FieldValue.serverTimestamp()
        )
        if (review.comment.isNotBlank()) data["comment"] = review.comment.trim().take(1000)
        if (!review.imageUrl.isNullOrBlank()) data["imageUrl"] = review.imageUrl.trim().take(2000)

        try {
            reviewRef.set(data).await()

            // Update restroom rating metadata
            val restroomRef = db.collection("restrooms").document(review.restroomId)
            restroomRef.update(
                mapOf(
                    "rating" to review.rating,
                    "cleanlinessRating" to review.cleanlinessRating,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()

            reviewId
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, reviewRef.path)
            throw e
        }
    }

    suspend fun toggleFavorite(userId: String, restroomId: String, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        val verifiedUid = requireUserId(userId)
        val favRef = db.collection("users").document(verifiedUid).collection("favorites").document(restroomId)
        try {
            if (isFavorite) {
                favRef.set(
                    mapOf(
                        "restroomId" to restroomId,
                        "addedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            } else {
                favRef.delete().await()
            }
        } catch (e: Exception) {
            handleFirestoreError(e, if (isFavorite) OperationType.CREATE else OperationType.DELETE, favRef.path)
            throw e
        }
    }

    // =========================================================================
    // MOSQUE DONATION ACCOUNTS (ADMIN-ONLY REVIEW SECTION)
    // =========================================================================

    /**
     * Observes pending or recorded mosque donation accounts.
     * Note: Firestore security rules enforce that only administrators can read.
     */
    fun observeMosqueDonationAccounts(): Flow<List<FirestoreMosqueDonationAccount>> {
        return db.collection("mosque_donation_accounts")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(
                    FirestoreMosqueDonationAccount::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            }
            .catch { e ->
                handleFirestoreError(e as? Exception ?: Exception(e), OperationType.LIST, "mosque_donation_accounts")
                emit(emptyList())
            }
            .flowOn(Dispatchers.IO)
    }

    /**
     * Seeds initial unverified mosque donation account records if not already present.
     * Checks before writing to prevent duplicate records or overwriting existing edits.
     * Preserves CCP, key, and RIP strictly as strings with leading zeros.
     */
    suspend fun seedInitialMosqueAccountsIfMissing(records: List<FirestoreMosqueDonationAccount>) = withContext(Dispatchers.IO) {
        for (rec in records) {
            val docRef = db.collection("mosque_donation_accounts").document(rec.id)
            try {
                val existing = docRef.get().await()
                if (!existing.exists()) {
                    val map = mutableMapOf<String, Any?>(
                        "id" to rec.id,
                        "mosque_name" to rec.mosque_name,
                        "location_description" to rec.location_description,
                        "ccp" to rec.ccp,
                        "account_key" to rec.account_key,
                        "rip" to rec.rip,
                        "source" to rec.source,
                        "account_holder_name" to rec.account_holder_name,
                        "source_checked_at" to rec.source_checked_at,
                        "verification_status" to rec.verification_status,
                        "donations_enabled" to rec.donations_enabled,
                        "verified_at" to rec.verified_at,
                        "committee_consent_confirmed" to rec.committee_consent_confirmed,
                        "document_name" to rec.document_name,
                        "document_attached_at" to rec.document_attached_at,
                        "admin_notes" to rec.admin_notes,
                        "created_at" to FieldValue.serverTimestamp(),
                        "updated_at" to FieldValue.serverTimestamp()
                    )
                    docRef.set(map).await()
                }
            } catch (e: Exception) {
                // If not admin or other error, handled via security rules and logged
                handleFirestoreError(e, OperationType.CREATE, docRef.path)
            }
        }
    }

    /**
     * Updates an account's verification and audit fields by an administrator.
     * Uploading a document or recording the legal beneficiary name does NOT enable donations.
     */
    suspend fun updateMosqueAccountAudit(
        accountId: String,
        accountHolderName: String?,
        committeeConsentConfirmed: Boolean,
        documentName: String?,
        adminNotes: String
    ) = withContext(Dispatchers.IO) {
        val docRef = db.collection("mosque_donation_accounts").document(accountId)
        val updates = mutableMapOf<String, Any?>(
            "account_holder_name" to accountHolderName,
            "committee_consent_confirmed" to committeeConsentConfirmed,
            "admin_notes" to adminNotes,
            "updated_at" to FieldValue.serverTimestamp()
        )
        if (!documentName.isNullOrBlank()) {
            updates["document_name"] = documentName
            updates["document_attached_at"] = FieldValue.serverTimestamp()
        }
        try {
            docRef.update(updates).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }
}
