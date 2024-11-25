package com.sam.flickr.data.dto

import com.google.gson.annotations.SerializedName

data class Media(
    @SerializedName("m")
    val imageUrl: String
)