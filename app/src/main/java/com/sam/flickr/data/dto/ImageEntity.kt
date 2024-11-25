package com.sam.flickr.data.dto

import com.google.gson.annotations.SerializedName

data class ImageEntity(
    @SerializedName("description")
    val description: String,
    @SerializedName("generator")
    val generator: String,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("link")
    val link: String,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("title")
    val title: String
)