package com.sam.flickr.di

import com.sam.flickr.domain.repository.IImageRepository
import com.sam.flickr.domain.usecase.imagefetchusecase.GetImageFetchStateUseCase
import com.sam.flickr.domain.usecase.imagefetchusecase.IGetImageFetchStateUseCase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    @Singleton
    fun getImageFetchStateUseCase(iImageRepository: IImageRepository): IGetImageFetchStateUseCase{
        return GetImageFetchStateUseCase(iImageRepository)
    }

}