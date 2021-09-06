package com.marcelo.conversor.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import com.marcelo.conversor.R
import com.marcelo.conversor.adapters.CurrenciesListAdapter
import com.marcelo.conversor.databinding.FragmentListCurrenciesBinding
import com.marcelo.conversor.ui.viewmodels.CurrenciesViewModel

@AndroidEntryPoint
class ListCurrenciesFragment : Fragment()
{
    private val viewModel: CurrenciesViewModel by viewModels()
    private val adapter = CurrenciesListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentListCurrenciesBinding.inflate(inflater)

        binding.recyclerviewListCurrencies.adapter = adapter
        binding.recyclerviewListCurrencies.layoutManager = LinearLayoutManager(requireContext())

        viewModel.currencies.observe(viewLifecycleOwner, {
            adapter.currencies = it
        })

        this.settingsFieldName(binding)
        this.settingsFieldCode(binding)
        this.clickReturnButton(binding)

        return binding.root
    }

    private fun clickReturnButton(binding: FragmentListCurrenciesBinding) {
        binding.returnButton.setOnClickListener {
            findNavController().navigate(ListCurrenciesFragmentDirections.actionPreviousFragmentToConvertFragment())
        }
    }

    private fun settingsFieldCode(binding: FragmentListCurrenciesBinding) {
        binding.chipCodeCurrency.setOnClickListener {
            adapter.orderByCode = true
            adapter.filter.filter("")
            binding.chipNameCurrency.isChecked = false
        }
    }

    private fun settingsFieldName(binding: FragmentListCurrenciesBinding) {
        binding.chipNameCurrency.setOnClickListener {
            adapter.orderByName = true
            adapter.filter.filter("")
            binding.chipCodeCurrency.isChecked = false
        }
    }
}