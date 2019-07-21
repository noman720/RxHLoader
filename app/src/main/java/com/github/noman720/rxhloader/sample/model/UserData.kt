package com.github.noman720.rxhloader.sample.model


import com.squareup.moshi.Json

data class UserData(
    @Json(name = "categories")
    val categories: List<Category>,
    @Json(name = "color")
    val color: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "current_user_collections")
    val currentUserCollections: List<Any>,
    @Json(name = "height")
    val height: Int,
    @Json(name = "id")
    val id: String,
    @Json(name = "liked_by_user")
    val likedByUser: Boolean,
    @Json(name = "likes")
    val likes: Int,
    @Json(name = "links")
    val links: Links,
    @Json(name = "urls")
    val urls: Urls,
    @Json(name = "user")
    val user: User,
    @Json(name = "width")
    val width: Int
)