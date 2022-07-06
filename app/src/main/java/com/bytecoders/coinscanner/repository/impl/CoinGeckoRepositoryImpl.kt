package com.bytecoders.coinscanner.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.data.coingecko.MarketsSource
import com.bytecoders.coinscanner.repository.CoinGeckoRepository
import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import com.bytecoders.coinscanner.ui.home.ITEMS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CoinGeckoRepositoryImpl @Inject constructor(private val geckoService: CoinGeckoService) :
    CoinGeckoRepository {

    override fun getMarkets(
        currency: String,
        itemsPerPage: Int,
        order: GeckoOrder
    ): Flow<PagingData<MarketItem>> {
        return Pager(PagingConfig(pageSize = ITEMS_PER_PAGE)) {
            MarketsSource(geckoService)
        }.flow
    }
}
