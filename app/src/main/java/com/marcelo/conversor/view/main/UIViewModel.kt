package com.marcelo.conversor.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.marcelo.conversor.model.Event
import com.marcelo.conversor.repository.CoinConverterRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UIViewModel @Inject constructor(
    private val repository: CoinConverterRepository
) : ViewModel() {

    private val _hasLoadInProgress = MutableLiveData<Event<Boolean>>()
    val hasLoadInProgress: LiveData<Event<Boolean>>
        get() = _hasLoadInProgress

    private val _onUpdateDataResponse = MutableLiveData<Event<Boolean>>()
    val onUpdateDataResponse: LiveData<Event<Boolean>>
        get() = _onUpdateDataResponse

    fun updateData() {
        Timber.d("Updating data...")
        _hasLoadInProgress.value = Event(true)
        viewModelScope.launch {
            val success = repository.updatedDataFromNetwork()
            _onUpdateDataResponse.value = Event(success)
            _hasLoadInProgress.value = Event(false)
        }
    }
}
