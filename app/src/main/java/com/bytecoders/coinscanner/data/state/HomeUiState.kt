package com.bytecoders.coinscanner.data.state

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.bytecoders.coinscanner.currency.CurrencyManager
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import java.util.*

@Entity(tableName = "home_ui_state")
data class HomeUiState(
    @PrimaryKey
    val id: Int = 0,
    val marketOrdering: GeckoOrder = GeckoOrder.MARKET_CAP_DESC,
    val currency: Currency = CurrencyManager.getDefaultCurrency(),
    val isRefreshing: Boolean = false,
    val stale: Boolean = true
)