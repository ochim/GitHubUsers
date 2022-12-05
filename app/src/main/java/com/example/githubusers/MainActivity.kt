package com.example.githubusers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.githubusers.api.GitHubService
import com.example.githubusers.data.repository.GitHubRepository
import com.example.githubusers.data.repository.GitHubUsersInterface
import com.example.githubusers.data.repository.GitHubUsersRemoteDataSource
import com.example.githubusers.navigation.MyAppNavHost
import com.example.githubusers.ui.home.MainViewModel
import com.example.githubusers.ui.theme.GitHubUsersTheme
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitHubUsersTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val mainViewModel = MainViewModel(
                        GitHubRepository(
                            GitHubUsersRemoteDataSource(
                                Dispatchers.IO,
                                GitHubService.retrofit.create(GitHubUsersInterface::class.java)
                            )
                        )
                    )

                    navController = rememberNavController()
                    MyAppNavHost(
                        modifier = Modifier,
                        navController = navController,
                        mainViewModel = mainViewModel
                    )
                    mainViewModel.usersList()
                }
            }
        }
    }
}
