package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.model.RestroomEntity
import com.example.data.model.RestroomType
import com.example.data.repository.RestroomRepository
import com.example.ui.viewmodel.FilterState
import com.example.ui.viewmodel.RestroomViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class MapFilteringCriteriaTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: RestroomRepository
    private lateinit var viewModel: RestroomViewModel
    private lateinit var testItems: List<RestroomEntity>

    @Before
    fun setUp() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = RestroomRepository(db.restroomDao(), db.cachedLocationDao())

        // Insert controlled sample items with specific criteria
        testItems = listOf(
            RestroomEntity(
                id = 1,
                name = "مرفق شامل مهيأ",
                wilaya = "الجزائر العاصمة",
                commune = "الوسط",
                type = RestroomType.MALL,
                latitude = 36.75,
                longitude = 3.05,
                isFree = true,
                rating = 4.8f,
                cleanlinessRating = 4.8f,
                hasWater = true,
                hasSoapPaper = true,
                isAccessiblePmr = true,
                hasChangingTable = true
            ),
            RestroomEntity(
                id = 2,
                name = "مرفق نظيف بدون طاولة رضع",
                wilaya = "الجزائر العاصمة",
                commune = "الوسط",
                type = RestroomType.MOSQUE,
                latitude = 36.76,
                longitude = 3.06,
                isFree = true,
                rating = 4.6f,
                cleanlinessRating = 4.6f,
                hasWater = true,
                hasSoapPaper = true,
                isAccessiblePmr = false,
                hasChangingTable = false
            ),
            RestroomEntity(
                id = 3,
                name = "مرفق قديم عادي",
                wilaya = "الجزائر العاصمة",
                commune = "الوسط",
                type = RestroomType.PUBLIC_MUNICIPAL,
                latitude = 36.77,
                longitude = 3.07,
                isFree = false,
                priceDzd = 20,
                rating = 3.2f,
                cleanlinessRating = 3.2f,
                hasWater = true,
                hasSoapPaper = false,
                isAccessiblePmr = false,
                hasChangingTable = false
            )
        )
        db.restroomDao().insertAll(testItems)
        viewModel = RestroomViewModel(repository)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testActiveFilterCount() {
        var state = FilterState()
        assertEquals(0, state.activeFilterCount)

        state = state.copy(accessiblePmrOnly = true)
        assertEquals(1, state.activeFilterCount)

        state = state.copy(highCleanlinessOnly = true, changingTableOnly = true)
        assertEquals(3, state.activeFilterCount)
    }

    @Test
    fun testFilterByAccessibility() {
        viewModel.toggleAccessibleOnly()
        val results = viewModel.applyCurrentFilters(testItems)

        assertEquals(1, results.size)
        assertTrue(results.all { it.restroom.isAccessiblePmr })
        assertEquals("مرفق شامل مهيأ", results[0].restroom.name)
    }

    @Test
    fun testFilterByCleanliness() {
        viewModel.toggleCleanlinessOnly()
        val results = viewModel.applyCurrentFilters(testItems)

        assertEquals(2, results.size)
        assertTrue(results.all { it.restroom.rating >= 4.2f && it.restroom.hasWater })
        assertFalse(results.any { it.restroom.id == 3L })
    }

    @Test
    fun testFilterByChangingTable() {
        viewModel.toggleChangingTableOnly()
        val results = viewModel.applyCurrentFilters(testItems)

        assertEquals(1, results.size)
        assertTrue(results.all { it.restroom.hasChangingTable })
        assertEquals("مرفق شامل مهيأ", results[0].restroom.name)
    }

    @Test
    fun testResetAllFilters() {
        viewModel.toggleAccessibleOnly()
        viewModel.toggleChangingTableOnly()
        viewModel.toggleCleanlinessOnly()
        assertTrue(viewModel.filterState.value.activeFilterCount >= 3)

        viewModel.resetAllFilters()
        assertEquals(0, viewModel.filterState.value.activeFilterCount)
        assertFalse(viewModel.filterState.value.accessiblePmrOnly)
        assertFalse(viewModel.filterState.value.highCleanlinessOnly)
        assertFalse(viewModel.filterState.value.changingTableOnly)

        val results = viewModel.applyCurrentFilters(testItems)
        assertEquals(3, results.size)
    }
}
