package com.bytecoders.coinscanner.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.bytecoders.coinscanner.data.DataUtil
import com.bytecoders.coinscanner.repository.CoinGeckoRepository
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.util.coroutineTest
import com.bytecoders.coinscanner.util.getOrAwaitValue
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    lateinit var savedStateHandle: SavedStateHandle

    @MockK
    lateinit var coinGeckoRepository: CoinGeckoRepository

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        every { savedStateHandle.get<Int>(any()) } returns 1
        every { coinGeckoRepository.getMarkets(any(), any(), any(), any()) } returns flow {
            emit(DataUtil.parseJson<List<MarketItem>>("markets.json").take(ITEMS_PER_PAGE))
        }
        viewModel = HomeViewModel(savedStateHandle, coinGeckoRepository)
    }

    @Test
    fun testLoad() = coroutineTest {
        viewModel.load()
        val markets = viewModel.markets.getOrAwaitValue()

        assertNotNull(markets)
        assertEquals(ITEMS_PER_PAGE, markets.size)
    }
}