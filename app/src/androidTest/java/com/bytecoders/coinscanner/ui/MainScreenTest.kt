package com.bytecoders.coinscanner.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.bytecoders.coinscanner.MainActivity
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import com.bytecoders.coinscanner.ui.currency.SEARCH_FIELD_TAG
import com.bytecoders.coinscanner.ui.home.COIN_ITEM
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

        val orderTypes = GeckoOrder.values()

        for (i in 0..orderTypes.size - 2){
            switchOrder(composeTestRule.activity.getString(orderTypes[i].description),
                composeTestRule.activity.getString(orderTypes[i + 1].description))
            waitCoinsLoaded()
        }
    }

    private fun switchOrder(fromOrder: String, toOrder: String) {
        // Check that now fromOrder is shown
        composeTestRule.onNodeWithText(toOrder).assertDoesNotExist()
        composeTestRule.onNodeWithText(fromOrder).assertIsDisplayed().performClick()

        // CLick on Lowest toOrder from all options
        composeTestRule.onNodeWithText(toOrder).assertIsDisplayed().performClick()

        // Check that now toOrder is shown
        composeTestRule.onNodeWithText(fromOrder).assertDoesNotExist()
        composeTestRule.onNodeWithText(toOrder).assertIsDisplayed()
    }

    @Test
    fun changeCurrency() {
        // Change currency
        composeTestRule.onNodeWithText("US Dollar").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("Search currency").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag(SEARCH_FIELD_TAG).assertIsDisplayed().performTextInput("eur")
        composeTestRule.onNodeWithTag(SEARCH_FIELD_TAG).assert(hasText("eur"))

        composeTestRule.onNodeWithText("euro", substring = true, ignoreCase = true).assertIsDisplayed().performClick()

        waitCoinsLoaded()
        // Check all Chips again, now eur should be used
        composeTestRule.onNodeWithText("Highest Market Cap").assertIsDisplayed()
        composeTestRule.onNodeWithText("Euro").assertIsDisplayed().performClick()

        composeTestRule.onNodeWithText("AFN", substring = true, ignoreCase = true).assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("afghan", substring = true, ignoreCase = true).assertIsDisplayed()
    }

    private fun waitCoinsLoaded() {
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            composeTestRule
                .onAllNodesWithTag(COIN_ITEM)
                .fetchSemanticsNodes().isNotEmpty()
        }
    }
}