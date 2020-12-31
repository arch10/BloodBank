package com.gigaworks.tech.bloodbank.ui.getdetails.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gigaworks.tech.bloodbank.R
import com.gigaworks.tech.bloodbank.databinding.FragmentRegisterLocationBinding
import com.gigaworks.tech.bloodbank.network.Resource
import com.gigaworks.tech.bloodbank.ui.base.BaseFragment
import com.gigaworks.tech.bloodbank.ui.getdetails.viewmodels.GetDetailsViewModel
import com.gigaworks.tech.bloodbank.ui.home.HomeActivity
import com.gigaworks.tech.bloodbank.util.*
import com.gigaworks.tech.bloodbank.util.logD
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment : BaseFragment<FragmentRegisterLocationBinding>() {
    private val viewModel by activityViewModels<GetDetailsViewModel>()

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
        viewModel.loading.observe(viewLifecycleOwner, {
            binding.loaderView.loaderOverlay.visible(it)
        })

        viewModel.user.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    startActivity(Intent(activity, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }
                is Resource.Failure -> {
                    if(it.isNetworkError) {
                        logD("userObserve: ${it.message}")
                        Snackbar.make(
                            binding.root,
                            "Please check the internet connection",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        logE("userObserve: ${it.message}")
                        Snackbar.make(
                            binding.root,
                            "Cannot save user details. Try again later",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })

        viewModel.loginError.observe(viewLifecycleOwner, { loginError ->
            if (loginError != "") {
                Snackbar.make(
                    binding.root,
                    loginError,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setUpView() {
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