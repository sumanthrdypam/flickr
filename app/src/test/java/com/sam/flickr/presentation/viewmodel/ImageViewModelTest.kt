package com.sam.flickr.presentation.viewmodel

import app.cash.turbine.test
import com.sam.flickr.domain.data.Image
import com.sam.flickr.domain.data.ImageFetchState
import com.sam.flickr.domain.usecase.imagefetchusecase.IGetImageFetchStateUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ImageViewModelTest {
    private lateinit var viewModel: ImageViewModel
    private lateinit var imageFetchStateUseCase: IGetImageFetchStateUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        imageFetchStateUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Idle`() = runTest {
        // Given
        coEvery { imageFetchStateUseCase(any()) } returns flowOf(ImageFetchState.Idle)
        
        // When
        viewModel = ImageViewModel(imageFetchStateUseCase)
        
        // Then
        assertEquals(ImageFetchState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `search query triggers successful state change`() = runTest {
        // Given
        val mockImages = listOf(
            Image(
                link = "https://test.com",
                title = "Test Image",
                description = "Test Description",
                author = "Test Author",
                dataTaken = "2024-01-01"
            )
        )
        
        coEvery { imageFetchStateUseCase("") } returns flowOf(ImageFetchState.Idle)
        coEvery { imageFetchStateUseCase("test") } returns flowOf(
            ImageFetchState.Loading,
            ImageFetchState.Success(mockImages)
        )
        
        viewModel = ImageViewModel(imageFetchStateUseCase)

        // When & Then
        viewModel.uiState.test {
            assertEquals(ImageFetchState.Idle, awaitItem())
            viewModel.updateSearchQuery("test")
            assertEquals(ImageFetchState.Loading, awaitItem())
            advanceTimeBy(300)
            assertEquals(ImageFetchState.Success(mockImages), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `selected image updates correctly`() = runTest {
        // Given
        val testImage = Image(
            link = "https://test.com",
            title = "Test Image",
            description = "Test Description",
            author = "Test Author",
            dataTaken = "2024-01-01"
        )
        viewModel = ImageViewModel(imageFetchStateUseCase)

        // When
        viewModel.updateSelectedImage(testImage)

        // Then
        assertEquals(testImage, viewModel.selectedImage.value)
    }
} 