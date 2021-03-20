package com.bytecoders.coinscanner.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytecoders.coinscanner.repository.CoinGeckoRepository
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import com.bytecoders.coinscanner.service.coingecko.MarketItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

const val ITEMS_PER_PAGE = 50

class HomeViewModel : ViewModel() {
    private val coinGeckoRepository = CoinGeckoRepository()
    private val _markets = MutableLiveData<List<MarketItem>>()
    val markets: LiveData<List<MarketItem>> get() = _markets
    private var page = 1

    fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            coinGeckoRepository.getMarkets(page = page, itemsPerPage = ITEMS_PER_PAGE,
                    currency = "usd", order = GeckoOrder.MARKET_CAP_DESC)
                    .collect { marketItems ->
                        _markets.value = marketItems
                        page += 1
                    }
        }
    }
}