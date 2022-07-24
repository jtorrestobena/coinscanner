package com.bytecoders.coinscanner.ui.home

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.versionedparcelable.ParcelField
import androidx.versionedparcelable.VersionedParcelize
import com.bytecoders.coinscanner.currency.CurrencyManager
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.repository.CoinGeckoRepository
import com.bytecoders.coinscanner.repository.CoinMarketConfiguration
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.parcelize.Parcelize
import java.util.*
import javax.inject.Inject

const val ITEMS_PER_PAGE = 50

private const val HOME_UI_STATE = "HOME_UI_STATE"

@Parcelize
data class HomeUiState(
    val marketOrdering: GeckoOrder = GeckoOrder.MARKET_CAP_DESC,
    val currency: Currency = CurrencyManager.getDefaultCurrency(),
    val isRefreshing: Boolean = false
): Parcelable

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val coinGeckoRepository: CoinGeckoRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var uiState by mutableStateOf(savedStateHandle[HOME_UI_STATE] ?: HomeUiState())
        private set

    private val marketConfiguration: CoinMarketConfiguration
        get() = CoinMarketConfiguration(
            itemsPerPage = ITEMS_PER_PAGE,
            currency = uiState.currency.currencyCode.lowercase(),
            order = uiState.marketOrdering
        )

    @OptIn(ExperimentalPagingApi::class)
    val markets: Flow<PagingData<MarketItem>> = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE),
        remoteMediator = coinGeckoRepository.getMarkets(marketConfiguration),
        pagingSourceFactory = { coinGeckoRepository.pagingSource(marketConfiguration) }
    ).flow.cachedIn(viewModelScope)

    fun changeOrder(newOrdering: GeckoOrder) {
        updateState(uiState.copy(marketOrdering = newOrdering))
        coinGeckoRepository.updateConfiguration(marketConfiguration)
    }

    fun changeCurrency(newCurrency: Currency) {
        updateState(uiState.copy(currency = newCurrency))
        coinGeckoRepository.updateConfiguration(marketConfiguration)
    }

    private fun updateState(newUiState: HomeUiState) {
        uiState = newUiState
        savedStateHandle[HOME_UI_STATE] = newUiState
    }
}
