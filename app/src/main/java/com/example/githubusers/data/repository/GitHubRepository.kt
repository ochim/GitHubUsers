package com.example.githubusers.data.repository

import com.example.githubusers.domain.User

class GitHubRepository(
    private val gitHubUsersRemoteDataSource: GitHubUsersRemoteDataSource
) {
    suspend fun usersList(since: Int?): List<User> {
        return if (since == null) gitHubUsersRemoteDataSource.usersList()
        else gitHubUsersRemoteDataSource.nextUsersList(since)
    }

    suspend fun userInfo(
        username: String
    ): User = gitHubUsersRemoteDataSource.userInfo(username)
}