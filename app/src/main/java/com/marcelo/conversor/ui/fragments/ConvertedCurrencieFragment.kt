package com.marcelo.conversor.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.marcelo.conversor.databinding.FragmentConvertCurrenciesBinding
import com.marcelo.conversor.ui.viewmodels.ConverterViewModel
import java.text.DecimalFormat

@AndroidEntryPoint
class ConvertedCurrencieFragment : Fragment() {
    private val viewModel: ConverterViewModel by viewModels()
    private val decimalFormat = DecimalFormat("#,###.##")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentConvertCurrenciesBinding.inflate(inflater)

        this.showCurrencies(binding)
        this.formatCurrencie(binding)
        this.showSpinnerCurrenciesOrigin(binding)
        this.showSpinnerCurrenciesDestiny(binding)
        this.clickButtons(binding)
        this.convertCurrencie(binding)
        this.changedListenerField(binding)
        this.checkErrorsFields()
        this.openFragmentListCurrencies(binding)


        return binding.root
    }

    private fun showCurrencies(binding: FragmentConvertCurrenciesBinding) {
        viewModel.currencies.observe(viewLifecycleOwner,
            {
                var selectedPositionOrigin = 0
                var selectedPositionDestiny = 0

                val spinnerItens = it.mapIndexed { position, view ->
                    if (view.code == "BRL") {
                        selectedPositionDestiny = position
                    }
                    if (view.code == "USD") {
                        selectedPositionOrigin = position
                    }
                    "${view.code} - ${view.name}"
                }

                val spinnerAdapterCurrencies: ArrayAdapter<String> =
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        spinnerItens
                    )

                binding.spinnerCurrencyOrigin.adapter = spinnerAdapterCurrencies
                binding.spinnerCurrencyDestiny.adapter = spinnerAdapterCurrencies

                binding.spinnerCurrencyOrigin.setSelection(selectedPositionOrigin)
                binding.spinnerCurrencyDestiny.setSelection(selectedPositionDestiny)
            })
    }

    private fun convertCurrencie(binding: FragmentConvertCurrenciesBinding)
    {
        binding.editTextValueCurrency.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                callConvertCurrencyValue(binding)
                return@OnKeyListener false
            }
            false
        })
    }

    private fun showSpinnerCurrenciesOrigin(binding: FragmentConvertCurrenciesBinding) {
        binding.spinnerCurrencyOrigin.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                binding.textViewGetCurrencyOrigin.text = selectedItem.take(3)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun showSpinnerCurrenciesDestiny(binding: FragmentConvertCurrenciesBinding) {
        binding.spinnerCurrencyDestiny.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                binding.textViewGetCurrencyDestiny.text = selectedItem.take(3)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun formatCurrencie(binding: FragmentConvertCurrenciesBinding) {
        viewModel.convertedValue.observe(viewLifecycleOwner,
            {
                binding.editTextValueConverted.setText(decimalFormat.format(it))
            })
    }

    private fun clickButtons(binding: FragmentConvertCurrenciesBinding) {
        binding.buttonConvertCurrency.setOnClickListener {
            callConvertCurrencyValue(binding)
        }
    }

    private fun changedListenerField(binding: FragmentConvertCurrenciesBinding) {
        binding.editTextValueCurrency.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(string: Editable) {
            }

            override fun beforeTextChanged(
                string: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(string: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.editTextValueConverted.length() != 0) {
                    binding.editTextValueConverted.setText("")
                }
            }
        })
    }

    private fun openFragmentListCurrencies(binding: FragmentConvertCurrenciesBinding) {
        binding.buttonListCurrency.setOnClickListener {
            findNavController().navigate(ConvertedCurrencieFragmentDirections.actionConverterFragmentToCurrenciesFragment())
        }
    }

    private fun checkErrorsFields() {
        viewModel.inputError.observe(viewLifecycleOwner,
            {
                if (it != null) {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    viewModel.displayInputErrorComplete()
                }
            })
    }

    private fun callConvertCurrencyValue(binding: FragmentConvertCurrenciesBinding) {
        val orig = binding.spinnerCurrencyOrigin.selectedItem.toString().take(3)
        val dest = binding.spinnerCurrencyDestiny.selectedItem.toString().take(3)
        val value = binding.editTextValueCurrency.text.toString()
        viewModel.convert(orig, dest, value)
    }
}