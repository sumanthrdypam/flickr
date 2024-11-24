package com.sam.flickr.presentation.view.feature.detailscreen

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sam.flickr.presentation.viewmodel.ImageViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import com.sam.flickr.presentation.view.util.TextUtils
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.FilterQuality



@Composable
fun DetailedScreen(imageViewModel: ImageViewModel) {
    val selectedImage = imageViewModel.selectedImage.collectAsState()
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val scrollState = rememberScrollState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            DisposableEffect(selectedImage.value.link) {
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
                    .load(selectedImage.value.link)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .encodeQuality(95)
                    .override(800, 800)
                    .skipMemoryCache(false)
                    .into(target)

                onDispose {
                    bitmap = null
                    requestManager.clear(target)
                }
            }

            if (selectedImage.value.link.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        bitmap?.let { btm ->
                            Image(
                                bitmap = btm.asImageBitmap(),
                                contentDescription = selectedImage.value.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                filterQuality = FilterQuality.High
                            )
                        } ?: Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp),
                                color = Color(0xFF666666),
                                strokeWidth = 4.dp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Add Share Button after the image and before details
                Button(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, selectedImage.value.title)
                            val shareText = buildString {
                                append("Check out this image!\n\n")
                                append("Title: ${selectedImage.value.title}\n")
                                append("By: ${selectedImage.value.author}\n")
                                append("Taken on: ${TextUtils.formatDate(selectedImage.value.dataTaken)}\n")
                                append("Link: ${selectedImage.value.link}\n")
                                
                                val (width, height) = TextUtils.parseImageDimensions(selectedImage.value.description)
                                append("Dimensions: $width × $height pixels\n")
                            }
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF666666)
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                        Text(
                            text = "SHARE",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))

                // Image Details
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Title section
                    Column {
                        Text(
                            text = "TITLE",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF666666),  // Silver/gray color for labels
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = selectedImage.value.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Description section
                    Column {
                        Text(
                            text = "DESCRIPTION",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF666666),
                            fontWeight = FontWeight.Bold
                        )
                        val parsedText = TextUtils.parseHtmlToAnnotatedString(selectedImage.value.description)
                        Text(
                            text = parsedText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black,
                            lineHeight = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Author section
                    Column {
                        Text(
                            text = "AUTHOR",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF666666),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = selectedImage.value.author,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                    
                    // Add Dimensions section after Author section
                    Column {
                        Text(
                            text = "DIMENSIONS",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF666666),
                            fontWeight = FontWeight.Bold
                        )
                        val (width, height) = TextUtils.parseImageDimensions(selectedImage.value.description)
                        Text(
                            text = "$width × $height pixels",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                    
                    // Date section
                    Column {
                        Text(
                            text = "PUBLISHED DATE",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF666666),
                            fontWeight = FontWeight.Bold
                        )
                        val formattedDate = remember(selectedImage.value.dataTaken) {
                            TextUtils.formatDate(selectedImage.value.dataTaken)
                        }
                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No image selected",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

