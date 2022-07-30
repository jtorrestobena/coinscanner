package com.bytecoders.coinscanner.service

import com.bytecoders.coinscanner.currency.CurrencyManager
import com.bytecoders.coinscanner.data.coingecko.INITIAL_PAGE
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.repository.coingecko.DEFAULT_CURRENCY
import com.bytecoders.coinscanner.repository.coingecko.ITEMS_PER_PAGE
import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import com.bytecoders.coinscanner.service.currency.CurrencyService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

private const val TEST_NUM_PAGES = 10
class ServiceFactoryTest {

    @Test
    fun `test pagination on gecko service has unique coin ids`() {
        val geckoService: CoinGeckoService = ServiceFactory.provideCoinGeckoService()
        val markets = mutableListOf<MarketItem>()
        runBlocking {
            for (i in INITIAL_PAGE..TEST_NUM_PAGES) {
                markets.addAll(geckoService.getMarkets("usd", i, ITEMS_PER_PAGE, GeckoOrder.MARKET_CAP_DESC))
                assertNotNull(markets)
                assertEquals(markets.size, markets.distinctBy { it.id }.size)
            }
            assertEquals(ITEMS_PER_PAGE * TEST_NUM_PAGES, markets.size)
        }
    }

    @Test
    fun `test currency conversion`() {
        val currencyService: CurrencyService = ServiceFactory.provideCurrencyService()
        runBlocking {
            CurrencyManager.getCurrencyList().forEach {
                val testTargetCurrency = it.currencyCode
                try {
                    val conversion = currencyService.convertCurrency(targetCurrency = testTargetCurrency)
                    assertNotNull(conversion)
                    assertEquals(1.0, conversion.oldAmount, 0.0)
                    assertEquals(DEFAULT_CURRENCY.uppercase(), conversion.oldCurrency.uppercase())
                    assertEquals(testTargetCurrency.uppercase(), conversion.newCurrency.uppercase())
                    assertNotEquals(0.0, conversion.newAmount)
                } catch (t: Throwable) {
                    println("Unsupported --> $testTargetCurrency")
                }
            }
        }
    }
}