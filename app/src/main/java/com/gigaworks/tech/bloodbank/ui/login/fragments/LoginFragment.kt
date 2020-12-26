package com.gigaworks.tech.bloodbank.ui.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.gigaworks.tech.bloodbank.R
import com.gigaworks.tech.bloodbank.databinding.FragmentLoginBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseFragment
import com.gigaworks.tech.bloodbank.ui.login.fragments.LoginConfirmFragment.Companion.PHONE_NUMBER
import com.gigaworks.tech.bloodbank.util.FieldValidation
import com.gigaworks.tech.bloodbank.util.TextErrorWatcher
import com.gigaworks.tech.bloodbank.util.hideError
import com.gigaworks.tech.bloodbank.util.showError

class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val errorWatcher: TextErrorWatcher by lazy {
        TextErrorWatcher {
            val phoneNumber = binding.phoneNumber.text.toString()
            binding.phoneLayout.hideError()
            binding.loginBtn.isEnabled = FieldValidation.validatePhoneNumber(phoneNumber)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        setUpView()
        return binding.root
    }

    private fun setUpView() {
        setStatusBarColor(STATUS_BAR_TRANSPARENT)
        setActionBar(binding.toolbar, onBackIconClick = {
            requireActivity().finish()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.phoneNumber.addTextChangedListener(errorWatcher)
        binding.loginBtn.setOnClickListener {
            if (isValidForm()) {
                //Goto next Page
                hideKeyboard()
                val bundle = bundleOf(
                    PHONE_NUMBER to binding.phoneNumber.text.toString().trim()
                )
                findNavController().navigate(
                    R.id.action_loginFragment_to_loginConfirmFragment,
                    bundle
                )
            }
        }
    }


    private fun isValidForm(): Boolean {
        return isValidPhoneNumber()
    }

    private fun isValidPhoneNumber(): Boolean {
        val phoneNumber = binding.phoneNumber.text.toString()
        val isValid = FieldValidation.validatePhoneNumber(phoneNumber)
        if (!isValid) {
            binding.phoneLayout.showError(getString(R.string.invalid_phone_number))
        }
        return isValid
    }


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)
}