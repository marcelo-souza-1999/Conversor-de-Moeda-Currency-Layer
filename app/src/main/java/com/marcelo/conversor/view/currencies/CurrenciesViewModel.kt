package com.marcelo.conversor.view.currencies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.marcelo.conversor.database.model.Currency
import com.marcelo.conversor.repository.CoinConverterRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val repository: CoinConverterRepository
) : ViewModel() {

    val search = MutableLiveData("")

    private val _currenciesList = MutableLiveData<List<Currency>>(emptyList())
    val currenciesList: LiveData<List<Currency>>
        get() = _currenciesList

    fun getAllElementsByName() = viewModelScope.launch {
        _currenciesList.value = repository.getAllCurrenciesOrderByName()
    }

    fun getAllElementsByCode() = viewModelScope.launch {
        _currenciesList.value = repository.getAllCurrenciesOrderByCode()
    }

    fun getSearchElementsByName(search: String) = viewModelScope.launch {
        _currenciesList.value = repository.getCurrenciesBySearchOrderByName(search)
    }

    fun getSearchElementsByCode(search: String) = viewModelScope.launch {
        _currenciesList.value = repository.getCurrenciesBySearchOrderByCode(search)
    }
}
