package com.example.githubusers.data.repository

import com.example.githubusers.domain.User
import kotlinx.coroutines.flow.Flow

class GitHubRepository(
    private val gitHubUsersRemoteDataSource: GitHubUsersRemoteDataSource
) {
    fun usersList(since: Int?): Flow<List<User>> {
        return if (since == null) gitHubUsersRemoteDataSource.usersList
        else gitHubUsersRemoteDataSource.nextUsersList(since)
    }

    suspend fun userInfo(
        username: String
    ): User = gitHubUsersRemoteDataSource.userInfo(username)
}