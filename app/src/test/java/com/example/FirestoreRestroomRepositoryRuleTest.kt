package com.example

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.model.FirestoreRestroom
import com.example.data.model.FirestoreReview
import com.example.data.repository.FirestoreRestroomRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class FirestoreRestroomRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun authenticatedUser_canCreateAndReadRestroom() = runBlocking {
        val userEmail = "test_user@example.com"
        val uid = signInTestUser(userEmail)

        val repository = FirestoreRestroomRepository(firestore)

        val restroom = FirestoreRestroom(
            id = "test_r1",
            name = "Alger Centre Restroom",
            nameAr = "مرحاض الجزائر الوسطى",
            latitude = 36.77,
            longitude = 3.06,
            province = "الجزائر",
            createdBy = uid
        )

        val createdId = repository.addRestroom(uid, restroom)
        assertEquals("test_r1", createdId)

        val restrooms = repository.observeRestrooms().first()
        assertTrue("Restrooms list should contain newly added restroom", restrooms.any { it.id == "test_r1" })
    }

    @Test
    fun unauthenticatedUser_failsToAddRestroom() = runBlocking {
        val userEmail = "alice@example.com"
        val uid = signInTestUser(userEmail)

        val repository = FirestoreRestroomRepository(firestore)

        // Sign out to test unauthenticated rejection
        auth.signOut()

        val restroom = FirestoreRestroom(
            id = "test_unauth",
            name = "Unauthorized Restroom",
            latitude = 36.77,
            longitude = 3.06,
            province = "الجزائر",
            createdBy = uid
        )

        try {
            repository.addRestroom(uid, restroom)
            fail("Expected exception when unauthenticated user attempts to add restroom")
        } catch (e: Exception) {
            assertTrue(e is IllegalStateException || e.message?.contains("not authenticated") == true)
        }
    }

    @Test
    fun authenticatedUser_canAddReviewAndRead() = runBlocking {
        val userEmail = "reviewer@example.com"
        val uid = signInTestUser(userEmail)

        val repository = FirestoreRestroomRepository(firestore)

        // First add a restroom
        val restroom = FirestoreRestroom(
            id = "test_r_review",
            name = "Oran Station Restroom",
            latitude = 35.69,
            longitude = -0.63,
            province = "وهران",
            createdBy = uid
        )
        repository.addRestroom(uid, restroom)

        // Add review
        val review = FirestoreReview(
            id = "rev_1",
            restroomId = "test_r_review",
            userId = uid,
            authorName = "كريم و.",
            rating = 4.8,
            cleanlinessRating = 4.5,
            comment = "مكان نظيف جداً ومريح"
        )
        val reviewId = repository.addReview(uid, review)
        assertEquals("rev_1", reviewId)

        val reviews = repository.observeReviews("test_r_review").first()
        assertTrue(reviews.any { it.id == "rev_1" && it.authorName == "كريم و." })
    }

    @Test
    fun userFavorites_isolatedPerUser() = runBlocking {
        val aliceUid = signInTestUser("alice_fav@example.com")
        val aliceRepo = FirestoreRestroomRepository(firestore)

        aliceRepo.toggleFavorite(aliceUid, "restroom_123", true)
        val aliceFavs = aliceRepo.observeUserFavorites(aliceUid).first()
        assertTrue(aliceFavs.contains("restroom_123"))

        // Sign in as Bob
        val bobUid = signInTestUser("bob_fav@example.com")
        val bobRepo = FirestoreRestroomRepository(firestore)

        val bobFavs = bobRepo.observeUserFavorites(bobUid).first()
        assertTrue("Bob should not see Alice's favorites", bobFavs.isEmpty())
    }
}
