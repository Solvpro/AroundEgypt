package com.example.aroundegyptapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.navigation.testing.TestNavHostController
import com.example.aroundegyptapplication.presentation.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()
        
        composeTestRule.setContent {
            navController = TestNavHostController(ApplicationProvider.getApplicationContext())
            // Set up your navigation graph here
        }
    }

    @Test
    fun navHost_verifyStartDestination() {
        assert(navController.currentDestination?.route == "home")
    }

    @Test
    fun navHost_clickExperience_navigatesToDetail() {
        // This would require setting up the full navigation and clicking on an experience card
        // Then verifying navigation to the detail screen
    }
}