package com.example.aroundegyptapplication.presentation.home.view.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.aroundegyptapplication.domain.model.Experience
import com.example.aroundegyptapplication.presentation.theme.AroundEgyptTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExperienceCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockExperience: Experience
    private var clickedExperienceId = ""
    private var likedExperienceId = ""

    @Before
    fun setup() {
        mockExperience = Experience(
            id = "test_id",
            title = "Abu Simbel Temples",
            description = "Amazing temples in southern Egypt",
            coverPhoto = "https://example.com/image.jpg",
            city = "Aswan",
            viewsNo = 39266,
            likesNo = 45,
            isLiked = false,
            recommended = true
        )
    }

    @Test
    fun experienceCard_displaysCorrectContent() {
        // Given
        composeTestRule.setContent {
            AroundEgyptTheme {
                ExperienceCard(
                    experience = mockExperience,
                    onClick = { clickedExperienceId = mockExperience.id },
                    onLike = { likedExperienceId = mockExperience.id }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Abu Simbel Temples").assertIsDisplayed()
        composeTestRule.onNodeWithText("39266").assertIsDisplayed()
        composeTestRule.onNodeWithText("45").assertIsDisplayed()
    }

    @Test
    fun experienceCard_clickTriggersOnClick() {
        // Given
        composeTestRule.setContent {
            AroundEgyptTheme {
                ExperienceCard(
                    experience = mockExperience,
                    onClick = { clickedExperienceId = mockExperience.id },
                    onLike = { likedExperienceId = mockExperience.id }
                )
            }
        }

        // When
        composeTestRule.onNodeWithText("Abu Simbel Temples").performClick()

        // Then
        assert(clickedExperienceId == "test_id")
    }

    @Test
    fun experienceCard_likeButtonClick_triggersOnLike() {
        // Given
        composeTestRule.setContent {
            AroundEgyptTheme {
                ExperienceCard(
                    experience = mockExperience,
                    onClick = { clickedExperienceId = mockExperience.id },
                    onLike = { likedExperienceId = mockExperience.id }
                )
            }
        }

        // When
        composeTestRule.onNodeWithContentDescription("Like").performClick()

        // Then
        assert(likedExperienceId == "test_id")
    }

    @Test
    fun experienceCard_likedState_showsFilledHeart() {
        // Given
        val likedExperience = mockExperience.copy(isLiked = true)
        
        composeTestRule.setContent {
            AroundEgyptTheme {
                ExperienceCard(
                    experience = likedExperience,
                    onClick = {},
                    onLike = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithContentDescription("Unlike").assertIsDisplayed()
    }

    @Test
    fun experienceCard_recommendedBadge_visibleForHorizontalCard() {
        // Given
        composeTestRule.setContent {
            AroundEgyptTheme {
                ExperienceCard(
                    experience = mockExperience,
                    onClick = {},
                    onLike = {},
                    isHorizontal = true
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("RECOMMENDED").assertIsDisplayed()
    }

    @Test
    fun experienceCard_recommendedBadge_hiddenForVerticalCard() {
        // Given
        composeTestRule.setContent {
            AroundEgyptTheme {
                ExperienceCard(
                    experience = mockExperience,
                    onClick = {},
                    onLike = {},
                    isHorizontal = false
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("RECOMMENDED").assertDoesNotExist()
    }
}