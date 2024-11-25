package com.sam.flickr.data.mapper

interface Mapper<E,D> {
    fun mapToDomain(type:E):D
}