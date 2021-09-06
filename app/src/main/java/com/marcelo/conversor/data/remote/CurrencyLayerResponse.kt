package com.marcelo.conversor.data.remote

import com.marcelo.conversor.data.local.DatabaseCurrency
import com.marcelo.conversor.data.local.DatabaseExchangeRate

data class Currencies(
    val success: Boolean,
    val currencies: Map<String, String>
)

data class Quotes(
    val success: Boolean,
    val timestamp: Long,
    val source: String,
    val quotes: Map<String, Double>
)

fun Currencies.asDatabaseCurrency(): List<DatabaseCurrency> {
    return currencies.map {
        DatabaseCurrency(it.key, it.value)
    }
}

fun Quotes.asDatabaseExchangeRate(): List<DatabaseExchangeRate> {
    return quotes.map {
        DatabaseExchangeRate(it.key.take(3), it.key.takeLast(3), it.value)
    }
}