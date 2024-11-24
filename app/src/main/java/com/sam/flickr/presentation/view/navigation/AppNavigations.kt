package com.sam.flickr.presentation.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sam.flickr.presentation.view.feature.detailscreen.DetailedScreen
import com.sam.flickr.presentation.view.feature.homescreen.HomeScreen
import com.sam.flickr.presentation.viewmodel.ImageViewModel


@Composable
fun AppNavHost(navController: NavHostController, imageViewModel: ImageViewModel) {

        NavHost(navController=navController, startDestination = Screen.Home.route){
            composable(Screen.Home.route){
                HomeScreen(navController,imageViewModel)
            }

            composable(Screen.DetailedScreen.route){
                DetailedScreen(imageViewModel)
            }
        }

}