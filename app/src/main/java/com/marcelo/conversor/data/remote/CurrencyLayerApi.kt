package com.marcelo.conversor.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyLayerApi {

    @GET("/list")
    suspend fun getCurrencies(@Query("access_key") access_key: String): Currencies

    @GET("/live")
    suspend fun getRates(@Query("access_key") access_key: String): Quotes
}