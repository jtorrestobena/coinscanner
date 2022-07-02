package com.bytecoders.coinscanner.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bytecoders.coinscanner.util.getOrAwaitValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val viewModel = HomeViewModel()

    @Test
    fun testLoad() {
        viewModel.load()
        val markets = viewModel.markets.getOrAwaitValue()

        assertNotNull(markets)
        assertEquals(ITEMS_PER_PAGE, markets.size)
    }
}