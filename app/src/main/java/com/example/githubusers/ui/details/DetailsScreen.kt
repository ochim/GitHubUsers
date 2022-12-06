package com.example.githubusers.ui.details

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.githubusers.R
import com.example.githubusers.domain.User
import com.example.githubusers.ui.FetchNetworkModelState

@Composable
fun DetailsScreen(
    detailsViewModel: DetailsViewModel,
    popBackStack: () -> Unit,
    username: String
) {
    detailsViewModel.reset()
    detailsViewModel.userInfo(username)
    Scaffold(
        topBar = {
            DetailsTopBar(popBackStack)
        },
        content = {
            DetailsContent(it, detailsViewModel, username)
        }
    )
}

@Composable
fun DetailsTopBar(
    popBackStack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.details),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.h6
            )
        },
        navigationIcon = {
            IconButton(onClick = { popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Icon",
                )
            }
        },
    )
}

@Composable
fun DetailsContent(
    paddingValues: PaddingValues,
    detailsViewModel: DetailsViewModel,
    username: String
) {
    val users = detailsViewModel.userLiveState.observeAsState()
    when (users.value) {
        is FetchNetworkModelState.NeverFetched -> {
            Text(username)
        }
        is FetchNetworkModelState.Fetching -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is FetchNetworkModelState.RefreshedOK -> {
            val user = (users.value as? FetchNetworkModelState.RefreshedOK<User>)?.data
            if (user != null) {
                Column(
                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                ) {
                    Text(text = user.login)
                    Text(text = "${user.id}")
                    Text(text = user.avatar_url ?: "")
                }
            } else {
                Text(username)
            }
        }
        is FetchNetworkModelState.FetchedError -> {
            Text(username)
            val exception = (users.value as FetchNetworkModelState.FetchedError).exception
            Toast.makeText(LocalContext.current, exception.message, Toast.LENGTH_LONG).show()
        }
        else -> {}
    }
}