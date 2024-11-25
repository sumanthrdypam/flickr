package com.sam.flickr.domain.repository

import com.sam.flickr.domain.data.ImageFetchState
import kotlinx.coroutines.flow.Flow

interface IImageRepository {
    suspend fun getImageFetchStateFlow(queries: String): Flow<ImageFetchState>
}