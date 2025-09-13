package com.example.aroundegyptapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aroundegyptapplication.presentation.experience.view.ExperienceScreen
import com.example.aroundegyptapplication.presentation.home.view.HomeScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(
            route = Screen.Experience.route,
            arguments = Screen.Experience.arguments
        ) {
            ExperienceScreen(navController = navController)
        }
    }
}