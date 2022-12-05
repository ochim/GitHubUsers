package com.example.githubusers.data.repository

import com.example.githubusers.domain.User

class GitHubRepository(
    private val gitHubUsersRemoteDataSource: GitHubUsersRemoteDataSource
) {

    suspend fun usersList(): List<User> = gitHubUsersRemoteDataSource.usersList()
}