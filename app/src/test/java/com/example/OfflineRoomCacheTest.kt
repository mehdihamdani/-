package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.model.CachedUserLocationEntity
import com.example.data.model.OfflineCacheMetadataEntity
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
class OfflineRoomCacheTest {

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
            cacheMetadataDao = db.offlineCacheMetadataDao()
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testCacheRestStopsAndLocationsInRoom() = runBlocking {
        val sampleRestStops = listOf(
            RestroomEntity(
                id = 101,
                name = "محطة استراحة بئر التوتة - الطريق السيار",
                nameFr = "Aire de repos Birtouta - Autoroute Est-Ouest",
                wilaya = "الجزائر العاصمة",
                commune = "بئر توتة",
                type = RestroomType.NAFTAL_HIGHWAY,
                latitude = 36.6432,
                longitude = 3.0041,
                isFree = true,
                priceDzd = 0,
                rating = 4.7f,
                cleanlinessRating = 4.8f,
                hasWater = true,
                hasSoapPaper = true,
                isAccessiblePmr = true,
                hasChangingTable = true,
                openingHours = "24/7",
                address = "الطريق السيار شرق-غرب كلم 24",
                isOfflineAvailable = true
            ),
            RestroomEntity(
                id = 102,
                name = "مرافق مسجد الأمير عبد القادر",
                nameFr = "Mosquée Emir Abdelkader",
                wilaya = "قسنطينة",
                commune = "قسنطينة",
                type = RestroomType.MOSQUE,
                latitude = 36.3578,
                longitude = 6.6114,
                isFree = true,
                priceDzd = 0,
                rating = 4.9f,
                cleanlinessRating = 4.9f,
                hasWater = true,
                hasSoapPaper = true,
                isAccessiblePmr = true,
                hasChangingTable = false,
                openingHours = "05:00 - 23:00",
                address = "شارع رحماني عاشور، قسنطينة",
                isOfflineAvailable = true
            )
        )

        // Cache items via repository
        repository.cacheRestStops(sampleRestStops)

        // Retrieve from Room database
        val cachedItems = db.restroomDao().getAllRestrooms().first()
        assertEquals(2, cachedItems.size)

        // Verify accurate coordinates (latitude, longitude)
        val birtouta = cachedItems.first { it.id == 101L }
        assertEquals(36.6432, birtouta.latitude, 0.0001)
        assertEquals(3.0041, birtouta.longitude, 0.0001)
        assertEquals("الجزائر العاصمة", birtouta.wilaya)
        assertTrue(birtouta.isAccessiblePmr)
        assertTrue(birtouta.hasChangingTable)
        assertTrue(birtouta.isOfflineAvailable)
        assertTrue(birtouta.lastCachedTimestamp > 0)
        assertTrue(birtouta.offlineCoordinatesString.contains("36.6432"))
    }

    @Test
    fun testOfflineBasicMetadataAvailable() = runBlocking {
        val testItem = RestroomEntity(
            id = 201,
            name = "مرحاض عمومي وهران الوسط",
            wilaya = "وهران",
            commune = "وهران",
            type = RestroomType.PUBLIC_MUNICIPAL,
            latitude = 35.6987,
            longitude = -0.6349,
            isFree = false,
            priceDzd = 20,
            isAccessiblePmr = true,
            hasWater = true,
            hasChangingTable = true,
            openingHours = "06:00 - 20:00"
        )
        db.restroomDao().insertRestroom(testItem)

        val retrieved = repository.getRestroomById(201)
        assertNotNull(retrieved)
        assertEquals("وهران", retrieved!!.wilaya)
        assertEquals(20, retrieved.priceDzd)
        assertEquals("06:00 - 20:00", retrieved.openingHours)

        val summary = retrieved.offlineMetadataSummary
        assertTrue(summary.contains("ذوي الهمم"))
        assertTrue(summary.contains("طاولة رضع"))
        assertTrue(summary.contains("ماء متوفر"))
        assertTrue(summary.contains("20 دج"))
    }

    @Test
    fun testOfflineCacheMetadataLifecycle() = runBlocking {
        val metadataDao = db.offlineCacheMetadataDao()

        val initialMeta = OfflineCacheMetadataEntity(
            id = 1,
            totalRestStopsCached = 45,
            lastCachedTimestamp = System.currentTimeMillis(),
            cachedWilayasCount = 18,
            isOfflineReady = true,
            statusMessage = "مخزن محلياً بالكامل"
        )
        metadataDao.saveMetadata(initialMeta)

        val retrieved = metadataDao.getMetadataOnce()
        assertNotNull(retrieved)
        assertEquals(45, retrieved!!.totalRestStopsCached)
        assertEquals(18, retrieved.cachedWilayasCount)
        assertTrue(retrieved.isOfflineReady)
    }

    @Test
    fun testRefreshOfflineCacheUpdatesTimestamps() = runBlocking {
        val items = listOf(
            RestroomEntity(
                id = 301,
                name = "مرفق سطيف",
                wilaya = "سطيف",
                commune = "العلمة",
                type = RestroomType.HAMMAM,
                latitude = 36.155,
                longitude = 5.688,
                isFree = false,
                priceDzd = 150,
                lastCachedTimestamp = 1000L
            )
        )
        db.restroomDao().insertAll(items)

        // Trigger refresh
        repository.refreshOfflineCache()

        val updated = repository.getRestroomById(301)
        assertNotNull(updated)
        assertTrue(updated!!.lastCachedTimestamp > 1000L)

        val meta = repository.getOfflineMetadataOnce()
        assertNotNull(meta)
        assertEquals(1, meta!!.totalRestStopsCached)
        assertTrue(meta.lastCachedTimestamp > 1000L)
    }

    @Test
    fun testCachedUserLocationWorksOffline() = runBlocking {
        repository.saveCachedLocation(
            lat = 36.7538,
            lng = 3.0588,
            cityName = "الجزائر العاصمة"
        )

        val cachedLoc = repository.getCachedLocationOnce()
        assertNotNull(cachedLoc)
        assertEquals(36.7538, cachedLoc!!.latitude, 0.0001)
        assertEquals(3.0588, cachedLoc.longitude, 0.0001)
        assertEquals("الجزائر العاصمة", cachedLoc.cityName)
    }
}
