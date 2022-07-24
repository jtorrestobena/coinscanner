package com.bytecoders.coinscanner.data.coingecko

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "market_item")
data class MarketItem(
    @SerializedName("ath")
    val ath: Double = 0.0,
    @SerializedName("ath_change_percentage")
    val athChangePercentage: Double = 0.0,
    @SerializedName("ath_date")
    val athDate: Date? = null,
    @SerializedName("atl")
    val atl: Double = 0.0,
    @SerializedName("atl_change_percentage")
    val atlChangePercentage: Double = 0.0,
    @SerializedName("atl_date")
    val atlDate: Date? = null,
    @SerializedName("circulating_supply")
    val circulatingSupply: Double = 0.0,
    @SerializedName("current_price")
    val currentPrice: Double = 0.0,
    @SerializedName("fully_diluted_valuation")
    val fullyDilutedValuation: Double = 0.0,
    @SerializedName("high_24h")
    val high24h: Double = 0.0,
    @PrimaryKey
    @SerializedName("id")
    val id: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("last_updated")
    val lastUpdated: Date? = null,
    @SerializedName("low_24h")
    val low24h: Double = 0.0,
    @SerializedName("market_cap")
    val marketCap: Double = 0.0,
    @SerializedName("market_cap_change_24h")
    val marketCapChange24h: Double = 0.0,
    @SerializedName("market_cap_change_percentage_24h")
    val marketCapChangePercentage24h: Double = 0.0,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int = 0,
    @SerializedName("max_supply")
    val maxSupply: Double = 0.0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("price_change_24h")
    val priceChange24h: Double = 0.0,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double = 0.0,
    @Embedded(prefix = "roi_")
    @SerializedName("roi")
    val roi: Roi? = null,
    @SerializedName("symbol")
    val symbol: String = "",
    @SerializedName("total_supply")
    val totalSupply: Double = 0.0,
    @SerializedName("total_volume")
    val totalVolume: Double = 0.0,
    val query: String = ""
)
