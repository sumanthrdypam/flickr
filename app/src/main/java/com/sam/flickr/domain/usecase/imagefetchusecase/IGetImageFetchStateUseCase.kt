package com.sam.flickr.domain.usecase.imagefetchusecase

import com.sam.flickr.domain.data.ImageFetchState

interface IGetImageFetchStateUseCase {
    suspend operator fun invoke(query: String) : ImageFetchState
}