package com.example.githubusers.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusers.data.repository.GitHubRepository
import com.example.githubusers.domain.User
import com.example.githubusers.ui.FetchNetworkModelState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val gitHubRepository: GitHubRepository,
) : ViewModel() {

    private val _usersLiveState =
        MutableLiveData<FetchNetworkModelState<List<User>>>(FetchNetworkModelState.NeverFetched)
    val usersLiveState: LiveData<FetchNetworkModelState<List<User>>>
        get() = _usersLiveState

    private var havingUsers: List<User> = emptyList()

    init {
        usersList()
    }

    fun usersList() {
        if (_usersLiveState.value == FetchNetworkModelState.Fetching) return

        _usersLiveState.value = FetchNetworkModelState.Fetching

        viewModelScope.launch {
            gitHubRepository.usersList(null)
                .catch { e ->
                    _usersLiveState.value = FetchNetworkModelState.FetchedError(e as Exception)
                }.collect { users ->
                    _usersLiveState.value = FetchNetworkModelState.RefreshedOK(users)
                    havingUsers = users
                }
        }
    }

    fun nextUsersList() {
        viewModelScope.launch {
            val lastId = havingUsers.last().id

            gitHubRepository.usersList(lastId)
                .catch { e ->
                    _usersLiveState.value = FetchNetworkModelState.FetchedError(e as Exception)
                }.collect { users ->
                    if (users.isNotEmpty()) {
                        val mutable = havingUsers.toMutableList()
                        mutable.addAll(users)
                        _usersLiveState.value = FetchNetworkModelState.RefreshedOK(mutable)
                        havingUsers = mutable.toList()
                    }
                }
        }
    }
}