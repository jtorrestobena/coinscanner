package com.bytecoders.coinscanner.currency

import java.util.*

object CurrencyManager {

    fun getDefaultCurrency(): Currency = Currency.getInstance(Locale.getDefault())

    fun getCurrencyList(): List<Currency> = Locale.getAvailableLocales().mapNotNull { Currency.getInstance(it) }
}