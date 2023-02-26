package com.example.githubusers.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val login: String,
    val id: Int,
    @Json(name = "avatar_url") val avatarUrl: String?,
    @Json(name = "html_url") val htmlUrl: String,
    val name: String?,
    val location: String?,
    val email: String?,
    val bio: String?,
    @Json(name = "twitter_username") val twitterUsername: String?,
    @Json(name = "public_repos") val publicRepos: Int?,
    @Json(name = "public_gists") val publicGists: Int?,
    val followers: Int?,
    val following: Int?,
    @Json(name = "created_at") val createdAt: String?,
    @Json(name = "updated_at") val updatedAt: String?,
)

fun User.toUserItem(): UserItem {
    return UserItem(
        login,
        id,
        avatarUrl
    )
}