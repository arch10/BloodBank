package com.gigaworks.tech.bloodbank.ui.register.fragments

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
import com.gigaworks.tech.bloodbank.databinding.FragmentConfirmRegisterBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseFragment
import com.gigaworks.tech.bloodbank.ui.getdetails.fragments.SetNameFragment
import com.gigaworks.tech.bloodbank.ui.login.fragments.LoginConfirmFragment
import com.gigaworks.tech.bloodbank.ui.register.viewmodels.RegisterViewModel
import com.gigaworks.tech.bloodbank.util.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class ConfirmRegisterFragment : BaseFragment<FragmentConfirmRegisterBinding>() {
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

            binding.registerBtn.isEnabled = isEnabled
        }
    }
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val phoneNumber: String by lazy {
        arguments?.getString(LoginConfirmFragment.PHONE_NUMBER, "") ?: ""
    }
    private val viewModel by viewModels<RegisterViewModel>()
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

        viewModel.hasUserDetails.observe(viewLifecycleOwner, { hasDetails ->
            binding.loaderView.loaderOverlay.hide()
            if (!hasDetails) {
                val bundle = bundleOf(
                    SetNameFragment.PHONE_NUMBER to phoneNumber
                )
                findNavController().navigate(
                    R.id.action_confirmRegisterFragment_to_get_details_navigation,
                    bundle
                )
            } else {
                Snackbar.make(binding.root, "Has Details!!", Snackbar.LENGTH_SHORT).show()
            }
        })

        viewModel.loginError.observe(viewLifecycleOwner, {loginError->
            if(loginError != "") {
                binding.otpLayout.error = loginError
                binding.loaderView.loaderOverlay.hide()
            }
        })
        sendPhoneOtp(phoneNumber)
    }

    private fun handleBackPress() {
        dialog = dialogBuilder.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.otp.addTextChangedListener(errorWatcher)
        binding.registerBtn.setOnClickListener {
            hideKeyboard()
            binding.loaderView.loaderOverlay.show()
            viewModel.verifyOtp(binding.otp.text.toString())
        }
        binding.resendOtpBtn.setOnClickListener {
            disableResendBtn()
            viewModel.startTimer()
            val resendToken = viewModel.getResendToken()
            sendPhoneOtp(phoneNumber, resendToken)
        }
    }

    //function to send OTP to provided phone number
    private fun sendPhoneOtp(
        phoneNumber: String,
        resendToken: PhoneAuthProvider.ForceResendingToken? = null
    ) {
        val phone = "+91$phoneNumber"
        val builder = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setActivity(requireActivity())
            .setTimeout(OTP_TIMER, TimeUnit.SECONDS)
            .setCallbacks(callbacks)
        if (resendToken != null) {
            builder.setForceResendingToken(resendToken)
        }
        PhoneAuthProvider.verifyPhoneNumber(builder.build())
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            logD("onVerificationCompleted: Auto verification completed")
            binding.loaderView.loaderOverlay.show()
            viewModel.signInWithPhoneCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            logE("onVerificationFailed: ${e.message}")
            binding.loaderView.loaderOverlay.hide()
            //TODO("Implement different exceptions")
            //some error occurred, return the user to previous page
            //FirebaseTooManyRequestsException & FirebaseAuthInvalidCredentialsException
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            logD("onCodeSent: Code sent")
            viewModel.setOtpResendParams(verificationId, token)
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
    ) = FragmentConfirmRegisterBinding.inflate(inflater, container, false)

    companion object {
        const val PHONE_NUMBER = "PHONE_NUMBER"
    }
}