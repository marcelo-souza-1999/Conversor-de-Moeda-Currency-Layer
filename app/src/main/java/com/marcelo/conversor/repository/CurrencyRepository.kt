package com.marcelo.conversor.repository

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.marcelo.conversor.BuildConfig
import com.marcelo.conversor.data.local.CurrencyDao
import com.marcelo.conversor.data.local.asEntitieCurrency
import com.marcelo.conversor.data.local.asEntitieExchangeRate
import com.marcelo.conversor.data.remote.CurrencyLayerApi
import com.marcelo.conversor.data.remote.asDatabaseCurrency
import com.marcelo.conversor.data.remote.asDatabaseExchangeRate
import com.marcelo.conversor.entities.Currency
import com.marcelo.conversor.entities.ExchangeRate
import java.lang.Exception
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val currencyLayerApi: CurrencyLayerApi
) {

    fun getCurrencies(): Flow<List<Currency>> {
        val scope = CoroutineScope(Job() + Dispatchers.IO)
        scope.launch {
            refreshCurrencies()
        }

        return currencyDao.loadCurrencies().map { list ->
            list.asEntitieCurrency()
        }
    }

    private suspend fun refreshCurrencies() {
        withContext(Dispatchers.IO) {
            try {
                val responseCurrencies = currencyLayerApi.getCurrencies(BuildConfig.ACCESS_KEY)
                currencyDao.saveCurrencies(responseCurrencies.asDatabaseCurrency())
            } catch (e: Exception) {
                Log.d("CurrencyRepository", e.stackTraceToString())
            }
        }
    }

    fun getCurrenciesFromDatabase(): Flow<List<Currency>> {
        return currencyDao.loadCurrencies().map { list ->
            list.asEntitieCurrency()
        }
    }

    fun getExchangeRates(): Flow<List<ExchangeRate>> {
        val scope = CoroutineScope(Job() + Dispatchers.IO)
        scope.launch {
            refreshExchangeRates()
        }

        return currencyDao.loadExchangeRates().map { list ->
            list.asEntitieExchangeRate()
        }
    }

    private suspend fun refreshExchangeRates() {
        withContext(Dispatchers.IO) {
            try {
                val responseExchangeRates = currencyLayerApi.getRates(BuildConfig.ACCESS_KEY)
                currencyDao.saveExchangeRate(responseExchangeRates.asDatabaseExchangeRate())
            } catch (e: Exception) {
                Log.d("CurrencyRepository", e.stackTraceToString())
            }
        }
    }
}