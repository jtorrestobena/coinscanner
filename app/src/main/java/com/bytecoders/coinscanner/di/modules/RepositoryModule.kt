package com.bytecoders.coinscanner.di.modules

import com.bytecoders.coinscanner.repository.CoinGeckoRepository
import com.bytecoders.coinscanner.repository.impl.CoinGeckoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindCoinGeckoRepository(
        coinGeckoRepositoryImpl: CoinGeckoRepositoryImpl
    ): CoinGeckoRepository
}