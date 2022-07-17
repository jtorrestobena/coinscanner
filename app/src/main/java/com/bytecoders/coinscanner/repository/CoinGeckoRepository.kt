package com.bytecoders.coinscanner.repository

import androidx.paging.PagingSource
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.data.coingecko.MarketsSource

interface CoinGeckoRepository {

    fun getMarkets(
        configuration: CoinMarketConfiguration
    ): MarketsSource

    fun pagingSource(marketConfiguration: CoinMarketConfiguration): PagingSource<Int, MarketItem>

    fun refreshMarkets(newConfiguration: CoinMarketConfiguration? = null)
}
