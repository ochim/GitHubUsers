package com.example.githubusers.ui.details

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.githubusers.api.GitHubService
import com.example.githubusers.data.repository.GitHubRepository
import com.example.githubusers.api.GitHubUsersInterface
import com.example.githubusers.data.repository.GitHubUsersRemoteDataSource
import com.example.githubusers.ui.theme.GitHubUsersTheme
import kotlinx.coroutines.Dispatchers

class DetailsFragment : Fragment() {
    private val args: DetailsFragmentArgs by navArgs()

    private val detailsViewModel = DetailsViewModel(
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
        detailsViewModel.userInfo(args.argUsername)
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                GitHubUsersTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        DetailsScreen(
                            detailsViewModel = detailsViewModel,
                            popBackStack = { findNavController().popBackStack() },
                            username = args.argUsername
                        )
                    }
                }
            }
        }
    }
}