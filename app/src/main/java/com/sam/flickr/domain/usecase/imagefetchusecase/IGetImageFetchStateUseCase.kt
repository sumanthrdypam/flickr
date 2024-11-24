package com.sam.flickr.domain.usecase.imagefetchusecase

import com.sam.flickr.domain.data.ImageFetchState
import kotlinx.coroutines.flow.Flow

interface IGetImageFetchStateUseCase {
    suspend operator fun invoke(query: String) : Flow<ImageFetchState>
}