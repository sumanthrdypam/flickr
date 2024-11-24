package com.sam.flickr.domain.data

sealed class ImageFetchState {
    object Idle : ImageFetchState()
    object Loading : ImageFetchState()
    data class Success(val images: List<Image>) : ImageFetchState()
    data class Error(val message: String) : ImageFetchState()
}