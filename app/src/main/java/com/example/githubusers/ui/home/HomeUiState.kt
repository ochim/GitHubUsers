package com.example.githubusers.ui.home

import com.example.githubusers.domain.UserItem

sealed class HomeUiState {
    object Idle : HomeUiState()
    object Fetching : HomeUiState()
    class RefreshedOK(val data: List<UserItem>) : HomeUiState()
    class Error(val exception: Exception) : HomeUiState()
}