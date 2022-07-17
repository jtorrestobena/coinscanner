package com.bytecoders.coinscanner.repository

import com.bytecoders.coinscanner.data.coingecko.MarketsSource

interface CoinGeckoRepository {

    fun getMarkets(
        configuration: CoinMarketConfiguration
    ): MarketsSource

    fun refreshMarkets(newConfiguration: CoinMarketConfiguration? = null)
}
