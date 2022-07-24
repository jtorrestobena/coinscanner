package com.bytecoders.coinscanner.di.modules

import com.bytecoders.coinscanner.repository.coingecko.CoinGeckoRepository
import com.bytecoders.coinscanner.repository.coingecko.impl.CoinGeckoRepositoryImpl
import com.bytecoders.coinscanner.repository.uistate.UiStateRepository
import com.bytecoders.coinscanner.repository.uistate.impl.UiStateRepositoryImpl
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

    @Binds
    fun bindUiStateRepository(
        uiStateRepositoryImpl: UiStateRepositoryImpl
    ): UiStateRepository
}
