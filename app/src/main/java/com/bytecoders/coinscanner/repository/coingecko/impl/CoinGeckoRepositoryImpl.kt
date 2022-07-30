package com.bytecoders.coinscanner.repository.coingecko.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.data.coingecko.MarketsSource
import com.bytecoders.coinscanner.data.database.AppDatabase
import com.bytecoders.coinscanner.data.database.MarketItemsDao
import com.bytecoders.coinscanner.repository.coingecko.CoinGeckoRepository
import com.bytecoders.coinscanner.repository.coingecko.CoinMarketConfiguration
import com.bytecoders.coinscanner.repository.coingecko.ITEMS_PER_PAGE
import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.service.currency.CurrencyService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoinGeckoRepositoryImpl @Inject constructor(
    geckoService: CoinGeckoService,
    currencyService: CurrencyService,
    appDatabase: AppDatabase,
    private val marketItemsDao: MarketItemsDao
) :
    CoinGeckoRepository {

    private var marketConfiguration = CoinMarketConfiguration()

    private val marketsSource: MarketsSource = MarketsSource(
        geckoService,
        currencyService,
        appDatabase,
        marketItemsDao
    )

    @OptIn(ExperimentalPagingApi::class)
    override val markets: Flow<PagingData<MarketItem>> = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE),
        remoteMediator = marketsSource,
        pagingSourceFactory = { marketItemsDao.sourceForQuery(marketConfiguration.query) }
    ).flow

    override fun updateConfiguration(newConfiguration: CoinMarketConfiguration) {
        marketsSource.coinMarketConfig = newConfiguration
        marketConfiguration = newConfiguration
    }
}
