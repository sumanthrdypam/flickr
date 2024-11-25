package com.sam.flickr.presentation.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltAndroidRule

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
    fun searchBarIsDisplayed() {
        composeTestRule.onNode(hasSetTextAction()).assertExists()
    }

    @Test
    fun enterSearchQueryShowsResults() {
        composeTestRule.onNode(hasSetTextAction())
            .assertExists()
            .assertIsEnabled()
            .performTextInput("nature")

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag("imageGrid")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("imageGrid")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun enterRandomSearchQueryShowsEmptyState() {
        composeTestRule.onNode(hasSetTextAction())
            .assertExists()
            .assertIsEnabled()
            .performTextInput("@+-//xj238sjd8f7sd89f7ds89f7")//random test

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Start Searching")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Enter keywords to find images")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onAllNodesWithTag("imageCard")
            .assertCountEquals(0)
    }
} 