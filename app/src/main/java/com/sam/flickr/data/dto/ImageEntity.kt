package com.sam.flickr.data.dto

data class ImageEntity(
    val description: String,
    val generator: String,
    val items: List<Item>,
    val link: String,
    val modified: String,
    val title: String
)