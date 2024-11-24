package com.sam.flickr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.flickr.domain.data.Image
import com.sam.flickr.domain.data.ImageFetchState
import com.sam.flickr.domain.usecase.imagefetchusecase.IGetImageFetchStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val imageFetchStateUseCase: IGetImageFetchStateUseCase
) : ViewModel() {
    private val _imageFetchState = MutableStateFlow<ImageFetchState>(ImageFetchState.Idle)
    val imageFetchState: StateFlow<ImageFetchState> = _imageFetchState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedImage = MutableStateFlow<Image>(Image("","No Image Selected","","",""))
    val selectedImage: StateFlow<Image> = _selectedImage
    
    init {
        viewModelScope.launch(Dispatchers.Default) {
            _query
                .debounce(300)
                .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .flatMapLatest { query ->
                    imageFetchStateUseCase(query)
                        .flowOn(Dispatchers.IO)
                }
                .catch { error -> 
                    emit(ImageFetchState.Error(error.message ?: "Unknown error"))
                }
                .flowOn(Dispatchers.Default)
                .collect { state ->
                    _imageFetchState.value = state
                }
        }
    }

    fun query(newQuery: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _query.emit(newQuery)
        }
    }

    fun selectImage(image: Image) {
        viewModelScope.launch(Dispatchers.Default) {
            _selectedImage.value = image
        }
    }
}