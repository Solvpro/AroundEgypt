package com.example.aroundegyptapplication.presentation.home.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.example.aroundegyptapplication.presentation.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun homeScreen_initialState_showsWelcomeMessage() {
        composeTestRule.onNodeWithText("Welcome!").assertIsDisplayed()
        composeTestRule.onNodeWithText(
            "Now you can explore any experience in 360 degrees and get all the details about it all in one place."
        ).assertIsDisplayed()
    }

    @Test
    fun homeScreen_initialState_showsSectionHeaders() {
        composeTestRule.onNodeWithText("Recommended Experiences").assertIsDisplayed()
        composeTestRule.onNodeWithText("Most Recent").assertIsDisplayed()
    }

    @Test
    fun homeScreen_searchBar_isVisible() {
        composeTestRule.onNodeWithText("Try \"Luxor\"").assertIsDisplayed()
    }

    @Test
    fun homeScreen_searchQuery_updatesSearchField() {
        // When
        composeTestRule.onNodeWithText("Try \"Luxor\"").performTextInput("temple")

        // Then
        composeTestRule.onNodeWithText("temple").assertIsDisplayed()
    }

    @Test
    fun homeScreen_clearSearch_clearsSearchField() {
        // Given
        composeTestRule.onNodeWithText("Try \"Luxor\"").performTextInput("temple")

        // When
        composeTestRule.onNodeWithContentDescription("Clear").performClick()

        // Then
        composeTestRule.onNodeWithText("Try \"Luxor\"").assertIsDisplayed()
    }

}