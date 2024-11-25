package com.sam.flickr.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.flickr.domain.data.Image
import com.sam.flickr.domain.data.ImageFetchState
import com.sam.flickr.domain.usecase.imagefetchusecase.IGetImageFetchStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class ImageViewModel @Inject constructor(
    private val imageFetchStateUseCase: IGetImageFetchStateUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<ImageFetchState>(ImageFetchState.Idle)
    val uiState: StateFlow<ImageFetchState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedImage = MutableStateFlow<Image>(Image("","No Image Selected","","",""))
    val selectedImage: StateFlow<Image> = _selectedImage.asStateFlow()

    init {
        viewModelScope.launch{
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    imageFetchStateUseCase(query)
                }
                .catch { error ->
                    emit(ImageFetchState.Error(error.message ?: "Unknown error"))
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    fun updateSearchQuery(newQuery: String) {
        viewModelScope.launch {
            _searchQuery.emit(newQuery)
        }
    }

    fun updateSelectedImage(image: Image) {
        _selectedImage.value = image
    }

    override fun onCleared() {
        super.onCleared()
        _uiState.value = ImageFetchState.Idle
        _searchQuery.value = ""
        _selectedImage.value = Image("", "No Image Selected", "", "", "")
        
        Log.d("ImageViewModel", "ViewModel cleared: States reset to initial values")
    }
}