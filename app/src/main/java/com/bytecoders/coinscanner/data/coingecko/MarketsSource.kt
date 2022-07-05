package com.bytecoders.coinscanner.data.coingecko

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

class MarketsSource : PagingSource<Int, MarketItem>() {
    override fun getRefreshKey(state: PagingState<Int, MarketItem>): Int? =
            state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MarketItem> =
            try {
                val nextPage = params.key ?: 1
                //val userList = RetrofitClient.apiService.getUserList(page = nextPage)
                val marketsList = emptyList<MarketItem>()
                LoadResult.Page(
                        data = marketsList,
                        prevKey = if (nextPage == 1) null else nextPage - 1,
                        nextKey = if (marketsList.isEmpty()) null else nextPage + 1
                )
            } catch (exception: IOException) {
                LoadResult.Error(exception)
            } catch (exception: HttpException) {
                LoadResult.Error(exception)
            }
}