package com.marcelo.conversor.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marcelo.conversor.database.dao.CurrencyDAO
import com.marcelo.conversor.database.dao.ExchangeDAO
import com.marcelo.conversor.database.model.Currency
import com.marcelo.conversor.database.model.Exchange

@Database(entities = [Currency::class, Exchange::class], version = 1, exportSchema = true)
abstract class CoinConverterDatabase : RoomDatabase() {
    abstract val currencyDAO: CurrencyDAO
    abstract val exchangeDAO: ExchangeDAO
}
