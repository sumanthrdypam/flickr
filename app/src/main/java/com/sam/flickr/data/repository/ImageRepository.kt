package com.sam.flickr.data.repository

import android.util.Log
import com.sam.flickr.data.api.ApiService
import com.sam.flickr.data.mapper.ImageMapper
import com.sam.flickr.domain.data.ImageFetchState
import com.sam.flickr.domain.repository.IImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class ImageRepository @Inject constructor(private val apiService: ApiService) : IImageRepository {
    
    companion object {
        private const val TAG = "ImageRepository"
        private const val ERROR_GENERIC = "Something went wrong. Please try again"
        private const val ERROR_NO_INTERNET = "Please check your internet connection and try again"
        private fun getNoImagesError(query: String) = "No images found for '$query'"
    }
    
    override suspend fun getImageFetchStateFlow(queries: String): Flow<ImageFetchState> = flow {
        if (queries.isEmpty()) {
            Log.d(TAG, "Empty search query")
            emit(ImageFetchState.Idle)
            return@flow
        }
        
        emit(ImageFetchState.Loading)
        
        try {
            val response = apiService.getImages(tags = queries)
            val body = response.body()
            
            when {
                !response.isSuccessful -> {
                    Log.e(TAG, "API error: ${response.code()}")
                    emit(ImageFetchState.Error(ERROR_GENERIC))
                }
                body == null -> {
                    Log.e(TAG, "Empty response")
                    emit(ImageFetchState.Error(ERROR_GENERIC))
                }
                else -> {
                    // heavy mapping operation
                    val mappedImages = withContext(Dispatchers.Default) {
                        body.items.map { ImageMapper.mapToDomain(it) }
                    }
                    
                    if (mappedImages.isEmpty()) {
                        Log.w(TAG, "No images found")
                        emit(ImageFetchState.Error(getNoImagesError(queries)))
                    } else {
                        emit(ImageFetchState.Success(mappedImages))
                    }
                }
            }
        } catch (e: UnknownHostException) {
            Log.e(TAG, "No internet", e)
            emit(ImageFetchState.Error(ERROR_NO_INTERNET))
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching images", e)
            emit(ImageFetchState.Error(ERROR_GENERIC))
        }
    }.flowOn(Dispatchers.IO) // network operations on IO dispatcher
}