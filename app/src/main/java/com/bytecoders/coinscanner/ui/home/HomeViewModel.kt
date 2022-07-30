package com.bytecoders.coinscanner.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.data.state.HomeUiState
import com.bytecoders.coinscanner.repository.coingecko.CoinGeckoRepository
import com.bytecoders.coinscanner.repository.coingecko.CoinMarketConfiguration
import com.bytecoders.coinscanner.repository.coingecko.ITEMS_PER_PAGE
import com.bytecoders.coinscanner.repository.uistate.UiStateRepository
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val coinGeckoRepository: CoinGeckoRepository,
    private val uiStateRepository: UiStateRepository
) : ViewModel() {

    val uiState by lazy {
        uiStateRepository.homeUiStateFlow.map {
            it.apply { updateState(this) }
        }
    }

    private var _uiState: HomeUiState = HomeUiState()

    val markets: Flow<PagingData<MarketItem>> = coinGeckoRepository.markets.cachedIn(viewModelScope)

    fun changeOrder(newOrdering: GeckoOrder) {
        updateState(_uiState.copy(marketOrdering = newOrdering))
    }

    fun changeCurrency(newCurrency: Currency) {
        updateState(_uiState.copy(currency = newCurrency))
    }

    private fun updateState(newUiState: HomeUiState) {
        coinGeckoRepository.updateConfiguration(
            CoinMarketConfiguration(
                itemsPerPage = ITEMS_PER_PAGE,
                currency = newUiState.currency.currencyCode.lowercase(),
                order = newUiState.marketOrdering
            )
        )
        viewModelScope.launch {
            uiStateRepository.updateHomeUiState(newUiState)
        }
    }
}
