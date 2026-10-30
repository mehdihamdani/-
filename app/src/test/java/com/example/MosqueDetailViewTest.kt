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
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.RestroomEntity
import com.example.data.model.RestroomType
import com.example.ui.components.MosqueDetailView
import com.example.ui.components.openExternalBrowserUrl
import com.example.ui.theme.MyApplicationTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class MosqueDetailViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `MosqueDetailView displays mosque info, FlowRow service tags, and opens Facebook in external browser`() {
        val sampleMosque = RestroomEntity(
            id = 101L,
            name = "جامع الجزائر الأعظم",
            nameFr = "Djamaâ El-Djazaïr",
            wilaya = "الجزائر",
            commune = "المحمدية",
            type = RestroomType.MOSQUE,
            latitude = 36.7358,
            longitude = 3.1419,
            isFree = true,
            hasWater = true,
            hasWudu = true,
            hasWomenSection = true,
            hasQuranSchool = true,
            hasFridayPrayer = true,
            hasLibrary = true,
            hasParking = true,
            imamName = "اللجنة العلمية لجامع الجزائر",
            mosqueServicesNotes = "حلقات تحفيظ القرآن الكريم يومياً بعد صلاة العصر",
            facebookUrl = "www.facebook.com/DjamaaElDjazair"
        )

        composeTestRule.setContent {
            MyApplicationTheme {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    MosqueDetailView(
                        mosque = sampleMosque,
                        distanceText = "1.2 كم",
                        showHeaderCard = true
                    )
                }
            }
        }

        composeTestRule.waitForIdle()

        // Verify mosque name & header card
        composeTestRule.onNodeWithTag("mosque_detail_view").assertIsDisplayed()
        composeTestRule.onNodeWithTag("mosque_info_card").assertIsDisplayed()
        composeTestRule.onNodeWithText("جامع الجزائر الأعظم").assertIsDisplayed()

        // Verify FlowRow services section & individual service tags
        composeTestRule.onNodeWithTag("mosque_services_flow_row").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("mosque_service_tag_quran_school").assertIsDisplayed()
        composeTestRule.onNodeWithTag("mosque_service_tag_friday_prayer").assertIsDisplayed()
        composeTestRule.onNodeWithTag("mosque_service_tag_library").assertIsDisplayed()
        composeTestRule.onNodeWithTag("mosque_services_schedule_card").performScrollTo().assertIsDisplayed()

        // Scroll to the Facebook button, click it, and verify external browser ACTION_VIEW Intent
        composeTestRule.onNodeWithTag("mosque_facebook_button").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("mosque_facebook_button").performClick()
        composeTestRule.waitForIdle()

        val app = ApplicationProvider.getApplicationContext<Application>()
        val startedIntent = Shadows.shadowOf(app).nextStartedActivity
        assertNotNull("Expected external browser Intent to be launched", startedIntent)
        assertEquals(Intent.ACTION_VIEW, startedIntent.action)
        assertEquals("https://www.facebook.com/DjamaaElDjazair", startedIntent.dataString)
    }

    @Test
    fun `openExternalBrowserUrl rejects unsafe javascript scheme`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val result = openExternalBrowserUrl(app, "javascript:alert(1)")
        assertFalse(result)
    }
}
