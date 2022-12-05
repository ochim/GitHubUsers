package com.example.githubusers.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusers.data.repository.GitHubRepository
import com.example.githubusers.domain.User
import com.example.githubusers.ui.FetchNetworkModelState
import kotlinx.coroutines.launch

class MainViewModel(
    private val gitHubRepository: GitHubRepository,
) : ViewModel() {

    private val _usersLiveState =
        MutableLiveData<FetchNetworkModelState<List<User>>>(FetchNetworkModelState.NeverFetched)
    val usersLiveState: LiveData<FetchNetworkModelState<List<User>>>
        get() = _usersLiveState

    fun usersList() {
        viewModelScope.launch {
            _usersLiveState.value = FetchNetworkModelState.RefreshedOK(gitHubRepository.usersList())
        }
    }
}