package com.bytecoders.coinscanner.data.coingecko

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bytecoders.coinscanner.data.database.AppDatabase
import com.bytecoders.coinscanner.data.database.MarketItemsDao
import com.bytecoders.coinscanner.repository.CoinMarketConfiguration
import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import retrofit2.HttpException
import java.io.IOException

const val INITIAL_PAGE = 1

@OptIn(ExperimentalPagingApi::class)
class MarketsSource(
    private val geckoService: CoinGeckoService,
    private val appDatabase: AppDatabase,
    private val marketItemsDao: MarketItemsDao,
    var coinMarketConfiguration: CoinMarketConfiguration
) : RemoteMediator<Int, MarketItem>() {

    private var currentPage = INITIAL_PAGE

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MarketItem>
    ): MediatorResult {
        return try {
            when (loadType) {
                LoadType.REFRESH -> {
                    currentPage = INITIAL_PAGE
                }
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    currentPage++
                }
            }

            val markets = geckoService.getMarkets(
                page = currentPage,
                itemsPerPage = coinMarketConfiguration.itemsPerPage,
                currency = coinMarketConfiguration.currency,
                order = coinMarketConfiguration.order
            )

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    marketItemsDao.clearAll()
                }

                marketItemsDao.insertAll(markets.map { it.copy(query = coinMarketConfiguration.query) })
            }

            MediatorResult.Success(
                endOfPaginationReached = markets.size < coinMarketConfiguration.itemsPerPage
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
