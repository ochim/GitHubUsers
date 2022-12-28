package com.example.githubusers.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusers.data.repository.GitHubRepository
import com.example.githubusers.domain.User
import com.example.githubusers.ui.FetchNetworkModelState
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val gitHubRepository: GitHubRepository,
    private val username: String? = null
) : ViewModel() {

    private val _userLiveState =
        MutableLiveData<FetchNetworkModelState<User>>(FetchNetworkModelState.NeverFetched)
    val userLiveState: LiveData<FetchNetworkModelState<User>>
        get() = _userLiveState

    init {
        if (!username.isNullOrEmpty()) userInfo(username)
    }

    fun userInfo(name: String) {
        if (_userLiveState.value == FetchNetworkModelState.Fetching) return

        _userLiveState.value = FetchNetworkModelState.Fetching

        viewModelScope.launch {
            try {
                _userLiveState.value =
                    FetchNetworkModelState.RefreshedOK(gitHubRepository.userInfo(name))
            } catch (e: Exception) {
                _userLiveState.value = FetchNetworkModelState.FetchedError(e)
            }
        }
    }
}