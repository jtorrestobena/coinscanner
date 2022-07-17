package com.bytecoders.coinscanner.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.data.coingecko.Roi

@Database(entities = [MarketItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun marketsDao(): MarketItemsDao
}