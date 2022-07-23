package com.bytecoders.coinscanner.data.coingecko

import androidx.paging.*
import androidx.room.withTransaction
import com.bytecoders.coinscanner.data.DataUtil
import com.bytecoders.coinscanner.data.database.AppDatabase
import com.bytecoders.coinscanner.data.database.MarketItemsDao
import com.bytecoders.coinscanner.repository.CoinMarketConfiguration
import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.service.currency.CurrencyService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertContentEquals


private const val TEST_ITEMS_PER_PAGE = 5

internal class MarketsSourceTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var coinGeckoService: CoinGeckoService

    @MockK
    private lateinit var currencyService: CurrencyService

    @MockK
    private lateinit var appDatabase: AppDatabase

    @RelaxedMockK
    private lateinit var marketItemsDao: MarketItemsDao

    private val coinMarketConfiguration = CoinMarketConfiguration()


    private lateinit var marketsSource: MarketsSource

    private val mockResponse = DataUtil.parseJson<List<MarketItem>>("markets.json")
        .map { it.copy(query = coinMarketConfiguration.query) }
    private val processedItems = mutableListOf<MarketItem>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic(
            "androidx.room.RoomDatabaseKt"
        )

        // Mock transaction
        val transactionLambda = slot<suspend () -> Any>()
        coEvery { appDatabase.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }

        var index = - 1
        coEvery { coinGeckoService.getMarkets(any(), any(), any(), any()) } coAnswers {
            index += 1
            val chunks = mockResponse.size / TEST_ITEMS_PER_PAGE
            mockResponse.chunked(chunks).getOrNull(index) ?: emptyList()
        }

        val marketItemList = slot<List<MarketItem>>()
        coEvery { marketItemsDao.insertAll(capture(marketItemList)) } coAnswers {
            processedItems.addAll(marketItemList.captured)
        }

        marketsSource = MarketsSource(
            coinGeckoService,
            currencyService,
            appDatabase,
            marketItemsDao,
            coinMarketConfiguration
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
    @Test
    fun `test loading paginated items`() = runTest {
        val repetitions = mockResponse.size / TEST_ITEMS_PER_PAGE
        repeat(repetitions) {
            val loadData = marketsSource.load(
                LoadType.REFRESH,
                state = PagingState(
                    emptyList(),
                    null,
                    PagingConfig(pageSize = TEST_ITEMS_PER_PAGE),
                    0
                )
            )
            assertThat(loadData, instanceOf(RemoteMediator.MediatorResult.Success::class.java))
        }

        assertContentEquals(
            mockResponse, processedItems
        )
    }
}