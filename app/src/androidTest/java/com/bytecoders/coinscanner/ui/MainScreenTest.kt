package com.bytecoders.coinscanner.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.bytecoders.coinscanner.MainActivity
import org.junit.Rule
import org.junit.Test

class MainScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun mainScreenTabs() {
        // Check all tabs
        composeTestRule.onNodeWithText("Markets").assertIsDisplayed()
        composeTestRule.onNodeWithText("Portfolio").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("This is your portfolio").assertIsDisplayed()
        composeTestRule.onNodeWithText("More").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("This is more stuff").assertIsDisplayed()
    }

    @Test
    fun coinListChips() {
        // Check all Chips
        composeTestRule.onNodeWithText("Highest Market Cap").assertIsDisplayed()
        composeTestRule.onNodeWithText("US Dollar").assertIsDisplayed()
    }
}