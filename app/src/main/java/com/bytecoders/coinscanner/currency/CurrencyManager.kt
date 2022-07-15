package com.bytecoders.coinscanner.currency

import java.util.*

object CurrencyManager {

    fun getDefaultCurrency(): Currency = Currency.getInstance(Locale.getDefault())

    fun getCurrencyList(): List<Currency> = Locale.getAvailableLocales().mapNotNull {
        try {
            Currency.getInstance(it)
        } catch (e: Exception) {
            null
        }
    }.distinctBy {
        it.currencyCode
    }.sortedBy {
        it.currencyCode
    }
}

fun Currency.displayTitle(): String {
    val displaySymbol = if (currencyCode != symbol) symbol else ""
    return "$currencyCode $displaySymbol $displayName"
}
