package com.bytecoders.coinscanner.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.data.coingecko.MarketsSource
import com.bytecoders.coinscanner.data.database.AppDatabase
import com.bytecoders.coinscanner.data.database.MarketItemsDao
import com.bytecoders.coinscanner.repository.CoinGeckoRepository
import com.bytecoders.coinscanner.repository.CoinMarketConfiguration
import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.ui.home.ITEMS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CoinGeckoRepositoryImpl @Inject constructor(
    private val geckoService: CoinGeckoService,
    private val marketItemsDao: MarketItemsDao
) :
    CoinGeckoRepository {

    private var marketsSource: MarketsSource? = null
    private var coinMarketConfiguration = CoinMarketConfiguration()

    override fun getMarkets(configuration: CoinMarketConfiguration): MarketsSource {
        coinMarketConfiguration = configuration
        return MarketsSource(
            geckoService,
            coinMarketConfiguration.currency,
            coinMarketConfiguration.order,
            ITEMS_PER_PAGE
        ).apply {
            if (marketsSource?.invalid == true) {
                resetPage()
            }
            marketsSource = this
        }
    }

    override fun refreshMarkets(newConfiguration: CoinMarketConfiguration?) {
        newConfiguration?.let {
            coinMarketConfiguration = it
        }
        marketsSource?.invalidate()
    }
}
