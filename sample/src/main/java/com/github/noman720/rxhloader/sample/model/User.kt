package com.github.noman720.rxhloader.sample.model


import com.squareup.moshi.Json

data class User(
    @Json(name = "id")
    val id: String,
    @Json(name = "links")
    val links: LinksX,
    @Json(name = "name")
    val name: String,
    @Json(name = "profile_image")
    val profileImage: ProfileImage,
    @Json(name = "username")
    val username: String
)