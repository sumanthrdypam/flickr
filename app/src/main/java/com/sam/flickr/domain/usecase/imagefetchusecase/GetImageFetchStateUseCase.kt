package com.sam.flickr.domain.usecase.imagefetchusecase

import com.sam.flickr.domain.repository.IImageRepository
import javax.inject.Inject

class GetImageFetchStateUseCase @Inject constructor(private val imageRepository:IImageRepository) : IGetImageFetchStateUseCase {
     override suspend fun invoke(query: String) = imageRepository.getImageFetchStateFlow(query)
}