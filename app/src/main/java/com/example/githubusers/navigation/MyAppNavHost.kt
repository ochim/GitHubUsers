package com.example.githubusers.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.githubusers.ui.details.DetailsScreen
import com.example.githubusers.ui.home.HomeScreen

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToDetails = { navController.navigate(Screen.Details.route) },
            )
        }
        composable(Screen.Details.route) {
            DetailsScreen()
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Details : Screen("details_screen")
}