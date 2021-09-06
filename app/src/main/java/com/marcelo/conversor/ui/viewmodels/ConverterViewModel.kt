package com.marcelo.conversor.ui.viewmodels

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.marcelo.conversor.entities.Currency
import com.marcelo.conversor.entities.ExchangeRate
import com.marcelo.conversor.repository.CurrencyRepository
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(private val repository: CurrencyRepository) :
    ViewModel() {

    private var getCurrencies = MutableLiveData<List<Currency>>()
    val currencies: LiveData<List<Currency>>
        get() = getCurrencies

    private var exchangeRates = MutableLiveData<List<ExchangeRate>>()

    private var getValueConverted = MutableLiveData<Double>()
    val convertedValue: LiveData<Double>
        get() = getValueConverted

    private var getInputError = MutableLiveData<String?>()
    val inputError: LiveData<String?>
        get() = getInputError

    init {
        getCurrencies = repository.getCurrencies().asLiveData() as MutableLiveData<List<Currency>>
        //getCurrencies = repository.getCurrencies().asLiveData() as MutableLiveData<List<Currency>>

        viewModelScope.launch {
            repository.getExchangeRates().collect { rates ->
                if (rates.isEmpty())
                {
                    Log.d("ExchangeRatesEmpty", "=Rates not is Empty")
                }
                else {
                    exchangeRates.value = rates
                }
            }
        }
    }

    fun convert(origin: String, destination: String, value: String) {
        if (origin.isEmpty() || destination.isEmpty()) {
            getInputError.value = "Escolha uma moeda de origem"
            return
        }

        if (origin == destination) {
            getInputError.value = "As Moedas não podem ser iguais"
            return
        }

        if (value.isEmpty()) {
            getInputError.value = "Insira um valor para ser convertido"
            return
        } else {
            if (value.toFloat() == 0F || value.toFloat() < 0F) {
                getInputError.value = "Insira um valor maior que zero"
                return
            }
        }

        when {
            origin == "USD" -> {
                val exchangeRate = exchangeRates.value!!.find { it.pairTo == destination }
                getValueConverted.value = value.toDouble() * exchangeRate!!.conversionRate
            }
            destination == "USD" -> {
                val exchangeRate = exchangeRates.value!!.find { it.pairTo == origin }
                getValueConverted.value = value.toDouble() / exchangeRate!!.conversionRate
            }
            else -> {
                val exchangeRate = exchangeRates.value!!.find { it.pairTo == origin }
                val originInUsd = value.toDouble() / exchangeRate!!.conversionRate
                val exchangeRateDest = exchangeRates.value!!.find { it.pairTo == destination }
                getValueConverted.value = originInUsd * exchangeRateDest!!.conversionRate
            }
        }
    }

    fun displayInputErrorComplete() {
        getInputError.value = null
    }
}