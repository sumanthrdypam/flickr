package com.sam.flickr.presentation.view.feature.homescreen

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sam.flickr.R
import com.sam.flickr.domain.data.Image
import com.sam.flickr.presentation.theme.GradientEnd
import com.sam.flickr.presentation.theme.GradientStart
import com.sam.flickr.presentation.theme.TextSecondary
import com.sam.flickr.presentation.theme.White

@Composable
fun ImageCard(
    image: Image,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var bitmap by remember(image.link) { mutableStateOf<Bitmap?>(null) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onImageClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            DisposableEffect(image.link) {
                val requestManager = Glide.with(context)
                val target = object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        bitmap = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        bitmap = null
                    }
                }

                requestManager
                    .asBitmap()
                    .load(image.link)
                    .override(Constants.THUMBNAIL_SIZE, Constants.THUMBNAIL_SIZE)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .encodeQuality(Constants.THUMBNAIL_QUALITY)
                    .priority(Priority.HIGH)
                    .thumbnail(
                        Glide.with(context)
                            .asBitmap()
                            .load(image.link)
                            .override(Constants.THUMBNAIL_SIZE / 2)
                    )
                    .skipMemoryCache(false)
                    .into(target)

                onDispose {
                    bitmap = null
                    requestManager.clear(target)
                }
            }

            bitmap?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = image.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    filterQuality = FilterQuality.High
                )
            } ?: Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size)),
                    color = TextSecondary,
                    strokeWidth = dimensionResource(R.dimen.loading_indicator_stroke)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                GradientStart,
                                GradientEnd
                            ),
                            startY = 0f,
                            endY = 100f
                        )
                    )
                    .align(Alignment.BottomCenter)
                    .padding(dimensionResource(R.dimen.spacing_medium))
            ) {
                Text(
                    text = image.title,
                    color = White,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}