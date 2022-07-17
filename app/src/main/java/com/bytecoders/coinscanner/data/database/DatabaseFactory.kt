package com.bytecoders.coinscanner.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseFactory {
    @Provides
    @Singleton
    fun provideApplicationDatabase(@ApplicationContext application: Context): AppDatabase =
        Room.databaseBuilder(
            application,
            AppDatabase::class.java, "database-coin_scanner"
        ).build()

    @Provides
    fun provideMarketItemsDao(db: AppDatabase): MarketItemsDao = db.marketsDao()
}