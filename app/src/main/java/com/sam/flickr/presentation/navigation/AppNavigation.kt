package com.sam.flickr.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sam.flickr.presentation.view.feature.detailscreen.DetailedScreen
import com.sam.flickr.presentation.view.feature.homescreen.HomeScreen
import com.sam.flickr.presentation.viewmodel.ImageViewModel


@Composable
fun AppNavigation(
    navController: NavHostController,
    imageViewModel: ImageViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Home.route
    ) {
        composable(
            route = NavigationRoutes.Home.route,

        ) {
            HomeScreen(
                navController = navController,
                imageViewModel = imageViewModel
            )
        }

        composable(
            route = NavigationRoutes.Detail.route,

        ) {
            DetailedScreen(imageViewModel = imageViewModel)
        }
    }
} 