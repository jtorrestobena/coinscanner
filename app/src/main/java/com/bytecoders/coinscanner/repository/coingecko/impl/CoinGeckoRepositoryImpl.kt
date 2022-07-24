package com.bytecoders.coinscanner.repository.coingecko.impl

import androidx.paging.PagingSource
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.data.coingecko.MarketsSource
import com.bytecoders.coinscanner.data.database.AppDatabase
import com.bytecoders.coinscanner.data.database.MarketItemsDao
import com.bytecoders.coinscanner.repository.coingecko.CoinGeckoRepository
import com.bytecoders.coinscanner.repository.coingecko.CoinMarketConfiguration
import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.service.currency.CurrencyService
import javax.inject.Inject

class CoinGeckoRepositoryImpl @Inject constructor(
    geckoService: CoinGeckoService,
    currencyService: CurrencyService,
    appDatabase: AppDatabase,
    private val marketItemsDao: MarketItemsDao
) :
    CoinGeckoRepository {

    private var marketConfiguration = CoinMarketConfiguration()

    override val markets: MarketsSource = MarketsSource(
        geckoService,
        currencyService,
        appDatabase,
        marketItemsDao
    )

    override val pagingSource: PagingSource<Int, MarketItem>
        get() = marketItemsDao.sourceForQuery(marketConfiguration.query)

    override fun updateConfiguration(newConfiguration: CoinMarketConfiguration) {
        markets.coinMarketConfig = newConfiguration
        marketConfiguration = newConfiguration
    }
}
