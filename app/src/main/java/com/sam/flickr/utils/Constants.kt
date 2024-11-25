package com.sam.flickr.utils

object Constants {
    // Network
    const val BASE_URL = "https://api.flickr.com/services/feeds/"
    const val SEARCH_DEBOUNCE_TIME = 300L
    
    // Image Loading
    const val THUMBNAIL_SIZE = 200
    const val DETAIL_IMAGE_SIZE = 800
    const val THUMBNAIL_QUALITY = 80
    const val DETAIL_IMAGE_QUALITY = 95
    const val THUMBNAIL_SCALE = 0.5f
    
    // Grid
    const val GRID_COLUMNS = 2
    const val GRID_SPACING = 16
    const val GRID_BOTTOM_PADDING = 80
    
    // Loading
    const val LOADING_INDICATOR_SIZE = 36
    const val LOADING_INDICATOR_STROKE = 2
    
    // Text
    const val LETTER_SPACING = 0.15f
    const val LINE_HEIGHT = 24
    const val FONT_SIZE = 16
    const val MAX_LINES = 2
    
    // Layout
    const val TOP_GUIDELINE = 0.2f
    
    // Navigation
    const val DETAIL_SCREEN_ROUTE = "detailedScreen"
} 