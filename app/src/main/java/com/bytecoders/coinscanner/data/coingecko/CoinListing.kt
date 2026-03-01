package com.bytecoders.coinscanner.data.coingecko

import com.google.gson.annotations.SerializedName

data class CoinListing(
    @SerializedName("id")
    val id: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("name")
    val name: String
)
