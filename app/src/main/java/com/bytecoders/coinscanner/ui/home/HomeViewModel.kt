package com.bytecoders.coinscanner.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.data.state.HomeUiState
import com.bytecoders.coinscanner.repository.coingecko.CoinGeckoRepository
import com.bytecoders.coinscanner.repository.coingecko.CoinMarketConfiguration
import com.bytecoders.coinscanner.repository.uistate.UiStateRepository
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

const val ITEMS_PER_PAGE = 50

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val coinGeckoRepository: CoinGeckoRepository,
    private val uiStateRepository: UiStateRepository
) : ViewModel() {

    val uiState by lazy { uiStateRepository.homeUiStateFlow.map {
        _uiState = it
        val marketConfiguration = CoinMarketConfiguration(
            itemsPerPage = ITEMS_PER_PAGE,
            currency = _uiState.currency.currencyCode.lowercase(),
            order = _uiState.marketOrdering
        )
        coinGeckoRepository.updateConfiguration(marketConfiguration)
        _uiState
    } }

    private var _uiState: HomeUiState = HomeUiState()

    @OptIn(ExperimentalPagingApi::class)
    val markets: Flow<PagingData<MarketItem>> = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE),
        remoteMediator = coinGeckoRepository.markets,
        pagingSourceFactory = { coinGeckoRepository.pagingSource }
    ).flow.cachedIn(viewModelScope)

    fun changeOrder(newOrdering: GeckoOrder) {
        updateState(_uiState.copy(marketOrdering = newOrdering))
    }

    fun changeCurrency(newCurrency: Currency) {
        updateState(_uiState.copy(currency = newCurrency))
    }

    private fun updateState(newUiState: HomeUiState) {
        viewModelScope.launch {
            uiStateRepository.updateHomeUiState(newUiState)
        }
    }
}
