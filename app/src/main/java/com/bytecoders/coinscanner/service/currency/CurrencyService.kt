package com.bytecoders.coinscanner.service.currency

import com.bytecoders.coinscanner.data.currency.CurrencyConversion
import com.bytecoders.coinscanner.repository.coingecko.DEFAULT_CURRENCY
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {
    @GET("convertcurrency")
    suspend fun convertCurrency(
        @Query("have") baseCurrency: String = DEFAULT_CURRENCY,
        @Query("want") targetCurrency: String,
        @Query("amount") amount: Int = 1,
    ): CurrencyConversion
}
