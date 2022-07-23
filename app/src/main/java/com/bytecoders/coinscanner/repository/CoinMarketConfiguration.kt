package com.bytecoders.coinscanner.repository

import com.bytecoders.coinscanner.service.coingecko.GeckoOrder

const val DEFAULT_CURRENCY = "usd"
data class CoinMarketConfiguration(
    val currency: String = DEFAULT_CURRENCY,
    val itemsPerPage: Int = 0,
    val order: GeckoOrder = GeckoOrder.MARKET_CAP_DESC
) {
    val query = "$currency:$order"
}
