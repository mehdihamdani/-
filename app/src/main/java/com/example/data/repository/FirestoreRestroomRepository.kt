package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.firebase.OperationType
import com.example.data.firebase.handleFirestoreError
import com.example.data.model.FirestoreFavorite
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
        return db.collection("users").document(userId).collection("favorites")
            .snapshots()
            .map { snapshot ->
                snapshot.documents.map { it.id }
            }
            .catch { e ->
                handleFirestoreError(e as? Exception ?: Exception(e), OperationType.LIST, "users/$userId/favorites")
                emit(emptyList())
            }
            .flowOn(Dispatchers.IO)
    }

    suspend fun saveUserProfile(userId: String, displayName: String, email: String) = withContext(Dispatchers.IO) {
        val verifiedUid = requireUserId(userId)
        val userRef = db.collection("users").document(verifiedUid)
        val data = mapOf(
            "userId" to verifiedUid,
            "displayName" to displayName.ifBlank { "مستخدم" },
            "email" to email.ifBlank { "user@dzrestrooms.app" },
            "createdAt" to FieldValue.serverTimestamp()
        )
        try {
            userRef.set(data).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, userRef.path)
            throw e
        }
    }

    suspend fun addRestroom(userId: String, restroom: FirestoreRestroom): String = withContext(Dispatchers.IO) {
        val verifiedUid = requireUserId(userId)
        val docId = if (restroom.id.isNotBlank()) restroom.id else UUID.randomUUID().toString()
        val docRef = db.collection("restrooms").document(docId)

        val data = mutableMapOf<String, Any>(
            "id" to docId,
            "name" to restroom.name,
            "latitude" to restroom.latitude,
            "longitude" to restroom.longitude,
            "province" to restroom.province,
            "createdBy" to verifiedUid,
            "createdAt" to FieldValue.serverTimestamp(),
            "rating" to restroom.rating,
            "reviewsCount" to restroom.reviewsCount,
            "cleanlinessRating" to restroom.cleanlinessRating,
            "isFree" to restroom.isFree,
            "priceDzd" to restroom.priceDzd,
            "hasWater" to restroom.hasWater,
            "hasSoap" to restroom.hasSoap,
            "hasPaper" to restroom.hasPaper,
            "hasAccessibleToilet" to restroom.hasAccessibleToilet,
            "hasSquatToilet" to restroom.hasSquatToilet,
            "hasSittingToilet" to restroom.hasSittingToilet,
            "type" to restroom.type
        )
        if (restroom.nameAr.isNotBlank()) data["nameAr"] = restroom.nameAr
        if (restroom.address.isNotBlank()) data["address"] = restroom.address

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
            "restroomId" to review.restroomId,
            "userId" to verifiedUid,
            "authorName" to review.authorName.ifBlank { "مواطن" },
            "rating" to review.rating.coerceIn(1.0, 5.0),
            "cleanlinessRating" to review.cleanlinessRating.coerceIn(1.0, 5.0),
            "hasWaterAvailable" to review.hasWaterAvailable,
            "hasSoapPaper" to review.hasSoapPaper,
            "createdAt" to FieldValue.serverTimestamp()
        )
        if (review.comment.isNotBlank()) data["comment"] = review.comment.trim()

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
}
