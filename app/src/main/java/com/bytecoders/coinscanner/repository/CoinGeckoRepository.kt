package com.bytecoders.coinscanner.repository

import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import com.bytecoders.coinscanner.service.coingecko.MarketItem
import kotlinx.coroutines.flow.Flow

interface CoinGeckoRepository {

    fun getMarkets(
        currency: String,
        page: Int,
        itemsPerPage: Int,
        order: GeckoOrder
    ): Flow<List<MarketItem>>
}
