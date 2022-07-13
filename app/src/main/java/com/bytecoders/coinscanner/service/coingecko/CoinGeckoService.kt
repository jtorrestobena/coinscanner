package com.bytecoders.coinscanner.service.coingecko

import com.bytecoders.coinscanner.R
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

// marketcapdesc, geckodesc, geckoasc, marketcapasc,
enum class GeckoOrder(val description: Int) {
    @SerializedName("market_cap_desc")
    MARKET_CAP_DESC(R.string.market_cap_desc),
    @SerializedName("market_cap_asc")
    MARKET_CAP_ASC(R.string.market_cap_asc),
    @SerializedName("gecko_desc")
    GECKO_DESC(R.string.market_gecko_desc),
    @SerializedName("gecko_asc")
    GECKO_ASC(R.string.market_gecko_asc),
    @SerializedName("volume_asc")
    VOLUME_ASC(R.string.market_volume_asc),
    @SerializedName("volume_desc")
    VOLUME_DESC(R.string.market_volume_desc)
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
