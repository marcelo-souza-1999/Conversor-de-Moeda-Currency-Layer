package com.marcelo.conversor.view.currencies

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import com.marcelo.conversor.R
import com.marcelo.conversor.databinding.FragmentCurrenciesBinding
import com.marcelo.conversor.model.closeKeyboard
import com.marcelo.conversor.model.distinctUntilChanged
import com.marcelo.conversor.model.removeOldEvents
import com.marcelo.conversor.view.adapters.CurrencyAdapter

@AndroidEntryPoint
class CurrenciesFragment : Fragment(){

    private lateinit var binding: FragmentCurrenciesBinding
    private val currenciesViewModel: CurrenciesViewModel by viewModels()
    private val handler = Handler(Looper.myLooper()!!)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_currencies, container, false)

        binding.currenciesViewModel = currenciesViewModel

        val adapter = CurrencyAdapter()
        binding.recyclerviewCurrencies.adapter = adapter

        currenciesViewModel.search.observe(viewLifecycleOwner, { searchText ->
            if (searchText.isEmpty())
                handleWithAllCurrencies()
            else {
                handler.removeOldEvents()
                // Schedule to make a search in 2 seconds
                handler.postDelayed({ handleWithSearchedCurrencies(searchText) }, 2000)
            }
        })

        currenciesViewModel.currenciesList.distinctUntilChanged().observe(viewLifecycleOwner, { list ->
            handleWithListVisibility(list.isEmpty())
            if (list.isNotEmpty())
                adapter.submitList(list)
        })

        binding.radioGroup.setOnCheckedChangeListener { _, _ ->
            if (currenciesViewModel.search.value!!.isEmpty())
                handleWithAllCurrencies()
            else
                handleWithSearchedCurrencies(currenciesViewModel.search.value!!)
        }

        binding.lifecycleOwner = this
        return binding.root
    }

    private fun handleWithAllCurrencies() {
        if (binding.radioButtonOrderByCode.isChecked)
            currenciesViewModel.getAllElementsByCode()
        else
            currenciesViewModel.getAllElementsByName()
    }

    private fun handleWithSearchedCurrencies(search: String) {
        binding.root.closeKeyboard()
        if (binding.radioButtonOrderByCode.isChecked)
            currenciesViewModel.getSearchElementsByCode(search)
        else
            currenciesViewModel.getSearchElementsByName(search)
    }

    private fun handleWithListVisibility(isEmpty: Boolean) {
        if (isEmpty) {
            binding.labelError.visibility = View.VISIBLE
            binding.recyclerviewCurrencies.visibility = View.GONE
        }
        else {
            binding.labelError.visibility = View.GONE
            binding.recyclerviewCurrencies.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        handler.removeOldEvents()
        super.onDestroy()
    }
}
