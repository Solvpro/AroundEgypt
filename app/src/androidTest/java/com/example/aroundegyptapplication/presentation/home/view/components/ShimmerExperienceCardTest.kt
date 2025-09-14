package com.example.aroundegyptapplication.presentation.home.view.components

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.aroundegyptapplication.presentation.theme.AroundEgyptTheme
import org.junit.Rule
import org.junit.Test

class ShimmerExperienceCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shimmerExperienceCard_isDisplayed() {
        composeTestRule.setContent {
            AroundEgyptTheme {
                ShimmerExperienceCard()
            }
        }

        // Verify shimmer animation is running
        composeTestRule.waitForIdle()
        // The shimmer effect should be visible, but we can't easily test animations
        // Instead, we verify the component renders without crashing
    }

    @Test
    fun shimmerExperienceCard_horizontalLayout() {
        composeTestRule.setContent {
            AroundEgyptTheme {
                ShimmerExperienceCard(isHorizontal = true)
            }
        }

        composeTestRule.waitForIdle()
        // Component should render for horizontal layout
    }

    @Test
    fun shimmerExperienceCard_verticalLayout() {
        composeTestRule.setContent {
            AroundEgyptTheme {
                ShimmerExperienceCard(isHorizontal = false)
            }
        }

        composeTestRule.waitForIdle()
        // Component should render for vertical layout
    }
}
