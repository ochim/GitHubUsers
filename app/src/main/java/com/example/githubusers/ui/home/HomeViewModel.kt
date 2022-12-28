package com.example.githubusers.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusers.data.repository.GitHubRepository
import com.example.githubusers.domain.User
import com.example.githubusers.ui.FetchNetworkModelState
import kotlinx.coroutines.launch

class HomeViewModel(
    private val gitHubRepository: GitHubRepository,
) : ViewModel() {

    private val _usersLiveState =
        MutableLiveData<FetchNetworkModelState<List<User>>>(FetchNetworkModelState.NeverFetched)
    val usersLiveState: LiveData<FetchNetworkModelState<List<User>>>
        get() = _usersLiveState

    var havingUsers: List<User> = emptyList()

    init {
        usersList()
    }

    fun usersList() {
        if (_usersLiveState.value == FetchNetworkModelState.Fetching) return

        _usersLiveState.value = FetchNetworkModelState.Fetching

        viewModelScope.launch {
            try {
                val users = gitHubRepository.usersList(null)
                _usersLiveState.value = FetchNetworkModelState.RefreshedOK(users)
                havingUsers = users
            } catch (e: Exception) {
                _usersLiveState.value = FetchNetworkModelState.FetchedError(e)
            }
        }
    }

    fun nextUsersList() {
        viewModelScope.launch {
            try {
                val lastId = havingUsers.last().id

                val nextUsers = gitHubRepository.usersList(lastId)
                if (nextUsers.isNotEmpty()) {
                    val mutable = havingUsers.toMutableList()
                    mutable.addAll(nextUsers)
                    _usersLiveState.value = FetchNetworkModelState.RefreshedOK(mutable)
                    havingUsers = mutable.toList()
                }
            } catch (e: Exception) {
                _usersLiveState.value = FetchNetworkModelState.FetchedError(e)
            }
        }
    }
}