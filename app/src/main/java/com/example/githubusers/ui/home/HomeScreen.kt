package com.example.githubusers.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.githubusers.domain.User
import androidx.compose.runtime.livedata.observeAsState
import com.example.githubusers.ui.FetchNetworkModelState

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    onNavigateToDetails: () -> Unit
) {
    val users = mainViewModel.usersLiveState.observeAsState()
    when (users.value) {
        is FetchNetworkModelState.NeverFetched -> {}
        is FetchNetworkModelState.Fetching -> {}
        is FetchNetworkModelState.RefreshedOK -> {
            val data = (users.value as FetchNetworkModelState.RefreshedOK<List<User>>).data
            if (data.isNotEmpty()) {
                LazyColumn(
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
            .height(30.dp)
            .fillMaxWidth(),
        elevation = 4.dp,
        onClick = onNavigateToDetails
    ) {
        Text(user.login)
    }
}