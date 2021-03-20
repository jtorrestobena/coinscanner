package com.bytecoders.coinscanner.service

import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.service.converter.EnumConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceFactory {
    fun getCoinGeckoService(): CoinGeckoService {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://coingecko.p.rapidapi.com/")
                .client(OkHttpClient.Builder()
                        .addInterceptor {
                            it.proceed(it.request().newBuilder()
                                    .addHeader("x-rapidapi-key", "9dca701489msha60ad7038f0b5fap150af9jsnd6101dc279bf")
                                    .addHeader("x-rapidapi-host", "coingecko.p.rapidapi.com")
                                    .build())
                        }
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(EnumConverterFactory())
                .build()

        return retrofit.create(CoinGeckoService::class.java)
    }
}