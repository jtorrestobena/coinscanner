package com.bytecoders.coinscanner.repository

import com.bytecoders.coinscanner.service.coingecko.GeckoOrder

data class CoinMarketConfiguration(val currency: String = "usd",
                                   val itemsPerPage: Int = 0,
                                   val order: GeckoOrder = GeckoOrder.MARKET_CAP_DESC)