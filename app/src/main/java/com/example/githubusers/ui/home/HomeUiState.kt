package com.example.githubusers.ui.home

import com.example.githubusers.domain.UserItem

class HomeUiState(
    val loading: Loading = Loading.IDLE,
    val userItems: List<UserItem> = emptyList(),
    val exception: Exception? = null,
)

enum class Loading {
    IDLE, FETCHING, APPENDING, ERROR
}