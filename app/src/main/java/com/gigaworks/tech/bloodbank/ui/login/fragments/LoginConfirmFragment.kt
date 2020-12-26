package com.gigaworks.tech.bloodbank.ui.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gigaworks.tech.bloodbank.R
import com.gigaworks.tech.bloodbank.databinding.FragmentConfirmLoginBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseFragment
import com.gigaworks.tech.bloodbank.ui.login.viewmodels.LoginConfirmViewModel
import com.gigaworks.tech.bloodbank.ui.register.fragments.SetNameFragment
import com.gigaworks.tech.bloodbank.util.FieldValidation
import com.gigaworks.tech.bloodbank.util.TextErrorWatcher
import com.gigaworks.tech.bloodbank.util.hideError
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginConfirmFragment : BaseFragment<FragmentConfirmLoginBinding>() {
    private val errorWatcher: TextErrorWatcher by lazy {
        TextErrorWatcher {
            val otp = binding.otp.text.toString()
            binding.otpLayout.hideError()
            val isEnabled = FieldValidation.validateString(otp, min = 6, max = 6)
            binding.otpLayout.apply {
                endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.check_circle)
                isEndIconVisible = isEnabled
            }

            binding.loginBtn.isEnabled = isEnabled
        }
    }
    private val phoneNumber: String by lazy {
        arguments?.getString(PHONE_NUMBER, "") ?: ""
    }
    private val viewModel by viewModels<LoginConfirmViewModel>()
    private val dialogBuilder: MaterialAlertDialogBuilder by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Discard OTP verification ?")
            .setMessage("Do you want to discard the OTP verification ?")
            .setCancelable(false)
            .setPositiveButton("Discard") { dialog, _ ->
                findNavController().navigateUp()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
    }
    private var dialog: AlertDialog? = null

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
        setActionBar(binding.toolbar, onBackIconClick = {
            handleBackPress()
        })
        disableResendBtn()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handleBackPress()
                }
            })
    }

    private fun setUpObservables() {
        viewModel.timerFinished.observe(viewLifecycleOwner, { isFinished ->
            if (isFinished) {
                enableResendBtn()
            }
        })

        viewModel.timerText.observe(viewLifecycleOwner, { timerText ->
            binding.otpText.text = timerText
        })
    }

    private fun handleBackPress() {
        dialog = dialogBuilder.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.otp.addTextChangedListener(errorWatcher)
        binding.loginBtn.setOnClickListener {
            hideKeyboard()
//            binding.otpLayout.showError("OTP not valid")
            //check if
            val bundle = bundleOf(
                SetNameFragment.PHONE_NUMBER to phoneNumber
            )
            findNavController().navigate(
                R.id.action_loginConfirmFragment_to_get_details_navigation,
                bundle
            )
        }
        binding.resendOtpBtn.setOnClickListener {
            disableResendBtn()
            viewModel.startTimer()
        }
    }

    private fun enableResendBtn() {
        if (shouldEnableResend()) {
            binding.resendOtpBtn.apply {
                isEnabled = true
                setTextColor(ContextCompat.getColorStateList(context, R.color.secondaryColor))
            }
        }
    }

    private fun disableResendBtn() {
        binding.resendOtpBtn.apply {
            isEnabled = false
            setTextColor(ContextCompat.getColorStateList(context, R.color.disabledColor))
        }
    }

    private fun shouldEnableResend(): Boolean {
        return viewModel.getBackOffCount() <= 2
    }

    override fun onDetach() {
        dialog?.dismiss()
        super.onDetach()
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentConfirmLoginBinding.inflate(inflater, container, false)

    companion object {
        const val PHONE_NUMBER = "PHONE_NUMBER"
    }
}