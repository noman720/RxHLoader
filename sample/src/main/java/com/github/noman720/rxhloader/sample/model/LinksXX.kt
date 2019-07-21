package com.github.noman720.rxhloader.sample.model


import com.squareup.moshi.Json

data class LinksXX(
    @Json(name = "download")
    val download: String,
    @Json(name = "html")
    val html: String,
    @Json(name = "self")
    val self: String
)