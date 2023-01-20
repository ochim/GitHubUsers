package com.example.githubusers.api

import com.example.githubusers.domain.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubUsersInterface {
    @GET("users")
    fun fetchUsers(): Call<List<User>>

    @GET("users")
    fun fetchNextUsers(@Query("since") id: Int): Call<List<User>>

    @GET("users/{username}")
    fun fetchUser(@Path("username") username: String): Call<User>
}
