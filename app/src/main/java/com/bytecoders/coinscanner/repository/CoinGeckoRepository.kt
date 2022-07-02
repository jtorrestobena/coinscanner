package com.bytecoders.coinscanner.repository

import com.bytecoders.coinscanner.service.ServiceFactory
import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import com.bytecoders.coinscanner.service.coingecko.MarketItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.http.Query
import javax.inject.Inject

interface CoinGeckoRepository {

    fun getMarkets(
        currency: String,
        page: Int,
        itemsPerPage: Int,
        order: GeckoOrder
    ): Flow<List<MarketItem>>
}