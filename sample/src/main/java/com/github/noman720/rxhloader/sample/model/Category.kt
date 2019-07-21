package com.github.noman720.rxhloader.sample.model


import com.squareup.moshi.Json

data class Category(
    @Json(name = "id")
    val id: Int,
    @Json(name = "links")
    val links: Links,
    @Json(name = "photo_count")
    val photoCount: Int,
    @Json(name = "title")
    val title: String
)