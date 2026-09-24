package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.model.CachedUserLocationEntity
import com.example.data.repository.RestroomRepository
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
class LocationRoomCachingTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: RestroomRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = RestroomRepository(db.restroomDao(), db.cachedLocationDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testSaveAndRetrieveCachedLocation() = runBlocking {
        // Save user location coordinates to Room
        repository.saveCachedLocation(
            lat = 36.7538,
            lng = 3.0588,
            cityName = "الجزائر العاصمة"
        )

        // Retrieve cached location
        val cached = repository.getCachedLocationOnce()
        assertNotNull(cached)
        assertEquals(36.7538, cached!!.latitude, 0.0001)
        assertEquals(3.0588, cached.longitude, 0.0001)
        assertEquals("الجزائر العاصمة", cached.cityName)
        assertTrue(cached.updatedAt > 0)
    }

    @Test
    fun testSeedRestroomDataWorksOffline() = runBlocking {
        repository.checkAndSeedInitialData()
        val count = db.restroomDao().countRestrooms()
        assertTrue("Restrooms should be seeded into Room database", count > 0)
    }
}
