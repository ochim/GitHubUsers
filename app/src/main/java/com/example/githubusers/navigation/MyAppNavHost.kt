package com.example.githubusers.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.githubusers.ui.details.DetailsScreen
import com.example.githubusers.ui.details.DetailsViewModel
import com.example.githubusers.ui.home.HomeScreen
import com.example.githubusers.ui.home.HomeViewModel

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel,
    detailsViewModel: DetailsViewModel,
) {
    val navigateToDetails: (username: String) -> Unit =
        { username -> navController.navigate(Screen.Details.passUsername(username)) }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                homeViewModel = homeViewModel,
                onNavigateToDetails = navigateToDetails,
            )
        }
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument(USER_NAME_ARGUMENT_KEY) { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString(USER_NAME_ARGUMENT_KEY)?.let {
                DetailsScreen(
                    detailsViewModel = detailsViewModel,
                    popBackStack = { navController.popBackStack() },
                    username = it
                )
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Details : Screen("details/{$USER_NAME_ARGUMENT_KEY}") {
        fun passUsername(username: String) = "details/$username"
    }
}

const val USER_NAME_ARGUMENT_KEY = "username"
