package com.bytecoders.coinscanner.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bytecoders.coinscanner.currency.CurrencyManager
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.repository.CoinGeckoRepository
import com.bytecoders.coinscanner.repository.CoinMarketConfiguration
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

const val ITEMS_PER_PAGE = 50
const val LOAD_MAX_3_TIMES_PAGE_SIZE = 3 * ITEMS_PER_PAGE

data class HomeUiState(
    val marketOrdering: GeckoOrder = GeckoOrder.MARKET_CAP_DESC,
    val currency: Currency = CurrencyManager.getDefaultCurrency(),
    val isRefreshing: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val coinGeckoRepository: CoinGeckoRepository
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    private val marketConfiguration: CoinMarketConfiguration
        get() = CoinMarketConfiguration(
            itemsPerPage = ITEMS_PER_PAGE,
            currency = uiState.currency.currencyCode.lowercase(),
            order = uiState.marketOrdering
        )

    val markets: Flow<PagingData<MarketItem>> = Pager(
        PagingConfig(
            pageSize = ITEMS_PER_PAGE,
            enablePlaceholders = false,
            maxSize = LOAD_MAX_3_TIMES_PAGE_SIZE,
            initialLoadSize = ITEMS_PER_PAGE
        )
    ) {
        coinGeckoRepository.getMarkets(marketConfiguration)
    }.flow.cachedIn(viewModelScope)

    fun refreshMarkets() {
        uiState = uiState.copy(isRefreshing = true)
        coinGeckoRepository.refreshMarkets()
    }

    fun changeOrder(newOrdering: GeckoOrder) {
        uiState = uiState.copy(marketOrdering = newOrdering)
        coinGeckoRepository.refreshMarkets(marketConfiguration)
    }

    fun changeCurrency(newCurrency: Currency) {
        uiState = uiState.copy(currency = newCurrency)
        coinGeckoRepository.refreshMarkets(marketConfiguration)
    }
}
