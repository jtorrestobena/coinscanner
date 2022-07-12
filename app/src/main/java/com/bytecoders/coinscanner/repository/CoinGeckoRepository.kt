package com.bytecoders.coinscanner.repository

import androidx.paging.PagingData
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import kotlinx.coroutines.flow.Flow

interface CoinGeckoRepository {

    fun getMarkets(
        currency: String,
        itemsPerPage: Int,
        order: GeckoOrder
    ): Flow<PagingData<MarketItem>>

    fun refreshMarkets()
}
