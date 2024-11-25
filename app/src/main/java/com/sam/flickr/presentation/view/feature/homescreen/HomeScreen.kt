package com.sam.flickr.presentation.view.feature.homescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.sam.flickr.presentation.viewmodel.ImageViewModel
import androidx.compose.ui.platform.testTag

@Composable
fun HomeScreen(
    navController: NavController,
    imageViewModel: ImageViewModel,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (searchBar, imagesGrid) = createRefs()
        val guideLineFromTop = createGuidelineFromTop(0.2f)

        Box(
            modifier = Modifier
                .constrainAs(searchBar) {
                    top.linkTo(parent.top)
                    bottom.linkTo(guideLineFromTop)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .testTag("searchBar")
        ) {
            SearchBar(imageViewModel)
        }

        ImageGrid(
            modifier = Modifier.constrainAs(imagesGrid) {
                top.linkTo(guideLineFromTop)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .testTag("imageGrid"),
            navController = navController,
            imageViewModel = imageViewModel
        )
    }
}

