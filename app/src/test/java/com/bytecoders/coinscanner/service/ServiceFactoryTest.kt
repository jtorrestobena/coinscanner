package com.bytecoders.coinscanner.service

import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ServiceFactoryTest : TestCase() {
    lateinit var geckoService: CoinGeckoService

    public override fun setUp() {
        super.setUp()
        geckoService = ServiceFactory().getCoinGeckoService()
    }

    public override fun tearDown() {}

    @Test
    fun testGeckoService() {
        runBlocking {
            val markets = geckoService.getMarkets("usd", 1, 10, GeckoOrder.MARKET_CAP_DESC)
            assertNotNull(markets)
        }
    }
}