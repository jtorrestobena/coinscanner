package com.bytecoders.coinscanner.service.coingecko

data class Roi(
    val currency: String,
    val percentage: Double,
    val times: Double
)