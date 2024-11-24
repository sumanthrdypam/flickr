package com.sam.flickr.data.repository

import com.sam.flickr.data.api.ApiService
import com.sam.flickr.data.mapper.ImageMapper
import com.sam.flickr.domain.data.ImageFetchState
import com.sam.flickr.domain.repository.IImageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val apiService: ApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IImageRepository {
    override suspend fun getImageFetchStateFlow(queries: String): ImageFetchState {
        try {
            val response = apiService.getImages(tags = queries)

            if (response.isSuccessful) {
                response.body()?.let { body ->
                    return  ImageFetchState.Success(body.items.map { ImageMapper.mapToDomain(it) })
                } ?: run {
                    return ImageFetchState.Error("Error")
                }
            } else {
                return ImageFetchState.Error("Error")
            }
        } catch (e: Exception) {
           return ImageFetchState.Error(e.toString())
        }
    }

}