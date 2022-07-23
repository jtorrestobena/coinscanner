package com.bytecoders.coinscanner.data.currency

import com.google.gson.annotations.SerializedName

data class CurrencyConversion(
    @SerializedName("new_amount")
    val newAmount: Double = 0.0,
    @SerializedName("new_currency")
    val newCurrency: String = "",
    @SerializedName("old_amount")
    val oldAmount: Double = 0.0,
    @SerializedName("old_currency")
    val oldCurrency: String = ""
)
