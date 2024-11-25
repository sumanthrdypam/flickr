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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sam.flickr.R
import com.sam.flickr.domain.data.Image
import com.sam.flickr.domain.data.ImageFetchState
import com.sam.flickr.presentation.theme.TextSecondary
import com.sam.flickr.presentation.viewmodel.ImageViewModel

@Composable
fun ImageGrid(
    modifier: Modifier = Modifier,
    navController: NavController,
    imageViewModel: ImageViewModel
) {
    val imageResult = imageViewModel.uiState.collectAsStateWithLifecycle()
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when(val result = imageResult.value) {
            is ImageFetchState.Idle -> IdleState()
            is ImageFetchState.Loading -> LoadingState()
            is ImageFetchState.Success -> {
                ImagesGrid(
                    images = result.images,
                    onImageClick = { image ->
                        imageViewModel.updateSelectedImage(image)
                        navController.navigate("detailedScreen")
                    }
                )
            }
            is ImageFetchState.Error -> ErrorState(message = result.message)
        }
    }
}

@Composable
private fun LoadingState() {
    CircularProgressIndicator(
        color = TextSecondary,
        modifier = Modifier.size(dimensionResource(R.dimen.loading_indicator_size)),
        strokeWidth = dimensionResource(R.dimen.loading_indicator_stroke)
    )
}

@Composable
private fun ErrorState(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.spacing_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.error_title),
            style = MaterialTheme.typography.titleMedium,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_small))
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
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
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            start = dimensionResource(R.dimen.grid_spacing),
            end = dimensionResource(R.dimen.grid_spacing),
            top = dimensionResource(R.dimen.spacing_small),
            bottom = dimensionResource(R.dimen.grid_bottom_padding)
        ),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.grid_spacing)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.grid_spacing)),
        state = rememberLazyGridState(),
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = dimensionResource(R.dimen.spacing_medium))
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

@Composable
private fun IdleState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.spacing_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.idle_title),
            style = MaterialTheme.typography.titleMedium,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
        Text(
            text = stringResource(R.string.idle_message),
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}
