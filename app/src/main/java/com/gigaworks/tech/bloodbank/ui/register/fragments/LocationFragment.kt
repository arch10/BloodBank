package com.gigaworks.tech.bloodbank.ui.register.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gigaworks.tech.bloodbank.databinding.FragmentRegisterLocationBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseFragment
import com.gigaworks.tech.bloodbank.ui.register.viewmodels.RegisterViewModel
import com.gigaworks.tech.bloodbank.util.CITIES_LIST
import com.gigaworks.tech.bloodbank.util.FieldValidation
import com.google.android.material.snackbar.Snackbar

class LocationFragment : BaseFragment<FragmentRegisterLocationBinding>() {
    private val viewModel by activityViewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        setUpView()
        setUpObservables()
        return binding.root
    }

    private fun setUpObservables() {
    }

    private fun setUpView() {
        setStatusBarColor(STATUS_BAR_TRANSPARENT)
        setActionBar(binding.toolbar, onBackIconClick = {
            handleBackPress()
        })
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handleBackPress()
                }
            })
        //populate drop downs
        populateDropDowns()
        //set values if any
        setValues()
    }

    private fun setValues() {
        val greeting = "Hey, ${viewModel.fName}"
        binding.title.text = greeting
    }

    private fun populateDropDowns() {
        val citiesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            CITIES_LIST
        )
        binding.city.setAdapter(citiesAdapter)
    }

    private fun handleBackPress() {
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.doneBtn.setOnClickListener {
            if (isValidForm()) {
                hideKeyboard()
                viewModel.state = binding.state.text.toString()
                viewModel.city = binding.city.text.toString()
                viewModel.saveUser()
                Snackbar.make(view, "Yayy!!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidForm(): Boolean {
        return isValidState() && isValidCity()
    }

    private fun isValidState(): Boolean {
        val state = binding.state.text.toString()
        val isValid = FieldValidation.validateString(state, min = 2)
        if (!isValid) {
            binding.stateLayout.error = "Enter a valid state"
        } else {
            binding.stateLayout.error = null
        }
        return isValid
    }

    private fun isValidCity(): Boolean {
        val city = binding.city.text.toString()
        val isValid = FieldValidation.validateString(city, min = 2)
        if (!isValid) {
            binding.cityLayout.error = "Enter a valid city"
        } else {
            binding.cityLayout.error = null
        }
        return isValid
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterLocationBinding.inflate(inflater, container, false)
}