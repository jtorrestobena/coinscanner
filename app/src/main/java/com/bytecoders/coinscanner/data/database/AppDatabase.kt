package com.bytecoders.coinscanner.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.data.state.HomeUiState

@Database(entities = [MarketItem::class, HomeUiState::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun marketsDao(): MarketItemsDao
    abstract fun uiStateDao(): UiStateDao
}
