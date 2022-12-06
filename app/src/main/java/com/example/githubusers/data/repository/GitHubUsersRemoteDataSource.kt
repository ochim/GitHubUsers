package com.example.githubusers.data.repository

import com.example.githubusers.domain.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubUsersInterface {
    @GET("users")
    fun fetchUsers(): Call<List<User>>

    @GET("users/{username}")
    fun fetchUser(@Path("username") username: String): Call<User>
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