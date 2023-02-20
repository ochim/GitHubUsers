package com.example.githubusers.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusers.data.repository.GitHubRepository
import com.example.githubusers.domain.UserItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val gitHubRepository: GitHubRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState

    private var havingUsers: List<UserItem> = emptyList()

    init {
        userItemsList()
    }

    fun userItemsList() {
        if (_uiState.value == HomeUiState.Fetching) return

        _uiState.value = HomeUiState.Fetching

        viewModelScope.launch {
            gitHubRepository.usersList(null)
                .catch { e ->
                    _uiState.value = HomeUiState.Error(e as Exception)
                }.collect { userItems ->
                    _uiState.value = HomeUiState.RefreshedOK(userItems)
                    havingUsers = userItems
                }
        }
    }

    fun nextUserItemsList() {
        viewModelScope.launch {
            val lastId = havingUsers.last().id

            gitHubRepository.usersList(lastId)
                .catch { e ->
                    _uiState.value = HomeUiState.Error(e as Exception)
                }.collect { userItems ->
                    if (userItems.isNotEmpty()) {
                        val mutable = havingUsers.toMutableList()
                        mutable.addAll(userItems)
                        _uiState.value = HomeUiState.RefreshedOK(mutable)
                        havingUsers = mutable.toList()
                    }
                }
        }
    }
}