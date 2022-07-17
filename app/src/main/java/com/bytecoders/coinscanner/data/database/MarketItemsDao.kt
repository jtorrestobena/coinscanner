package com.bytecoders.coinscanner.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder

@Dao
interface MarketItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(marketItems: List<MarketItem>)

    @Query("SELECT * FROM market_item WHERE roi_currency LIKE :currency ORDER BY marketCap DESC LIMIT :itemsPerPage")
    fun pagingSource(currency: String, /*order: GeckoOrder,*/ itemsPerPage: Int): PagingSource<Int, MarketItem>

    @Query("SELECT * FROM market_item")
    fun getAllMarketItems(): PagingSource<Int, MarketItem>

    @Query("DELETE FROM market_item")
    suspend fun clearAll()
}