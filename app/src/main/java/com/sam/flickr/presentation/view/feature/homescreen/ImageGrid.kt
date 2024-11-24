package com.sam.flickr.presentation.view.feature.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sam.flickr.domain.data.Image
import com.sam.flickr.domain.data.ImageFetchState
import com.sam.flickr.presentation.viewmodel.ImageViewModel

private const val GRID_SPACING = 16
private const val GRID_COLUMNS = 2
private const val LOADING_INDICATOR_SIZE = 36
private const val GRID_BOTTOM_PADDING = 80

@Composable
fun ImageGrid(
    modifier: Modifier = Modifier,
    navController: NavController,
    imageViewModel: ImageViewModel
) {
    val imageResult = imageViewModel.imageFetchState.collectAsState()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when(val result = imageResult.value) {
            is ImageFetchState.Loading -> LoadingState()
            is ImageFetchState.Success -> {
                if (result.images.isEmpty()) {
                    EmptyState()
                } else {
                    ImagesGrid(
                        images = result.images,
                        onImageClick = { image ->
                            imageViewModel.selectImage(image)
                            navController.navigate("detailedScreen")
                        }
                    )
                }
            }
            is ImageFetchState.Error -> ErrorState(message = result.message)
        }
    }
}

@Composable
private fun LoadingState() {
    CircularProgressIndicator(
        color = Color(0xFF666666),
        modifier = Modifier.size(LOADING_INDICATOR_SIZE.dp)
    )
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No images found",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF666666)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Try searching for something else",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF666666)
        )
    }
}

@Composable
private fun ErrorState(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Oops! Something went wrong",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF666666),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ImagesGrid(
    images: List<Image>,
    onImageClick: (Image) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(GRID_COLUMNS),
        contentPadding = PaddingValues(
            start = GRID_SPACING.dp,
            end = GRID_SPACING.dp,
            top = 4.dp,
            bottom = GRID_BOTTOM_PADDING.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(GRID_SPACING.dp),
        verticalArrangement = Arrangement.spacedBy(GRID_SPACING.dp),
        state = rememberLazyGridState(),
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        items(
            items = images,
            key = { it.link }
        ) { image ->
            ImageCard(
                image = image,
                onImageClick = { onImageClick(image) }
            )
        }
    }
}
