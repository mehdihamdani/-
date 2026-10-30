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
        val runId = java.util.UUID.randomUUID().toString()
        val userEmail = "test_user_$runId@example.com"
        val uid = signInTestUser(userEmail)

        val repository = FirestoreRestroomRepository(firestore)
        val restroomId = "test_r1_$runId"

        val restroom = FirestoreRestroom(
            id = restroomId,
            name = "Alger Centre Restroom",
            nameAr = "دورة مياه الجزائر الوسطى",
            latitude = 36.77,
            longitude = 3.06,
            province = "الجزائر",
            createdBy = uid
        )

        val createdId = kotlinx.coroutines.withTimeout(5000L) {
            repository.addRestroom(uid, restroom)
        }
        assertEquals(restroomId, createdId)

        val restrooms = kotlinx.coroutines.withTimeout(5000L) {
            repository.observeRestrooms().first { list -> list.any { it.id == restroomId } }
        }
        assertTrue("Restrooms list should contain newly added restroom", restrooms.any { it.id == restroomId })
    }

    @Test
    fun unauthenticatedUser_failsToAddRestroom() = runBlocking {
        val runId = java.util.UUID.randomUUID().toString()
        val userEmail = "alice_$runId@example.com"
        val uid = signInTestUser(userEmail)

        val repository = FirestoreRestroomRepository(firestore)

        // Sign out to test unauthenticated rejection
        auth.signOut()

        val restroom = FirestoreRestroom(
            id = "test_unauth_$runId",
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
        val runId = java.util.UUID.randomUUID().toString()
        val userEmail = "reviewer_$runId@example.com"
        val uid = signInTestUser(userEmail)

        val repository = FirestoreRestroomRepository(firestore)
        val restroomId = "test_r_review_$runId"
        val revId = "rev_1_$runId"

        // First add a restroom
        val restroom = FirestoreRestroom(
            id = restroomId,
            name = "Oran Station Restroom",
            latitude = 35.69,
            longitude = -0.63,
            province = "وهران",
            createdBy = uid
        )
        kotlinx.coroutines.withTimeout(5000L) {
            repository.addRestroom(uid, restroom)
        }

        // Add review
        val review = FirestoreReview(
            id = revId,
            restroomId = restroomId,
            userId = uid,
            authorName = "كريم و.",
            rating = 4.8,
            cleanlinessRating = 4.5,
            comment = "مكان نظيف جداً ومريح"
        )
        val reviewId = kotlinx.coroutines.withTimeout(5000L) {
            repository.addReview(uid, review)
        }
        assertEquals(revId, reviewId)

        val reviews = kotlinx.coroutines.withTimeout(5000L) {
            repository.observeReviews(restroomId).first { list -> list.any { it.id == revId } }
        }
        assertTrue(reviews.any { it.id == revId && it.authorName == "كريم و." })
    }

    @Test
    fun userFavorites_isolatedPerUser() = runBlocking {
        val runId = java.util.UUID.randomUUID().toString()
        val favRestroomId = "restroom_$runId"
        val aliceUid = signInTestUser("alice_fav_$runId@example.com")
        val aliceRepo = FirestoreRestroomRepository(firestore)

        kotlinx.coroutines.withTimeout(5000L) {
            aliceRepo.toggleFavorite(aliceUid, favRestroomId, true)
        }
        val aliceFavs = kotlinx.coroutines.withTimeout(5000L) {
            aliceRepo.observeUserFavorites(aliceUid).first { it.contains(favRestroomId) }
        }
        assertTrue(aliceFavs.contains(favRestroomId))

        // Sign in as Bob
        val bobUid = signInTestUser("bob_fav_$runId@example.com")
        val bobRepo = FirestoreRestroomRepository(firestore)

        val bobFavs = kotlinx.coroutines.withTimeout(5000L) {
            bobRepo.observeUserFavorites(bobUid).first()
        }
        assertTrue("Bob should not see Alice's favorites", bobFavs.isEmpty())
    }

    @Test
    fun authenticatedUser_canCreateMosqueWithQuranSchoolAndFacebookUrl() = runBlocking {
        val runId = java.util.UUID.randomUUID().toString()
        val uid = signInTestUser("mosque_contributor_$runId@example.com")
        val repository = FirestoreRestroomRepository(firestore)
        val mosqueId = "mosque_test_$runId"

        val mosque = FirestoreRestroom(
            id = mosqueId,
            name = "Grande Mosquée d'Alger",
            nameAr = "جامع الجزائر الأعظم",
            latitude = 36.7345,
            longitude = 3.1418,
            province = "الجزائر العاصمة",
            type = "mosque",
            hasQuranSchool = true,
            hasFridayPrayer = true,
            hasLibrary = true,
            facebookUrl = "https://www.facebook.com/DjamaaElDjazairOfficial",
            mosqueServicesNotes = "مدرسة قرآنية لتحفيظ القرآن الكريم",
            createdBy = uid
        )

        val createdId = kotlinx.coroutines.withTimeout(5000L) {
            repository.addRestroom(uid, mosque)
        }
        assertEquals(mosqueId, createdId)

        val mosques = kotlinx.coroutines.withTimeout(5000L) {
            repository.observeRestrooms().first { list -> list.any { it.id == mosqueId } }
        }
        val fetched = mosques.find { it.id == mosqueId }
        assertNotNull(fetched)
        assertTrue(fetched!!.hasQuranSchool)
        assertEquals("https://www.facebook.com/DjamaaElDjazairOfficial", fetched.facebookUrl)
    }

    @Test
    fun authenticatedUser_canCreateHammamWithServicesAndSchedules() = runBlocking {
        val runId = java.util.UUID.randomUUID().toString()
        val uid = signInTestUser("hammam_contributor_$runId@example.com")
        val repository = FirestoreRestroomRepository(firestore)
        val hammamId = "hammam_test_$runId"

        val hammam = FirestoreRestroom(
            id = hammamId,
            name = "Station Thermale Hammam Debagh",
            nameAr = "المركب الحموي المعدني حمام دباغ",
            latitude = 36.4589,
            longitude = 7.2694,
            province = "قالمة",
            type = "hammam",
            isFree = false,
            priceDzd = 350.0,
            hasSteamSauna = true,
            hasKessaScrub = true,
            hasPrivateShowerRooms = true,
            isThermalSpring = true,
            hasTowelRental = true,
            hasTeaLounge = true,
            hammamPhone = "037 14 22 10",
            hammamMenHours = "الرجال: 06:00 - 21:00",
            hammamWomenHours = "النساء: 08:00 - 19:00",
            hammamServicesNotes = "مياه حموية علاجية طبيعية ساخنة وغرف استشفاء عائلية.",
            facebookUrl = "https://www.facebook.com/HammamDebaghGuelma",
            createdBy = uid
        )

        val createdId = kotlinx.coroutines.withTimeout(5000L) {
            repository.addRestroom(uid, hammam)
        }
        assertEquals(hammamId, createdId)

        val allFacilities = kotlinx.coroutines.withTimeout(5000L) {
            repository.observeRestrooms().first { list -> list.any { it.id == hammamId } }
        }
        val fetched = allFacilities.find { it.id == hammamId }
        assertNotNull(fetched)
        assertTrue(fetched!!.hasSteamSauna)
        assertTrue(fetched.hasKessaScrub)
        assertTrue(fetched.isThermalSpring)
        assertEquals(false, fetched.isFree)
        assertEquals("الرجال: 06:00 - 21:00", fetched.hammamMenHours)
        assertEquals("https://www.facebook.com/HammamDebaghGuelma", fetched.facebookUrl)
    }
}
