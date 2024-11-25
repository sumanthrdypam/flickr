package com.sam.flickr.di

import com.sam.flickr.data.repository.ImageRepository
import com.sam.flickr.domain.repository.IImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Singleton
    @Binds
    abstract fun bindImageRepository(impl: ImageRepository): IImageRepository
} 