package com.bytecoders.coinscanner.data.coingecko

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import retrofit2.HttpException
import java.io.IOException

const val INITIAL_PAGE = 1
class MarketsSource(
    private val geckoService: CoinGeckoService,
    private val currency: String,
    private val order: GeckoOrder,
    private val pageSize: Int,
) : PagingSource<Int, MarketItem>() {

    private var resetPage = false

    override fun getRefreshKey(state: PagingState<Int, MarketItem>): Int? {
        if (resetPage) {
            resetPage = false
            return null
        }

        return state.anchorPosition?.let { anchorPosition ->
            anchorPosition / pageSize
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MarketItem> =
        try {
            val pageNumber = params.key ?: INITIAL_PAGE
            val markets = geckoService.getMarkets(
                page = pageNumber, itemsPerPage = pageSize,
                currency = currency, order = order
            )
            LoadResult.Page(
                itemsBefore = pageNumber * pageSize,
                data = markets,
                prevKey = if (pageNumber > 0) pageNumber - 1 else null,
                nextKey = if (markets.isNotEmpty()) pageNumber + 1 else null
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    fun resetPage() {
        resetPage = true
    }
}
