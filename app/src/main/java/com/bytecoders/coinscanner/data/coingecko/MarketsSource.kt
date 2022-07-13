package com.bytecoders.coinscanner.data.coingecko

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import retrofit2.HttpException
import java.io.IOException

class MarketsSource(
    private val geckoService: CoinGeckoService,
    private val currency: String,
    private val order: GeckoOrder
) : PagingSource<Int, MarketItem>() {

    override val keyReuseSupported: Boolean = true

    override fun getRefreshKey(state: PagingState<Int, MarketItem>): Int? =
        state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MarketItem> =
        try {
            val pageNumber = params.key ?: 0
            val markets = geckoService.getMarkets(
                page = pageNumber, itemsPerPage = params.loadSize,
                currency = currency, order = order
            )
            LoadResult.Page(
                data = markets,
                prevKey = if (pageNumber > 0) pageNumber - 1 else null,
                nextKey = if (markets.isNotEmpty()) pageNumber + 1 else null
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
}
