package com.example.githubusers.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusers.data.repository.GitHubRepository
import com.example.githubusers.domain.User
import kotlinx.coroutines.launch

class MainViewModel(
    private val gitHubRepository: GitHubRepository,
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    fun usersList() {
        viewModelScope.launch {
            _users.value = gitHubRepository.usersList()
        }
    }
}