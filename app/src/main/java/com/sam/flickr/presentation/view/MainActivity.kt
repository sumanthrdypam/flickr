package com.sam.flickr.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.sam.flickr.presentation.view.navigation.AppNavHost
import com.sam.flickr.presentation.view.theme.FlickrTheme
import com.sam.flickr.presentation.viewmodel.ImageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val imageViewModel by viewModels<ImageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlickrTheme {
                val navController = rememberNavController()
                AppNavHost(navController,imageViewModel)
            }
        }
    }
}


