package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.model.RestroomEntity
import com.example.data.model.RestroomType
import com.example.data.repository.RestroomRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ReviewSubmissionWithCameraTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: RestroomRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = RestroomRepository(
            dao = db.restroomDao(),
            cachedLocationDao = db.cachedLocationDao(),
            cacheMetadataDao = db.offlineCacheMetadataDao(),
            reviewDao = db.reviewDao()
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testSubmitReviewWithRatingAndCameraImage() = runBlocking {
        // 1. Insert a restroom
        val restroom = RestroomEntity(
            id = 50,
            name = "مرافق وضوء مسجد القدس",
            nameFr = "Mosquée El Qods - Ablutions",
            wilaya = "الشلف",
            commune = "الشلف",
            type = RestroomType.MOSQUE,
            latitude = 36.165,
            longitude = 1.334,
            isFree = true,
            rating = 4.0f,
            reviewsCount = 2,
            hasWater = true,
            hasSoapPaper = true
        )
        repository.addRestroom(restroom)

        // 2. Submit a review with camera image URI
        val testImagePath = "/data/user/0/com.aistudio.dzrestrooms.ktqxnm/files/review_photos/review_1234567.jpg"
        val reviewId = repository.submitReview(
            restroomId = 50,
            authorName = "مواطن كريم",
            rating = 5.0f,
            cleanlinessRating = 4.8f,
            comment = "مرفق نظيف جداً وتوفر مستمر للمياه الساخنة والباردة",
            hasWaterAvailable = true,
            hasSoapPaper = true,
            imageUri = testImagePath
        )

        assertTrue(reviewId > 0)

        // 3. Verify the review is saved with the image path
        val reviews = repository.getReviewsForRestroom(50).first()
        assertEquals(1, reviews.size)
        val savedReview = reviews.first()
        assertEquals("مواطن كريم", savedReview.authorName)
        assertEquals(5.0f, savedReview.rating, 0.01f)
        assertEquals(4.8f, savedReview.cleanlinessRating, 0.01f)
        assertEquals(testImagePath, savedReview.imageUri)
        assertTrue(savedReview.hasWaterAvailable)

        // 4. Verify the restroom's reviews count and average rating were updated
        val updatedRestroom = repository.getRestroomById(50)
        assertNotNull(updatedRestroom)
        assertEquals(1, updatedRestroom!!.reviewsCount)
        assertEquals(5.0f, updatedRestroom.rating, 0.01f)
        assertEquals(4.8f, updatedRestroom.cleanlinessRating, 0.01f)
    }
}
