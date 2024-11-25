package com.sam.flickr.data.mapper

import com.sam.flickr.data.dto.Item
import com.sam.flickr.domain.data.Image

object ImageMapper : Mapper<Item,Image> {
    override fun mapToDomain(type: Item): Image {
        return Image(link = type.media.imageUrl, title = type.title, description = type.description, author = type.author, dataTaken = type.dateTaken )
    }
}