package com.bytecoders.coinscanner.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.bytecoders.coinscanner.MainActivity
import com.bytecoders.coinscanner.ui.currency.SEARCH_FIELD_TAG
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

    @Test
    fun changeOrder() {
        // Change order

        // Check that now Highest Market Cap is shown
        composeTestRule.onNodeWithText("Lowest Market Cap").assertDoesNotExist()
        composeTestRule.onNodeWithText("Highest Market Cap").assertIsDisplayed().performClick()

        // CLick on Lowest Market Cap from all options
        composeTestRule.onNodeWithText("Lowest Market Cap").assertIsDisplayed().performClick()

        // Check that now Lowest Market Cap is shown
        composeTestRule.onNodeWithText("Highest Market Cap").assertDoesNotExist()
        composeTestRule.onNodeWithText("Lowest Market Cap").assertIsDisplayed()
    }

    @Test
    fun changeCurrency() {
        // Change currency
        composeTestRule.onNodeWithText("US Dollar").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("Search currency").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag(SEARCH_FIELD_TAG).assertIsDisplayed().performTextInput("eur")
        composeTestRule.onNodeWithTag(SEARCH_FIELD_TAG).assert(hasText("eur"))

        composeTestRule.onNodeWithText("euro", substring = true, ignoreCase = true).assertIsDisplayed().performClick()

        // Check all Chips again, now eur should be used
        composeTestRule.onNodeWithText("Highest Market Cap").assertIsDisplayed()
        composeTestRule.onNodeWithText("Euro").assertIsDisplayed()
    }
}