package com.example.githubusers.data.repository

import com.example.githubusers.domain.User
import com.example.githubusers.domain.UserItem
import com.example.githubusers.domain.toUserItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GitHubRepository(
    private val gitHubUsersRemoteDataSource: GitHubUsersRemoteDataSource
) {
    fun usersList(since: Int?): Flow<List<UserItem>> =
        gitHubUsersRemoteDataSource.usersList(since).map { users -> users.map { it.toUserItem() } }

    suspend fun userInfo(
        username: String
    ): User = gitHubUsersRemoteDataSource.userInfo(username)
}