package com.example.githubusers.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.githubusers.api.GitHubService
import com.example.githubusers.data.repository.GitHubRepository
import com.example.githubusers.data.repository.GitHubUsersInterface
import com.example.githubusers.data.repository.GitHubUsersRemoteDataSource
import com.example.githubusers.ui.details.DetailsScreen
import com.example.githubusers.ui.details.DetailsViewModel
import com.example.githubusers.ui.home.HomeScreen
import com.example.githubusers.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val navigateToDetails: (username: String) -> Unit =
        { username -> navController.navigate(Screen.Details.passUsername(username)) }

    val homeViewModel = HomeViewModel(
        GitHubRepository(
            GitHubUsersRemoteDataSource(
                Dispatchers.IO,
                GitHubService.retrofit.create(GitHubUsersInterface::class.java)
            )
        )
    )

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
            val name = remember(backStackEntry) {
                backStackEntry.arguments?.getString(USER_NAME_ARGUMENT_KEY)
            }
            name?.let {
                val detailsViewModel = DetailsViewModel(
                    GitHubRepository(
                        GitHubUsersRemoteDataSource(
                            Dispatchers.IO,
                            GitHubService.retrofit.create(GitHubUsersInterface::class.java)
                        )
                    ),
                    it
                )
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
