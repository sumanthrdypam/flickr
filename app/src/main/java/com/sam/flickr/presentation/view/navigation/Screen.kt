package com.sam.flickr.presentation.view.navigation


sealed class Screen(val route:String) {
    data object  Home : Screen("home")
    data object DetailedScreen : Screen("detailedScreen")
}