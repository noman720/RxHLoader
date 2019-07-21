package com.github.noman720.rxhloader.sample.model


import com.squareup.moshi.Json

data class LinksX(
    @Json(name = "html")
    val html: String,
    @Json(name = "likes")
    val likes: String,
    @Json(name = "photos")
    val photos: String,
    @Json(name = "self")
    val self: String
)