package com.bytecoders.coinscanner.ui.home

import androidx.lifecycle.*
import com.bytecoders.coinscanner.repository.CoinGeckoRepository
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import com.bytecoders.coinscanner.service.coingecko.MarketItem
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

const val ITEMS_PER_PAGE = 50
private const val CURRENT_PAGE_SAVED_STATE = "CURRENT_PAGE_SAVED_STATE"

@HiltViewModel
class HomeViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle,
                                        private val coinGeckoRepository: CoinGeckoRepository) : ViewModel() {
    private val _markets = MutableLiveData<List<MarketItem>>()
    val markets: LiveData<List<MarketItem>> get() = _markets

    private var page = savedStateHandle[CURRENT_PAGE_SAVED_STATE] ?: 1

    fun load() {
        viewModelScope.launch(Dispatchers.Main) {
            coinGeckoRepository.getMarkets(page = page, itemsPerPage = ITEMS_PER_PAGE,
                    currency = "usd", order = GeckoOrder.MARKET_CAP_DESC)
                    .collect { marketItems ->
                        _markets.value = marketItems
                        page += 1
                        savedStateHandle[CURRENT_PAGE_SAVED_STATE] = page
                    }
        }
    }
}