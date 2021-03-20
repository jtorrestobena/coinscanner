package com.bytecoders.coinscanner.repository

import com.bytecoders.coinscanner.service.ServiceFactory
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import com.bytecoders.coinscanner.service.coingecko.MarketItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.http.Query

class CoinGeckoRepository {
    private val geckoService = ServiceFactory().getCoinGeckoService()

    fun getMarkets(currency: String, page: Int, itemsPerPage: Int, order: GeckoOrder): Flow<List<MarketItem>> {
        return flow {
            emit(geckoService.getMarkets(currency, page, itemsPerPage, order))
        }.flowOn(Dispatchers.IO)
    }
}