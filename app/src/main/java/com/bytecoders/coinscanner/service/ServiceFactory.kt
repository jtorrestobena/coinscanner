package com.bytecoders.coinscanner.service

import com.bytecoders.coinscanner.BuildConfig.RAPID_API_KEY
import com.bytecoders.coinscanner.service.coingecko.CoinGeckoService
import com.bytecoders.coinscanner.service.converter.EnumConverterFactory
import com.bytecoders.coinscanner.service.currency.CurrencyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object ServiceFactory {

    @Provides
    fun provideCoinGeckoService(): CoinGeckoService {
        val host = "coingecko.p.rapidapi.com"
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://$host/")
            .client(
                getRapidApiOkHttpClient(
                    host
                )
            )
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(EnumConverterFactory())
            .build()

        return retrofit.create(CoinGeckoService::class.java)
    }

    @Provides
    fun provideCurrencyService(): CurrencyService {
        val host = "currency-converter-by-api-ninjas.p.rapidapi.com"
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://$host/v1/")
            .client(
                getRapidApiOkHttpClient(
                    host
                )
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CurrencyService::class.java)
    }

    private fun getRapidApiOkHttpClient(apiHost: String) = OkHttpClient.Builder()
        .addInterceptor {
            it.proceed(
                it.request().newBuilder()
                    .addHeader(
                        "x-rapidapi-key",
                        RAPID_API_KEY
                    )
                    .addHeader("x-rapidapi-host", apiHost)
                    .build()
            )
        }
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()
}
