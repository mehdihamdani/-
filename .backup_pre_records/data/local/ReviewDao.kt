package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    @Query("SELECT * FROM reviews WHERE restroomId = :restroomId ORDER BY timestamp DESC")
    fun getReviewsForRestroom(restroomId: Long): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews WHERE restroomId = :restroomId ORDER BY timestamp DESC")
    suspend fun getReviewsForRestroomOnce(restroomId: Long): List<ReviewEntity>

    @Query("SELECT COUNT(*) FROM reviews WHERE restroomId = :restroomId")
    suspend fun countReviewsForRestroom(restroomId: Long): Int

    @Query("SELECT COUNT(*) FROM reviews")
    suspend fun countAllReviews(): Int

    @Query("SELECT AVG(rating) FROM reviews WHERE restroomId = :restroomId")
    suspend fun getAverageRating(restroomId: Long): Float?

    @Query("SELECT AVG(cleanlinessRating) FROM reviews WHERE restroomId = :restroomId")
    suspend fun getAverageCleanlinessRating(restroomId: Long): Float?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reviews: List<ReviewEntity>)

    @Query("DELETE FROM reviews WHERE id = :reviewId")
    suspend fun deleteReview(reviewId: Long)
}
