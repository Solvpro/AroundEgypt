package com.example.aroundegyptapplication.presentation.experience.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.aroundegyptapplication.domain.model.Experience
import com.example.aroundegyptapplication.presentation.experience.model.ExperienceUiState
import com.example.aroundegyptapplication.presentation.theme.AroundEgyptTheme
import org.junit.Rule
import org.junit.Test

class ExperienceScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun experienceScreen_loadingState_showsProgressIndicator() {
        // Given
        val loadingState = ExperienceUiState(isLoading = true)

        composeTestRule.setContent {
            AroundEgyptTheme {
                // You would need to create a test version of ExperienceScreen
                // that accepts a UiState parameter for testing
                TestExperienceScreen(uiState = loadingState)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Loading").assertIsDisplayed()
    }

    @Test
    fun experienceScreen_withExperience_displaysContent() {
        // Given
        val experience = Experience(
            id = "1",
            title = "Abu Simbel Temples",
            description = "The Abu Simbel temples are two massive rock temples...",
            coverPhoto = "https://example.com/image.jpg",
            city = "Aswan, Egypt",
            viewsNo = 39266,
            likesNo = 3317,
            isLiked = false,
            recommended = true
        )
        val state = ExperienceUiState(experience = experience, isLoading = false)

        composeTestRule.setContent {
            AroundEgyptTheme {
                TestExperienceScreen(uiState = state)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Abu Simbel Temples").assertIsDisplayed()
        composeTestRule.onNodeWithText("Aswan, Egypt").assertIsDisplayed()
        composeTestRule.onNodeWithText("39266").assertIsDisplayed()
        composeTestRule.onNodeWithText("3317").assertIsDisplayed()
        composeTestRule.onNodeWithText("View in 360°").assertIsDisplayed()
        composeTestRule.onNodeWithText("View Details").assertIsDisplayed()
    }

    @Test
    fun experienceScreen_backButton_navigatesBack() {
        // Given
        val experience = createTestExperience()
        val state = ExperienceUiState(experience = experience, isLoading = false)
        var backPressed = false

        composeTestRule.setContent {
            AroundEgyptTheme {
                TestExperienceScreen(
                    uiState = state,
                    onBackClick = { backPressed = true }
                )
            }
        }

        // When
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Then
        assert(backPressed)
    }

    private fun createTestExperience(): Experience {
        return Experience(
            id = "1",
            title = "Abu Simbel Temples",
            description = "Amazing ancient temples",
            coverPhoto = "https://example.com/image.jpg",
            city = "Aswan, Egypt",
            viewsNo = 39266,
            likesNo = 3317,
            isLiked = false,
            recommended = true
        )
    }

}