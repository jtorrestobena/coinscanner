package com.bytecoders.coinscanner.data.coingecko

import com.google.gson.annotations.SerializedName

data class Roi(
    @SerializedName("currency")
    val currency: String = "",
    @SerializedName("percentage")
    val percentage: Double = 0.0,
    @SerializedName("times")
    val times: Double = 0.0
)
