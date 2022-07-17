package com.bytecoders.coinscanner.repository.impl

import androidx.paging.PagingSource
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.data.coingecko.MarketsSource
import com.bytecoders.coinscanner.data.database.AppDatabase
import com.bytecoders.coinscanner.data.database.MarketItemsDao
import com.bytecoders.coinscanner.repository.CoinGeckoRepository
import com.bytecoders.coinscanner.repository.CoinMarketConfiguration
import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.ui.home.ITEMS_PER_PAGE
import javax.inject.Inject

class CoinGeckoRepositoryImpl @Inject constructor(
    private val geckoService: CoinGeckoService,
    private val appDatabase: AppDatabase,
    private val marketItemsDao: MarketItemsDao
) :
    CoinGeckoRepository {

    private var marketsSource: PagingSource<Int, MarketItem>? = null
    private var coinMarketConfiguration = CoinMarketConfiguration()

    override fun getMarkets(configuration: CoinMarketConfiguration): MarketsSource {
        coinMarketConfiguration = configuration
        return MarketsSource(
            geckoService,
            appDatabase,
            marketItemsDao,
            coinMarketConfiguration.currency,
            coinMarketConfiguration.order,
            ITEMS_PER_PAGE
        )
    }

    override fun pagingSource(marketConfiguration: CoinMarketConfiguration): PagingSource<Int, MarketItem> =
        /*marketItemsDao.pagingSource(
            marketConfiguration.currency,
            //marketConfiguration.order,
            marketConfiguration.itemsPerPage
        )*/
        marketItemsDao.getAllMarketItems().apply {
            marketsSource = this
        }

    override fun updateConfiguration(newConfiguration: CoinMarketConfiguration) {
        coinMarketConfiguration = newConfiguration
    }
}
