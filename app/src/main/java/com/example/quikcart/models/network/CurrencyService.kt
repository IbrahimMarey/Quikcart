package com.example.quikcart.models.network

import com.example.quikcart.BuildConfig
import com.example.quikcart.models.entities.CurrencyModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
private const val CURRENCY_URL=BuildConfig.CURRENCY_URL
interface CurrencyService{
    @GET("EGP")
    suspend fun getCurrency():CurrencyModel
}

object CurrencyHelper{
    val instance = Retrofit.Builder()
        .baseUrl(CURRENCY_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val currencyService : CurrencyService by lazy {
        instance.create(CurrencyService::class.java)
    }
}