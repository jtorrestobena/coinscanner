package com.bytecoders.coinscanner.data.coingecko

import com.google.gson.annotations.SerializedName

data class MarketChart(
    @SerializedName("prices")
    val prices: List<List<Double>>,
    @SerializedName("market_caps")
    val marketCaps: List<List<Double>>,
    @SerializedName("total_volumes")
    val totalVolumes: List<List<Double>>
)

data class StatusUpdateResponse(
    @SerializedName("status_updates")
    val statusUpdates: List<StatusUpdate>
)

data class StatusUpdate(
    @SerializedName("description")
    val description: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("user")
    val user: String,
    @SerializedName("user_title")
    val userTitle: String,
    @SerializedName("pin")
    val pin: Boolean
)

data class EventResponse(
    @SerializedName("data")
    val data: List<Event>,
    @SerializedName("count")
    val count: Int,
    @SerializedName("page")
    val page: Int
)

data class Event(
    @SerializedName("type")
    val type: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("organizer")
    val organizer: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("website")
    val website: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("venue")
    val venue: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("screenshot")
    val screenshot: String
)

data class ExchangeRateResponse(
    @SerializedName("rates")
    val rates: Map<String, ExchangeRate>
)

data class ExchangeRate(
    @SerializedName("name")
    val name: String,
    @SerializedName("unit")
    val unit: String,
    @SerializedName("value")
    val value: Double,
    @SerializedName("type")
    val type: String
)

data class GlobalDataResponse(
    @SerializedName("data")
    val data: GlobalData
)

data class GlobalData(
    @SerializedName("active_cryptocurrencies")
    val activeCryptocurrencies: Int,
    @SerializedName("upcoming_icos")
    val upcomingIcos: Int,
    @SerializedName("ongoing_icos")
    val ongoingIcos: Int,
    @SerializedName("ended_icos")
    val endedIcos: Int,
    @SerializedName("markets")
    val markets: Int,
    @SerializedName("total_market_cap")
    val totalMarketCap: Map<String, Double>,
    @SerializedName("total_volume")
    val totalVolume: Map<String, Double>,
    @SerializedName("market_cap_percentage")
    val marketCapPercentage: Map<String, Double>,
    @SerializedName("market_cap_change_percentage_24h_usd")
    val marketCapChangePercentage24hUsd: Double,
    @SerializedName("updated_at")
    val updatedAt: Long
)

data class TickerResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("tickers")
    val tickers: List<Ticker>
)

data class Ticker(
    @SerializedName("base")
    val base: String,
    @SerializedName("target")
    val target: String,
    @SerializedName("market")
    val market: Market,
    @SerializedName("last")
    val last: Double,
    @SerializedName("volume")
    val volume: Double,
    @SerializedName("converted_last")
    val convertedLast: Map<String, Double>,
    @SerializedName("converted_volume")
    val convertedVolume: Map<String, Double>,
    @SerializedName("trust_score")
    val trustScore: String?,
    @SerializedName("bid_ask_spread_percentage")
    val bidAskSpreadPercentage: Double?,
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("last_traded_at")
    val lastTradedAt: String,
    @SerializedName("last_fetch_at")
    val lastFetchAt: String,
    @SerializedName("is_anomaly")
    val isAnomaly: Boolean,
    @SerializedName("is_stale")
    val isStale: Boolean,
    @SerializedName("trade_url")
    val tradeUrl: String?,
    @SerializedName("token_info_url")
    val tokenInfoUrl: String?,
    @SerializedName("coin_id")
    val coinId: String,
    @SerializedName("target_coin_id")
    val targetCoinId: String?
)

data class Market(
    @SerializedName("name")
    val name: String,
    @SerializedName("identifier")
    val identifier: String,
    @SerializedName("has_trading_incentive")
    val hasTradingIncentive: Boolean
)

data class Exchange(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("year_established")
    val yearEstablished: Int?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("has_trading_incentive")
    val hasTradingIncentive: Boolean?,
    @SerializedName("trust_score")
    val trustScore: Int?,
    @SerializedName("trust_score_rank")
    val trustScoreRank: Int?,
    @SerializedName("trade_volume_24h_btc")
    val tradeVolume24hBtc: Double?,
    @SerializedName("trade_volume_24h_btc_normalized")
    val tradeVolume24hBtcNormalized: Double?
)

data class CoinFullData(
    @SerializedName("id")
    val id: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: Map<String, String>,
    @SerializedName("image")
    val image: ImageSource,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int?,
    @SerializedName("coingecko_rank")
    val coingeckoRank: Int?,
    @SerializedName("market_data")
    val marketData: MarketData?
)

data class ImageSource(
    @SerializedName("thumb")
    val thumb: String,
    @SerializedName("small")
    val small: String,
    @SerializedName("large")
    val large: String
)

data class MarketData(
    @SerializedName("current_price")
    val currentPrice: Map<String, Double>,
    @SerializedName("market_cap")
    val marketCap: Map<String, Double>,
    @SerializedName("total_volume")
    val totalVolume: Map<String, Double>,
    @SerializedName("high_24h")
    val high24h: Map<String, Double>,
    @SerializedName("low_24h")
    val low24h: Map<String, Double>,
    @SerializedName("price_change_24h")
    val priceChange24h: Double,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double,
    @SerializedName("market_cap_change_24h")
    val marketCapChange24h: Double,
    @SerializedName("market_cap_change_percentage_24h")
    val marketCapChangePercentage24h: Double,
    @SerializedName("circulating_supply")
    val circulatingSupply: Double,
    @SerializedName("total_supply")
    val totalSupply: Double?,
    @SerializedName("max_supply")
    val maxSupply: Double?
)

data class EventCountry(
    @SerializedName("country")
    val country: String,
    @SerializedName("code")
    val code: String
)

data class EventTypeResponse(
    @SerializedName("data")
    val data: List<String>,
    @SerializedName("count")
    val count: Int
)

data class CoinHistory(
    @SerializedName("id")
    val id: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: ImageSource,
    @SerializedName("market_data")
    val marketData: MarketData?
)
