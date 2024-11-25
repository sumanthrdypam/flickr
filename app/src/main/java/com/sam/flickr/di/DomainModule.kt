package com.sam.flickr.di

import com.sam.flickr.domain.usecase.imagefetchusecase.GetImageFetchStateUseCase
import com.sam.flickr.domain.usecase.imagefetchusecase.IGetImageFetchStateUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    @Binds
    @Singleton
    abstract fun bindImageFetchStateUseCase(
        impl: GetImageFetchStateUseCase
    ): IGetImageFetchStateUseCase
}