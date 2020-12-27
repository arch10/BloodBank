package com.gigaworks.tech.bloodbank.ui.getdetails.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gigaworks.tech.bloodbank.R
import com.gigaworks.tech.bloodbank.databinding.FragmentSetNameBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseFragment
import com.gigaworks.tech.bloodbank.ui.getdetails.viewmodels.GetDetailsViewModel
import com.gigaworks.tech.bloodbank.util.FieldValidation
import com.gigaworks.tech.bloodbank.util.TextErrorWatcher
import com.gigaworks.tech.bloodbank.util.hideError

class SetNameFragment : BaseFragment<FragmentSetNameBinding>() {
    private val errorWatcher: TextErrorWatcher by lazy {
        TextErrorWatcher {
            val fName = binding.fName.text.toString()
            val lName = binding.lName.text.toString()
            binding.fNameLayout.hideError()
            binding.lNameLayout.hideError()
            val isEnabled =
                FieldValidation.validateString(fName, min = 1) && FieldValidation.validateString(
                    lName,
                    min = 1
                )
            binding.nextBtn.isEnabled = isEnabled
        }
    }
    private val phoneNumber: String by lazy {
        arguments?.getString(PHONE_NUMBER, "") ?: ""
    }
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

    private fun setUpView() {
        setStatusBarColor(STATUS_BAR_TRANSPARENT)
        setActionBar(binding.toolbar)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handleBackPress()
                }
            })
        viewModel.phoneNumber = phoneNumber
    }

    private fun setUpObservables() {
    }

    private fun handleBackPress() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fName.addTextChangedListener(errorWatcher)
        binding.lName.addTextChangedListener(errorWatcher)
        binding.nextBtn.setOnClickListener {
            hideKeyboard()
            viewModel.fName = binding.fName.text.toString()
            viewModel.lName = binding.lName.text.toString()
            findNavController().navigate(R.id.action_setNameFragment_to_basicDetailsFragment)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSetNameBinding.inflate(inflater, container, false)

    companion object {
        const val PHONE_NUMBER = "PHONE_NUMBER"
    }
}