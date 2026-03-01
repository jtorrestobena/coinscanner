package com.bytecoders.coinscanner.service

import android.os.Build
import com.bytecoders.coinscanner.BuildConfig
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
            .client(getRapidApiOkHttpClient(host))
            .addConverterFactory(EnumConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CoinGeckoService::class.java)
    }

    @Provides
    fun provideCurrencyService(): CurrencyService {
        val host = "currency-converter-by-api-ninjas.p.rapidapi.com"
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://$host/v1/")
            .client(getRapidApiOkHttpClient(host))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CurrencyService::class.java)
    }

    private fun getRapidApiOkHttpClient(apiHost: String) = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val userAgent = "${BuildConfig.APP_NAME}/${BuildConfig.VERSION_NAME} " +
                "(Android ${Build.VERSION.RELEASE}; ${Build.MANUFACTURER} ${Build.MODEL}; API ${Build.VERSION.SDK_INT})"
            val request = chain.request().newBuilder()
                .header("x-rapidapi-key", RAPID_API_KEY)
                .header("x-rapidapi-host", apiHost)
                .header("User-Agent", userAgent)
                .header("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()
}
