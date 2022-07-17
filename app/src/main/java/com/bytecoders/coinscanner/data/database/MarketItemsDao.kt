package com.bytecoders.coinscanner.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bytecoders.coinscanner.data.coingecko.MarketItem

@Dao
interface MarketItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(marketItems: List<MarketItem>)

    @Query("SELECT * FROM market_item WHERE id LIKE :identifier")
    fun pagingSource(identifier: String): PagingSource<Int, MarketItem>

    @Query("DELETE FROM market_item")
    suspend fun clearAll()
}