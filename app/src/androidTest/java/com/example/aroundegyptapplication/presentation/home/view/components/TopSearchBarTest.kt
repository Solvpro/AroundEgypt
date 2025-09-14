package com.example.aroundegyptapplication.presentation.home.view.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.aroundegyptapplication.presentation.theme.AroundEgyptTheme
import org.junit.Rule
import org.junit.Test

class TopSearchBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun topSearchBar_initialState_showsPlaceholder() {
        composeTestRule.setContent {
            AroundEgyptTheme {
                TopSearchBar(
                    query = "",
                    onQueryChange = {},
                    onSearch = {},
                    onClear = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Try \"Luxor\"").assertIsDisplayed()
    }

    @Test
    fun topSearchBar_withQuery_showsClearButton() {
        composeTestRule.setContent {
            AroundEgyptTheme {
                TopSearchBar(
                    query = "temple",
                    onQueryChange = {},
                    onSearch = {},
                    onClear = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Clear").assertIsDisplayed()
    }

    @Test
    fun topSearchBar_textInput_triggersOnQueryChange() {
        var queryChanged = ""
        
        composeTestRule.setContent {
            AroundEgyptTheme {
                TopSearchBar(
                    query = "",
                    onQueryChange = { queryChanged = it },
                    onSearch = {},
                    onClear = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Try \"Luxor\"").performTextInput("temple")
        
        assert(queryChanged == "temple")
    }

    @Test
    fun topSearchBar_clearButton_triggersOnClear() {
        var clearTriggered = false
        
        composeTestRule.setContent {
            AroundEgyptTheme {
                TopSearchBar(
                    query = "temple",
                    onQueryChange = {},
                    onSearch = {},
                    onClear = { clearTriggered = true }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Clear").performClick()
        
        assert(clearTriggered)
    }
}