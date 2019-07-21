package com.github.noman720.rxhloader.sample.model


import com.squareup.moshi.Json

data class ProfileImage(
    @Json(name = "large")
    val large: String,
    @Json(name = "medium")
    val medium: String,
    @Json(name = "small")
    val small: String
)