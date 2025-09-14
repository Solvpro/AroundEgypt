package com.example.aroundegyptapplication.e2e

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.example.aroundegyptapplication.presentation.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ExperienceFlowTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun completeFlow_searchAndViewExperience() {
        // 1. Verify home screen loads
        composeTestRule.onNodeWithText("Welcome!").assertIsDisplayed()

        // 2. Search for experience
        composeTestRule.onNodeWithText("Try \"Luxor\"").performTextInput("temple")
        
        // 3. Wait for search results
        composeTestRule.waitForIdle()

        // 4. Click on first result (if available)
        // This would require having test data or mocked responses

        // 5. Verify navigation to detail screen
        // 6. Test like functionality
        // 7. Test back navigation
    }
}