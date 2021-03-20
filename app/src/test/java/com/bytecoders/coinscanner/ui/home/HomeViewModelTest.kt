package com.bytecoders.coinscanner.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bytecoders.coinscanner.service.coingecko.MarketItem
import com.bytecoders.coinscanner.util.BaseCoroutineTest
import com.bytecoders.coinscanner.util.getOrAwaitValue
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest: BaseCoroutineTest() {
    private val viewModel = HomeViewModel()

    @Test
    fun testLoad() {
        viewModel.load()
        val markets = viewModel.markets.getOrAwaitValue()

        assertNotNull(markets)
        assertEquals(ITEMS_PER_PAGE, markets.size)
    }
}