package com.bytecoders.coinscanner.data.coingecko

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bytecoders.coinscanner.data.currency.CurrencyConversion
import com.bytecoders.coinscanner.data.database.AppDatabase
import com.bytecoders.coinscanner.data.database.MarketItemsDao
import com.bytecoders.coinscanner.repository.CoinMarketConfiguration
import com.bytecoders.coinscanner.repository.DEFAULT_CURRENCY
import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.service.currency.CurrencyService
import retrofit2.HttpException
import java.io.IOException

const val INITIAL_PAGE = 1
private const val BAD_REQUEST = 400

@OptIn(ExperimentalPagingApi::class)
class MarketsSource(
    private val geckoService: CoinGeckoService,
    private val currencyService: CurrencyService,
    private val appDatabase: AppDatabase,
    private val marketItemsDao: MarketItemsDao,
    var coinMarketConfiguration: CoinMarketConfiguration
) : RemoteMediator<Int, MarketItem>() {

    private var currentPage = INITIAL_PAGE
    private val currencyCache = mutableMapOf<String, CurrencyConversion>()

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

            val markets = try {
                if (currencyCache.containsKey(coinMarketConfiguration.currency)) {
                    performCurrencyConversion()
                } else {
                    geckoService.getMarkets(
                        page = currentPage,
                        itemsPerPage = coinMarketConfiguration.itemsPerPage,
                        currency = coinMarketConfiguration.currency,
                        order = coinMarketConfiguration.order
                    )
                }
            } catch (e: HttpException) {
                if (e.code() != BAD_REQUEST) throw e
                performCurrencyConversion()
            }

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

    private suspend fun performCurrencyConversion(): List<MarketItem> {
        val currencyConversion: CurrencyConversion =
            currencyCache[coinMarketConfiguration.currency]
                ?: currencyService.convertCurrency(targetCurrency = coinMarketConfiguration.currency)
                    .apply {
                        currencyCache[coinMarketConfiguration.currency] = this
                    }

        /**
         * Fallback to DEFAULT_CURRENCY if API does not support it,
         * then apply conversion to obtain new values
         */
        return geckoService.getMarkets(
            page = currentPage,
            itemsPerPage = coinMarketConfiguration.itemsPerPage,
            currency = DEFAULT_CURRENCY,
            order = coinMarketConfiguration.order
        ).map {
            it.copy(currentPrice = it.currentPrice * currencyConversion.newAmount)
        }
    }
}
