package com.github.noman720.rxhloader.sample.model


import com.squareup.moshi.Json

data class Links(
    @Json(name = "photos")
    val photos: String,
    @Json(name = "self")
    val self: String
)