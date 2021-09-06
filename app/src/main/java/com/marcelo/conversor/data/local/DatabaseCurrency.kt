package com.marcelo.conversor.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marcelo.conversor.entities.Currency
import com.marcelo.conversor.entities.ExchangeRate

@Entity
data class DatabaseCurrency(
    @PrimaryKey
    val code: String,
    val name: String
)

@Entity
data class DatabaseExchangeRate(
    val pairFrom: String,
    @PrimaryKey
    val pairTo: String,
    val conversionRate: Double
)

fun List<DatabaseCurrency>.asEntitieCurrency(): List<Currency> {
    return map {
        Currency(
            code = it.code,
            name = it.name
        )
    }
}

fun List<DatabaseExchangeRate>.asEntitieExchangeRate(): List<ExchangeRate> {
    return map {
        ExchangeRate(
            pairFrom = it.pairFrom,
            pairTo = it.pairTo,
            conversionRate = it.conversionRate
        )
    }
}