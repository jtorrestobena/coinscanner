package com.bytecoders.coinscanner.data.coingecko

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bytecoders.coinscanner.data.database.AppDatabase
import com.bytecoders.coinscanner.data.database.MarketItemsDao
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
    private val currency: String,
    private val order: GeckoOrder,
    private val pageSize: Int,
) : RemoteMediator<Int, MarketItem>() {

    private var currentPage = INITIAL_PAGE

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MarketItem>
    ): MediatorResult {
        return try {
            // The network load method takes an optional after=<user.id>
            // parameter. For every page after the first, pass the last user
            // ID to let it continue from where it left off. For REFRESH,
            // pass null to load the first page.
            val pageNumber = when (loadType) {
                LoadType.REFRESH -> {
                    currentPage = INITIAL_PAGE
                    currentPage
                }
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )

                    // You must explicitly check if the last item is null when
                    // appending, since passing null to networkService is only
                    // valid for initial load. If lastItem is null it means no
                    // items were loaded after the initial REFRESH and there are
                    // no more items to load.

                    currentPage++
                    currentPage
                }
            }

            val markets = geckoService.getMarkets(
                page = pageNumber, itemsPerPage = pageSize,
                currency = currency, order = order
            )

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    marketItemsDao.clearAll()
                }

                // Insert new users into database, which invalidates the
                // current PagingData, allowing Paging to present the updates
                // in the DB.
                marketItemsDao.insertAll(markets)
            }

            MediatorResult.Success(
                endOfPaginationReached = markets.size < pageSize
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
