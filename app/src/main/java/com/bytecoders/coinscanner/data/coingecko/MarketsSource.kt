package com.bytecoders.coinscanner.data.coingecko

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import retrofit2.HttpException
import java.io.IOException

class MarketsSource(private val geckoService: CoinGeckoService) : PagingSource<Int, MarketItem>() {
    override fun getRefreshKey(state: PagingState<Int, MarketItem>): Int? =
        state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MarketItem> =
        try {
            val currentPage = params.key ?: 0
            val nextPage = currentPage + 1
            val markets = geckoService.getMarkets(
                page = currentPage, itemsPerPage = params.loadSize,
                currency = "usd", order = GeckoOrder.MARKET_CAP_DESC
            )
            LoadResult.Page(
                data = markets,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (markets.isEmpty()) null else nextPage + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
}
