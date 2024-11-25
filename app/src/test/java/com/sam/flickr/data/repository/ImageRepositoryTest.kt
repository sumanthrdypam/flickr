package com.sam.flickr.data.repository

import android.util.Log
import app.cash.turbine.test
import com.sam.flickr.data.api.ApiService
import com.sam.flickr.data.dto.ImageEntity
import com.sam.flickr.data.dto.Item
import com.sam.flickr.data.dto.Media
import com.sam.flickr.domain.data.ImageFetchState
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertEquals
import retrofit2.Response
import okhttp3.ResponseBody.Companion.toResponseBody

@OptIn(ExperimentalCoroutinesApi::class)
class ImageRepositoryTest {
    private lateinit var repository: ImageRepository
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        // Mock Android Log
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0


        apiService = mockk()
        repository = ImageRepository(apiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `empty query returns idle state`() = runTest {
        repository.getImageFetchStateFlow("").test {
            assertEquals(ImageFetchState.Idle, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `successful api call returns success state`() = runTest {
        // Given
        val mockItem = Item(
            author = "author",
            authorId = "1",
            dateTaken = "2024",
            description = "desc",
            link = "link",
            media = Media("url"),
            published = "2024",
            tags = "test",
            title = "title"
        )
        val response = ImageEntity("", "", listOf(mockItem), "", "", "")
        coEvery { apiService.getImages(format = "json", noJsonCallback = 1, tags = "test") } returns Response.success(response)

        // When & Then
        repository.getImageFetchStateFlow("test").test {
            assertEquals(ImageFetchState.Loading, awaitItem())
            val success = awaitItem() as ImageFetchState.Success
            assertEquals("title", success.images.first().title)
            awaitComplete()
        }
    }

    @Test
    fun `api error returns error state`() = runTest {
        // Given
        val responseBody = "".toResponseBody()
        coEvery { 
            apiService.getImages(format = "json", noJsonCallback = 1, tags = "test") 
        } returns Response.error(404, responseBody)

        // When & Then
        repository.getImageFetchStateFlow("test").test {
            assertEquals(ImageFetchState.Loading, awaitItem())
            val error = awaitItem() as ImageFetchState.Error
            assertEquals("Something went wrong. Please try again", error.message)
            awaitComplete()
        }
    }
} 