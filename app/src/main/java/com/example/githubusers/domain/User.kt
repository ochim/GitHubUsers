package com.example.githubusers.domain

data class User(
    val login: String,
    val id: Int,
    val avatar_url: String?,
    val html_url: String,
    val name: String?,
    val location: String?,
    val email: String?,
    val bio: String?,
    val twitter_username: String?,
    val public_repos: Int?,
    val public_gists: Int?,
    val followers: Int?,
    val following: Int?,
    val created_at: String?,
    val updated_at: String?,
)
