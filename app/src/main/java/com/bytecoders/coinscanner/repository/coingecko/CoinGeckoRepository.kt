package com.bytecoders.coinscanner.repository.coingecko

import androidx.paging.PagingData
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import kotlinx.coroutines.flow.Flow

const val ITEMS_PER_PAGE = 50

interface CoinGeckoRepository {

    val markets: Flow<PagingData<MarketItem>>

    fun updateConfiguration(newConfiguration: CoinMarketConfiguration)
}
