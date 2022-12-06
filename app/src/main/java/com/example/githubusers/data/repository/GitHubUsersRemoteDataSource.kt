package com.example.githubusers.data.repository

import com.example.githubusers.domain.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.http.GET

interface GitHubUsersInterface {
    @GET("users")
    fun fetchUsers(): Call<List<User>>
}

class GitHubUsersRemoteDataSource(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val usersInterface: GitHubUsersInterface
) {

    suspend fun usersList(): List<User> {
        return withContext(ioDispatcher) {
            val response = usersInterface.fetchUsers().execute()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                throw Exception("usersList error code ${response.code()} ${response.message()}")
            }
        }
    }
}