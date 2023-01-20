package com.example.githubusers.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.githubusers.api.GitHubService
import com.example.githubusers.data.repository.GitHubRepository
import com.example.githubusers.api.GitHubUsersInterface
import com.example.githubusers.data.repository.GitHubUsersRemoteDataSource
import com.example.githubusers.ui.theme.GitHubUsersTheme
import kotlinx.coroutines.Dispatchers

class HomeFragment : Fragment() {
    private val homeViewModel = HomeViewModel(
        GitHubRepository(
            GitHubUsersRemoteDataSource(
                Dispatchers.IO,
                GitHubService.retrofit.create(GitHubUsersInterface::class.java)
            )
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Fragment の View が破棄されるときに Composition も破棄する
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                GitHubUsersTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                    }
                    HomeScreen(
                        homeViewModel = homeViewModel,
                        onNavigateToDetails = {
                            val action = HomeFragmentDirections.actionHomeToDetails(it)
                            findNavController().navigate(action)
                        }
                    )
                }
            }
        }
    }
}