package com.sam.flickr

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.CustomTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun searchBar_isDisplayed() {
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithText("Search Flickr images...")
            .assertIsDisplayed()
    }

    @Test
    fun enterSearchQuery_updatesSearchBar() {
        composeTestRule.waitForIdle()
        val searchText = "nature"
        
        composeTestRule
            .onNodeWithText("Search Flickr images...")
            .performTextInput(searchText)

        composeTestRule
            .onNodeWithText(searchText)
            .assertIsDisplayed()
    }
} 