package com.sam.flickr.data.repository

import com.sam.flickr.data.api.ApiService
import com.sam.flickr.data.mapper.ImageMapper
import com.sam.flickr.domain.data.ImageFetchState
import com.sam.flickr.domain.repository.IImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val apiService: ApiService
) : IImageRepository {
    
    override suspend fun getImageFetchStateFlow(queries: String): Flow<ImageFetchState> {
        return flow {
            try {
                val response = apiService.getImages(tags = queries)
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        emit(ImageFetchState.Success(body.items.map { ImageMapper.mapToDomain(it) }))
                    } ?: emit(ImageFetchState.Error("Empty response body"))
                } else {
                    emit(ImageFetchState.Error("API Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                emit(ImageFetchState.Error(e.message ?: "Unknown error occurred"))
            }
        }
    }
}