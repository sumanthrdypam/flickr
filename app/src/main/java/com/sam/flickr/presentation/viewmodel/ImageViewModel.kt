package com.sam.flickr.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.flickr.domain.data.Image
import com.sam.flickr.domain.data.ImageFetchState
import com.sam.flickr.domain.usecase.imagefetchusecase.IGetImageFetchStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ImageViewModel @Inject constructor(
    private val imageFetchStateUseCase: IGetImageFetchStateUseCase
) : ViewModel() {
    private val _imageFetchState = MutableStateFlow<ImageFetchState>(ImageFetchState.Loading)
    val imageFetchState: StateFlow<ImageFetchState> = _imageFetchState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedImage = MutableStateFlow<Image>(Image("","No Image Selected","","",""))
    val selectedImage : StateFlow<Image> = _selectedImage
    init {
        viewModelScope.launch {
            fetchImages("car")
        }

        viewModelScope.launch {
            _query
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotEmpty()) {
                        fetchImages(query)
                    }
                }
        }
    }

    fun query(newQuery: String) {
        viewModelScope.launch {
            _query.emit(newQuery)
        }
    }

    private suspend fun fetchImages(query: String) {
        _imageFetchState.emit(ImageFetchState.Loading)
        try {
            _imageFetchState.emit(imageFetchStateUseCase(query))
        } catch (e: Exception) {
            _imageFetchState.emit(ImageFetchState.Error(e.message ?: "Unknown error"))
        }
    }

    fun selectImage(image:Image){
        _selectedImage.value = image
    }
}