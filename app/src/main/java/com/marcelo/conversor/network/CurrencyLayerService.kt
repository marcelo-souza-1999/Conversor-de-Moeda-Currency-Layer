package com.marcelo.conversor.network

import com.marcelo.conversor.network.model.CurrencyDTO
import com.marcelo.conversor.network.model.ExchangeDTO
import retrofit2.http.GET

interface CurrencyLayerService {
    @GET("/live")
    suspend fun getExchanges() : ExchangeDTO

    @GET("/list")
    suspend fun getCurrencies() : CurrencyDTO
}
