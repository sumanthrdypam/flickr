package com.sam.flickr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.flickr.domain.data.Image
import com.sam.flickr.domain.data.ImageFetchState
import com.sam.flickr.domain.usecase.imagefetchusecase.IGetImageFetchStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
            imageFetchStateUseCase(query).collect { state ->
                _imageFetchState.emit(state)
            }
        } catch (e: Exception) {
            _imageFetchState.emit(ImageFetchState.Error(e.message ?: "Unknown error"))
        }
    }

    fun selectImage(image: Image) {
        _selectedImage.value = image
    }
}