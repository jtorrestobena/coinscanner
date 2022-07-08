package com.bytecoders.coinscanner.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.repository.CoinGeckoRepository
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val ITEMS_PER_PAGE = 50
// private const val CURRENT_PAGE_SAVED_STATE = "CURRENT_PAGE_SAVED_STATE"

@HiltViewModel
class HomeViewModel @Inject constructor(
    coinGeckoRepository: CoinGeckoRepository
) : ViewModel() {
    val markets: Flow<PagingData<MarketItem>> =
        coinGeckoRepository.getMarkets(
            itemsPerPage = ITEMS_PER_PAGE,
            currency = "usd", order = GeckoOrder.MARKET_CAP_DESC
        ).cachedIn(viewModelScope)

    /*val markets: Flow<List<MarketItem>> get() = coinGeckoRepository.getMarkets(
        page = page, itemsPerPage = ITEMS_PER_PAGE,
        currency = "usd", order = GeckoOrder.MARKET_CAP_DESC
    )

    .collect { marketItems ->
        _markets.value = marketItems
        page += 1
        savedStateHandle[CURRENT_PAGE_SAVED_STATE] = page
    }*/
}
