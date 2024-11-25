package com.sam.flickr.presentation.navigation

sealed class NavigationRoutes(val route: String) {
    object Home : NavigationRoutes("homeScreen")
    object Detail : NavigationRoutes("detailScreen")
} 