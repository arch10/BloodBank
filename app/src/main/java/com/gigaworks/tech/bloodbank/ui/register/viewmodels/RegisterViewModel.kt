package com.gigaworks.tech.bloodbank.ui.register.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigaworks.tech.bloodbank.util.OTP_TIMER
import com.gigaworks.tech.bloodbank.util.OtpTimer
import com.gigaworks.tech.bloodbank.util.logD
import com.gigaworks.tech.bloodbank.util.logE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private var verificationId: String? = null
    private var forceResendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var resendBackOff = 0
    private lateinit var timer: OtpTimer
    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String>
        get() = _timerText
    private val _timerFinished = MutableLiveData(false)
    val timerFinished: LiveData<Boolean>
        get() = _timerFinished
    private val _hasUserDetails = MutableLiveData<Boolean>()
    val hasUserDetails: LiveData<Boolean>
        get() = _hasUserDetails
    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String>
        get() = _loginError

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    init {
        startTimer()
    }

    fun startTimer() {
        timer = getNewTimer()
        timer.start()
    }

    fun getBackOffCount(): Int {
        return resendBackOff
    }

    private fun cancelTimer() {
        if (::timer.isInitialized) {
            timer.cancel()
        }
    }

    private fun getNewTimer(): OtpTimer {
        if (::timer.isInitialized) {
            timer.cancel()
        }
        return OtpTimer(
            secondsInFuture = OTP_TIMER + (resendBackOff * OTP_TIMER),
            onTimerTick = { min, sec ->
                val stringFormat = "$min : $sec"
                _timerText.value = stringFormat
            },
            onTimerFinish = {
                resendBackOff++
                _timerFinished.value = true
            })
    }

    private fun checkUserInfo(userId: String?) {
        viewModelScope.launch {
            delay(2000)
            //TODO("add backend logic to check if user has provided all the data")
            //defaulting to false for now for testing.
            logD("checkUserInfo: Got user info.")
            _hasUserDetails.value = true
        }
    }

    fun verifyOtp(otp: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
        signInWithPhoneCredential(credential)
    }

    fun signInWithPhoneCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    logD("signInWithPhoneCredential: Phone login success")
                    //stop the otp timer
                    cancelTimer()
                    //check if we have user's details.
                    checkUserInfo(task.result.user?.uid)
                } else {
                    logE("signInWithPhoneCredential: ${task.exception?.message}")
                    _loginError.value = "Invalid OTP"
                }
            }
    }

    fun setOtpResendParams(
        verificationId: String,
        forceResendToken: PhoneAuthProvider.ForceResendingToken
    ) {
        this.verificationId = verificationId
        this.forceResendToken = forceResendToken
    }

    fun getResendToken(): PhoneAuthProvider.ForceResendingToken? {
        return forceResendToken
    }

    override fun onCleared() {
        super.onCleared()
        cancelTimer()
    }
}