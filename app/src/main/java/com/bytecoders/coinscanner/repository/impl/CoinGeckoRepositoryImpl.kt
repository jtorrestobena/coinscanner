package com.bytecoders.coinscanner.repository.impl

import com.bytecoders.coinscanner.repository.CoinGeckoRepository
import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import com.bytecoders.coinscanner.service.coingecko.MarketItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CoinGeckoRepositoryImpl @Inject constructor(private val geckoService: CoinGeckoService) :
    CoinGeckoRepository {

    override fun getMarkets(
        currency: String,
        page: Int,
        itemsPerPage: Int,
        order: GeckoOrder
    ): Flow<List<MarketItem>> {
        return flow {
            emit(geckoService.getMarkets(currency, page, itemsPerPage, order))
        }.flowOn(Dispatchers.IO)
    }
}
