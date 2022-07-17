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

    @Query("SELECT * FROM market_item WHERE `query` LIKE :query")
    fun sourceForQuery(query: String): PagingSource<Int, MarketItem>

    @Query("SELECT * FROM market_item")
    fun getAllMarketItems(): PagingSource<Int, MarketItem>

    @Query("DELETE FROM market_item")
    suspend fun clearAll()
}