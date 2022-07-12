package com.bytecoders.coinscanner.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.repository.CoinGeckoRepository
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

const val ITEMS_PER_PAGE = 50

data class HomeUiState(
    val marketOrdering: GeckoOrder = GeckoOrder.MARKET_CAP_DESC,
    val currency: String = "usd",
    val isRefreshing: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val coinGeckoRepository: CoinGeckoRepository
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    val markets: Flow<PagingData<MarketItem>> =
        coinGeckoRepository.getMarkets(
            itemsPerPage = ITEMS_PER_PAGE,
            currency = uiState.currency, order = uiState.marketOrdering
        ).cachedIn(viewModelScope)

    fun refreshMarkets() {
        uiState = uiState.copy(isRefreshing = true)
        coinGeckoRepository.refreshMarkets()
    }

    fun changeOrder(newOrdering: GeckoOrder) {
        uiState = uiState.copy(marketOrdering = newOrdering)
    }

    fun changeCurrency(newCurrency: String) {
        uiState = uiState.copy(currency = newCurrency)
    }
}
