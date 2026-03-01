package com.bytecoders.coinscanner.service.coingecko

import com.bytecoders.coinscanner.R
import com.bytecoders.coinscanner.data.coingecko.CoinFullData
import com.bytecoders.coinscanner.data.coingecko.CoinHistory
import com.bytecoders.coinscanner.data.coingecko.CoinListing
import com.bytecoders.coinscanner.data.coingecko.EventResponse
import com.bytecoders.coinscanner.data.coingecko.Exchange
import com.bytecoders.coinscanner.data.coingecko.ExchangeRateResponse
import com.bytecoders.coinscanner.data.coingecko.GlobalDataResponse
import com.bytecoders.coinscanner.data.coingecko.MarketChart
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.data.coingecko.StatusUpdateResponse
import com.bytecoders.coinscanner.data.coingecko.TickerResponse
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
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
    @GET("ping")
    suspend fun ping(): Map<String, String>

    @GET("simple/price")
    suspend fun getSimplePrice(
        @Query("ids") ids: String,
        @Query("vs_currencies") vsCurrencies: String,
        @Query("include_market_cap") includeMarketCap: Boolean = false,
        @Query("include_24hr_vol") include24hrVol: Boolean = false,
        @Query("include_24hr_change") include24hrChange: Boolean = false,
        @Query("include_last_updated_at") includeLastUpdatedAt: Boolean = false
    ): Map<String, Map<String, Double>>

    @GET("simple/token_price/{id}")
    suspend fun getSimpleTokenPrice(
        @Path("id") id: String,
        @Query("contract_addresses") contractAddresses: String,
        @Query("vs_currencies") vsCurrencies: String,
        @Query("include_market_cap") includeMarketCap: Boolean = false,
        @Query("include_24hr_vol") include24hrVol: Boolean = false,
        @Query("include_24hr_change") include24hrChange: Boolean = false,
        @Query("include_last_updated_at") includeLastUpdatedAt: Boolean = false
    ): Map<String, Map<String, Double>>

    @GET("simple/supported_vs_currencies")
    suspend fun getSupportedVsCurrencies(): List<String>

    @GET("coins/list")
    suspend fun getCoinsList(): List<CoinListing>

    @GET("coins/markets")
    suspend fun getMarkets(
        @Query("vs_currency") currency: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int,
        @Query("order") order: GeckoOrder
    ): List<MarketItem>

    @GET("coins/{id}")
    suspend fun getCoinDetails(
        @Path("id") id: String,
        @Query("localization") localization: String = "false",
        @Query("tickers") tickers: Boolean = false,
        @Query("market_data") marketData: Boolean = true,
        @Query("community_data") communityData: Boolean = false,
        @Query("developer_data") developerData: Boolean = false,
        @Query("sparkline") sparkline: Boolean = false
    ): CoinFullData

    @GET("coins/{id}/tickers")
    suspend fun getCoinTickers(
        @Path("id") id: String,
        @Query("exchange_ids") exchangeIds: String? = null,
        @Query("include_exchange_logo") includeExchangeLogo: Boolean? = null,
        @Query("page") page: Int? = null,
        @Query("depth") depth: Boolean? = null,
        @Query("order") order: String? = null
    ): TickerResponse

    @GET("coins/{id}/history")
    suspend fun getCoinHistory(
        @Path("id") id: String,
        @Query("date") date: String,
        @Query("localization") localization: String = "false"
    ): CoinHistory

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") id: String,
        @Query("vs_currency") vsCurrency: String,
        @Query("days") days: String
    ): MarketChart

    @GET("coins/{id}/market_chart/range")
    suspend fun getMarketChartRange(
        @Path("id") id: String,
        @Query("vs_currency") vsCurrency: String,
        @Query("from") from: Long,
        @Query("to") to: Long
    ): MarketChart

    @GET("coins/{id}/status_updates")
    suspend fun getCoinStatusUpdates(
        @Path("id") id: String,
        @Query("per_page") perPage: Int? = null,
        @Query("page") page: Int? = null
    ): StatusUpdateResponse

    @GET("coins/{id}/contract/{contract_address}")
    suspend fun getCoinContractInfo(
        @Path("id") id: String,
        @Path("contract_address") contractAddress: String
    ): CoinFullData

    @GET("exchanges")
    suspend fun getExchanges(
        @Query("per_page") perPage: Int? = null,
        @Query("page") page: Int? = null
    ): List<Exchange>

    @GET("exchanges/list")
    suspend fun getExchangesList(): List<Map<String, String>>

    @GET("exchanges/{id}")
    suspend fun getExchangeDetails(
        @Path("id") id: String
    ): Exchange

    @GET("exchanges/{id}/tickers")
    suspend fun getExchangeTickers(
        @Path("id") id: String,
        @Query("coin_ids") coinIds: String? = null,
        @Query("include_exchange_logo") includeExchangeLogo: Boolean? = null,
        @Query("page") page: Int? = null,
        @Query("depth") depth: Boolean? = null,
        @Query("order") order: String? = null
    ): TickerResponse

    @GET("exchanges/{id}/status_updates")
    suspend fun getExchangeStatusUpdates(
        @Path("id") id: String,
        @Query("per_page") perPage: Int? = null,
        @Query("page") page: Int? = null
    ): StatusUpdateResponse

    @GET("status_updates")
    suspend fun getStatusUpdates(
        @Query("category") category: String? = null,
        @Query("project_type") projectType: String? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("page") page: Int? = null
    ): StatusUpdateResponse

    @GET("events")
    suspend fun getEvents(
        @Query("country_code") countryCode: String? = null,
        @Query("type") type: String? = null,
        @Query("page") page: Int? = null,
        @Query("upcoming_events_only") upcomingEventsOnly: Boolean? = null,
        @Query("from_date") fromDate: String? = null,
        @Query("to_date") toDate: String? = null
    ): EventResponse

    @GET("events/countries")
    suspend fun getEventCountries(): Map<String, Any>

    @GET("events/types")
    suspend fun getEventTypes(): Map<String, Any>

    @GET("exchange_rates")
    suspend fun getExchangeRates(): ExchangeRateResponse

    @GET("global")
    suspend fun getGlobal(): GlobalDataResponse
}
