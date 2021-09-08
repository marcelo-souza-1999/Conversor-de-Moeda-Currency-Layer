package com.marcelo.conversor.network.model

data class CurrencyDTO(
    val success: Boolean,
    val currencies: Map<String, String>
)
