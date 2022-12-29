package com.example.githubusers.ui.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.example.githubusers.R
import com.example.githubusers.domain.User
import com.example.githubusers.ui.FetchNetworkModelState
import kotlinx.coroutines.flow.filter

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onNavigateToDetails: (String) -> Unit
) {
    Scaffold(
        topBar = {
            HomeTopBar()
        },
        content = {
            UserListContent(it, homeViewModel, onNavigateToDetails)
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
    homeViewModel: HomeViewModel,
    onNavigateToDetails: (String) -> Unit
) {
    val users = homeViewModel.usersLiveState.observeAsState()
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
            val data = (users.value as? FetchNetworkModelState.RefreshedOK<List<User>>)?.data
            if (!data.isNullOrEmpty()) {
                UserList(topPadding = paddingValues.calculateTopPadding(),
                    onNavigateToDetails = onNavigateToDetails,
                    users = data,
                    onAppearLastItem = {
                        homeViewModel.nextUsersList()
                    }
                )
            }
        }

        is FetchNetworkModelState.FetchedError -> {
            val exception = (users.value as FetchNetworkModelState.FetchedError).exception
            Toast.makeText(LocalContext.current, exception.message, Toast.LENGTH_LONG).show()
        }

        else -> {}
    }
}

@Composable
fun UserList(
    topPadding: Dp,
    onNavigateToDetails: (String) -> Unit,
    users: List<User>,
    onAppearLastItem: () -> Unit
) {
    val listState = rememberLazyListState().apply {
        OnAppearLastItem(onAppearLastItem = onAppearLastItem)
    }
    LazyColumn(
        modifier = Modifier.padding(top = topPadding),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        state = listState
    ) {
        items(items = users,
            key = { user -> user.id }
        ) {
            UserListItem(user = it, onNavigateToDetails)
        }
    }
}

@Composable
fun UserListItem(
    user: User,
    onNavigateToDetails: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp)
            .height(50.dp)
            .fillMaxWidth(),
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable {
                   onNavigateToDetails(user.login)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            user.avatar_url?.let {
                Image(
                    modifier = Modifier
                        .padding(end = 8.dp)
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

@Composable
fun LazyListState.OnAppearLastItem(onAppearLastItem: () -> Unit) {
    val isReachedToListEnd by remember {
        derivedStateOf {
            layoutInfo.visibleItemsInfo.size < layoutInfo.totalItemsCount &&
                    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { isReachedToListEnd }
            .filter { it }
            .collect {
                onAppearLastItem()
            }
    }
}