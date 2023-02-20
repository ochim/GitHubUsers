package com.example.githubusers.data.repository

import com.example.githubusers.api.GitHubUsersInterface
import com.example.githubusers.domain.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class GitHubUsersRemoteDataSource(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val usersInterface: GitHubUsersInterface
) {

    fun usersList(since: Int?): Flow<List<User>> = flow {
        val response = if (since == null) usersInterface.fetchUsers().execute()
        else usersInterface.fetchNextUsers(since).execute()

        if (response.isSuccessful) {
            emit(response.body()!!)
        } else {
            throw Exception("usersList since:$since error code ${response.code()} ${response.message()}")
        }
    }.flowOn(ioDispatcher)

    suspend fun userInfo(username: String): User {
        return withContext(ioDispatcher) {
            val response = usersInterface.fetchUser(username).execute()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                throw Exception("userInfo error code ${response.code()} ${response.message()}")
            }
        }
    }
}