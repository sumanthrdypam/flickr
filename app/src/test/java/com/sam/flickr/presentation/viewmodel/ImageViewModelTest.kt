package com.sam.flickr.presentation.viewmodel

import com.sam.flickr.domain.data.Image
import com.sam.flickr.domain.data.ImageFetchState
import com.sam.flickr.domain.usecase.imagefetchusecase.IGetImageFetchStateUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ImageViewModelTest {
    private lateinit var viewModel: ImageViewModel
    private val imageFetchStateUseCase: IGetImageFetchStateUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        // Given
        coEvery { imageFetchStateUseCase("car") } returns ImageFetchState.Success(emptyList())
        
        // When
        viewModel = ImageViewModel(imageFetchStateUseCase)
        
        // Then
        assertEquals(ImageFetchState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `when search is successful, state is Success`() = runTest {
        // Given
        val mockImages = listOf(
            Image("link1", "title1", "desc1", "author1", "date1")
        )
        coEvery { imageFetchStateUseCase("test") } returns ImageFetchState.Success(mockImages)

        // When
        viewModel = ImageViewModel(imageFetchStateUseCase)
        viewModel.updateSearchQuery("test")
        
        testDispatcher.scheduler.advanceTimeBy(500) // Account for debounce
        
        // Then
        assertEquals(ImageFetchState.Success(mockImages), viewModel.uiState.value)
    }

    @Test
    fun `when search fails, state is Error`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { imageFetchStateUseCase("test") } throws RuntimeException(errorMessage)

        // When
        viewModel = ImageViewModel(imageFetchStateUseCase)
        viewModel.updateSearchQuery("test")
        
        testDispatcher.scheduler.advanceTimeBy(500)
        
        // Then
        assert(viewModel.uiState.value is ImageFetchState.Error)
    }

    @Test
    fun `selectImage updates selectedImage state`() = runTest {
        // Given
        coEvery { imageFetchStateUseCase("car") } returns ImageFetchState.Success(emptyList())
        val testImage = Image("link1", "title1", "desc1", "author1", "date1")
        
        // When
        viewModel = ImageViewModel(imageFetchStateUseCase)
        viewModel.updateSelectedImage(testImage)
        
        // Then
        assertEquals(testImage, viewModel.selectedImage.value)
    }
} 