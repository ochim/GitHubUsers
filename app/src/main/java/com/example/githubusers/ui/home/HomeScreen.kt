package com.example.githubusers.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.example.githubusers.R
import com.example.githubusers.domain.User
import com.example.githubusers.ui.FetchNetworkModelState

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    onNavigateToDetails: () -> Unit
) {
    Scaffold(
        topBar = {
            HomeTopBar()
        },
        content = {
            UserListContent(it, mainViewModel, onNavigateToDetails)
        }
    )
}

@Composable
fun HomeTopBar(
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.h6
            )
        }
    )
}

@Composable
fun UserListContent(
    paddingValues: PaddingValues,
    mainViewModel: MainViewModel,
    onNavigateToDetails: () -> Unit
) {
    val users = mainViewModel.usersLiveState.observeAsState()
    when (users.value) {
        is FetchNetworkModelState.NeverFetched -> {}
        is FetchNetworkModelState.Fetching -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is FetchNetworkModelState.RefreshedOK -> {
            val data = (users.value as FetchNetworkModelState.RefreshedOK<List<User>>).data
            if (data.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    items(data) {
                        UserListItem(user = it, onNavigateToDetails)
                    }
                }
            }
        }
        else -> {}
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserListItem(
    user: User,
    onNavigateToDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp)
            .height(50.dp)
            .fillMaxWidth(),
        elevation = 4.dp,
        onClick = onNavigateToDetails
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            user.avatar_url?.let {
                Image(
                    modifier = Modifier
                        .padding(
                            end = 8.dp,
                        )
                        .width(50.dp),
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(data = it)
                            .apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                scale(Scale.FILL)
                            }).build()
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
            Text(
                style = MaterialTheme.typography.subtitle1,
                text = user.login
            )

        }
    }
}