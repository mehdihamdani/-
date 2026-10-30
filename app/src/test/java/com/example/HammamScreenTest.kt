package com.example

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.RestroomEntity
import com.example.data.model.RestroomType
import com.example.ui.components.HammamDetailView
import com.example.ui.screens.HammamsDirectoryScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.RestroomWithDistance
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class HammamScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `HammamDetailView displays schedules, FlowRow service tags, and opens Facebook in external browser`() {
        val sampleHammam = RestroomEntity(
            id = 23L,
            name = "المركب الحموي حمام ريغة",
            nameFr = "Station Thermale Hammam Righa",
            wilaya = "عين الدفلى",
            commune = "حمام ريغة",
            type = RestroomType.HAMMAM,
            latitude = 36.3792,
            longitude = 2.3964,
            isFree = false,
            priceDzd = 500,
            hasWater = true,
            hasShower = true,
            hasWomenSection = true,
            isThermalSpring = true,
            hasSteamSauna = true,
            hasKessaScrub = true,
            hasPrivateShowerRooms = true,
            hasTowelRental = true,
            hasTeaLounge = true,
            hammamMenHours = "الرجال: 07:00 - 19:00",
            hammamWomenHours = "النساء: جناح منفصل 08:00 - 18:00",
            hammamPhone = "027591234",
            hammamServicesNotes = "حوض سباحة حموي 500 دج • غرف استجمام عائلية",
            facebookUrl = "www.facebook.com/HammamRighaOfficiel"
        )

        composeTestRule.setContent {
            MyApplicationTheme {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    HammamDetailView(
                        hammam = sampleHammam,
                        distanceText = "65.0 كم",
                        showHeaderCard = true
                    )
                }
            }
        }

        composeTestRule.waitForIdle()

        // Verify Hammam header & schedules
        composeTestRule.onNodeWithTag("hammam_detail_view").assertIsDisplayed()
        composeTestRule.onNodeWithTag("hammam_info_card").assertIsDisplayed()
        composeTestRule.onNodeWithText("المركب الحموي حمام ريغة").assertIsDisplayed()
        composeTestRule.onNodeWithTag("hammam_men_hours_card").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("hammam_women_hours_card").performScrollTo().assertIsDisplayed()

        // Verify FlowRow service tags & notes
        composeTestRule.onNodeWithTag("hammam_services_flow_row").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("hammam_service_tag_thermal_spring").assertIsDisplayed()
        composeTestRule.onNodeWithTag("hammam_service_tag_steam_sauna").assertIsDisplayed()
        composeTestRule.onNodeWithTag("hammam_service_tag_kessa_scrub").assertIsDisplayed()
        composeTestRule.onNodeWithTag("hammam_services_notes_card").performScrollTo().assertIsDisplayed()

        // Verify external Facebook browser launch
        composeTestRule.onNodeWithTag("hammam_facebook_button").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("hammam_facebook_button").performClick()
        composeTestRule.waitForIdle()

        val app = ApplicationProvider.getApplicationContext<Application>()
        val startedIntent = Shadows.shadowOf(app).nextStartedActivity
        assertNotNull("Expected external browser Intent to be launched", startedIntent)
        assertEquals(Intent.ACTION_VIEW, startedIntent.action)
        assertEquals("https://www.facebook.com/HammamRighaOfficiel", startedIntent.dataString)
    }

    @Test
    fun `HammamsDirectoryScreen filters thermal springs and opens selected Hammam profile`() {
        val thermalHammam = RestroomEntity(
            id = 24L,
            name = "المركب الحموي حمام دباغ",
            nameFr = "Station Thermale Hammam Debagh",
            wilaya = "قالمة",
            commune = "حمام دباغ",
            type = RestroomType.HAMMAM,
            latitude = 36.4619,
            longitude = 7.2661,
            isFree = false,
            priceDzd = 450,
            isThermalSpring = true,
            hasSteamSauna = true,
            hasKessaScrub = true,
            hasWomenSection = true
        )
        val traditionalHammam = RestroomEntity(
            id = 5L,
            name = "حمام البستان التقليدي والعصري",
            nameFr = "Hammam El Boustane",
            wilaya = "الجزائر العاصمة",
            commune = "القبة",
            type = RestroomType.HAMMAM,
            latitude = 36.7262,
            longitude = 3.0869,
            isFree = false,
            priceDzd = 350,
            isThermalSpring = false,
            hasSteamSauna = true,
            hasKessaScrub = true,
            hasWomenSection = true
        )

        var selectedHammamId: Long? = null
        val items = listOf(
            RestroomWithDistance(traditionalHammam, 3200.0, "3.2 كم"),
            RestroomWithDistance(thermalHammam, 420000.0, "420.0 كم")
        )

        composeTestRule.setContent {
            MyApplicationTheme {
                HammamsDirectoryScreen(
                    hammams = items,
                    onHammamSelected = { selectedHammamId = it.id },
                    onNavigateClick = {},
                    onFavoriteToggle = {},
                    onAddHammamClick = {},
                    onBack = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify directory screen and filter by thermal springs
        composeTestRule.onNodeWithTag("hammams_directory_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("hammam_filter_thermal").performClick()
        composeTestRule.waitForIdle()

        // Thermal Hammam is displayed and selecting its detail button triggers callback
        composeTestRule.onNodeWithTag("hammams_directory_screen")
            .performScrollToNode(androidx.compose.ui.test.hasTestTag("open_hammam_detail_24"))
        composeTestRule.onNodeWithText("المركب الحموي حمام دباغ").assertIsDisplayed()
        composeTestRule.onNodeWithTag("open_hammam_detail_24").performScrollTo().performClick()
        composeTestRule.waitForIdle()

        assertEquals(24L, selectedHammamId)
    }
}
