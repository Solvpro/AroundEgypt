package com.example.aroundegyptapplication.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object Home : Screen("home")
    
    object Experience : Screen(
        route = "experience/{experienceId}",
        arguments = listOf(
            navArgument("experienceId") {
                type = NavType.StringType
            }
        )
    )
}