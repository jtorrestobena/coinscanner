package com.bytecoders.coinscanner.repository.coingecko

import androidx.paging.PagingSource
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.data.coingecko.MarketsSource

interface CoinGeckoRepository {

    val markets: MarketsSource

    val pagingSource: PagingSource<Int, MarketItem>

    fun updateConfiguration(newConfiguration: CoinMarketConfiguration)
}
