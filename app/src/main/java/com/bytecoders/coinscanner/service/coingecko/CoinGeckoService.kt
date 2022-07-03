package com.bytecoders.coinscanner.service.coingecko

import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

// marketcapdesc, geckodesc, geckoasc, marketcapasc,
enum class GeckoOrder {
    @SerializedName("market_cap_desc")
    MARKET_CAP_DESC,
    @SerializedName("market_cap_asc")
    MARKET_CAP_ASC,
    @SerializedName("gecko_desc")
    GECKO_DESC,
    @SerializedName("gecko_asc")
    GECKO_ASC,
    @SerializedName("volume_asc")
    VOLUME_ASC,
    @SerializedName("volume_desc")
    VOLUME_DESC
}

interface CoinGeckoService {
    @GET("coins/markets")
    suspend fun getMarkets(
        @Query("vs_currency") currency: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int,
        @Query("order") order: GeckoOrder
    ): List<MarketItem>
}
