package com.example.githubusers.ui.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.githubusers.domain.UserItem
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
    val state = homeViewModel.uiState.collectAsState()
    when (state.value) {
        is HomeUiState.Idle -> {}
        is HomeUiState.Fetching -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is HomeUiState.RefreshedOK -> {
            val data = (state.value as HomeUiState.RefreshedOK).data
            if (data.isNotEmpty()) {
                UserList(topPadding = paddingValues.calculateTopPadding(),
                    onNavigateToDetails = onNavigateToDetails,
                    users = data,
                    onAppearLastItem = {
                        homeViewModel.nextUserItemsList()
                    }
                )
            }
        }

        is HomeUiState.Error -> {
            val exception = (state.value as HomeUiState.Error).exception
            Toast.makeText(LocalContext.current, exception.message, Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun UserList(
    topPadding: Dp,
    onNavigateToDetails: (String) -> Unit,
    users: List<UserItem>,
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
    user: UserItem,
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
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onNavigateToDetails(user.login)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            user.avatarUrl?.let {
                Image(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(50.dp)
                        .clip(RoundedCornerShape(percent = 50)),
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(data = it)
                            .apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                scale(Scale.FILL)
                            }).build()
                    ),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop
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