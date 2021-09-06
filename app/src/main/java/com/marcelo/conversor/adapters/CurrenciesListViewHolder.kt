package com.marcelo.conversor.adapters

import androidx.recyclerview.widget.RecyclerView
import com.marcelo.conversor.databinding.ListGetcurrenciesBinding
import com.marcelo.conversor.entities.Currency

class CurrenciesListViewHolder(
    private var binding: ListGetcurrenciesBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(currency: Currency) {
        binding.currenciesCode.text = currency.code
        binding.currenciesNameCountry.text = currency.name
    }
}