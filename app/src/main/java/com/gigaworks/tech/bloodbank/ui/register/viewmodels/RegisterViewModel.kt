package com.gigaworks.tech.bloodbank.ui.register.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigaworks.tech.bloodbank.domain.model.User
import com.gigaworks.tech.bloodbank.network.Resource
import com.gigaworks.tech.bloodbank.network.bearer
import com.gigaworks.tech.bloodbank.repository.UserRepository
import com.gigaworks.tech.bloodbank.util.OTP_TIMER
import com.gigaworks.tech.bloodbank.util.OtpTimer
import com.gigaworks.tech.bloodbank.util.logD
import com.gigaworks.tech.bloodbank.util.logE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.launch

class RegisterViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
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
    private val _user = MutableLiveData<Resource<User>>()
    val user: LiveData<Resource<User>>
        get() = _user
    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String>
        get() = _loginError
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading
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

    private fun checkUserInfo(token: String, uid: String) {
        viewModelScope.launch {
            _user.value = userRepository.getUserCache(bearer(token), uid)
            _loading.value = false
        }
    }

    private fun generateUserToken(firebaseUser: FirebaseUser) {
        firebaseUser.getIdToken(false).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                checkUserInfo(token = task.result.token!!, firebaseUser.uid)
            } else {
                logE("generateUserToken: ${task.exception?.message}")
                _loginError.value = "Unknown Error"
                _loading.value = false
            }
        }
    }

    fun verifyOtp(otp: String) {
        if (verificationId != null) {
            _loading.value = true
            val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
            signInWithPhoneCredential(credential)
        }
    }

    fun signInWithPhoneCredential(credential: PhoneAuthCredential) {
        _loading.value = true
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    logD("signInWithPhoneCredential: Phone login success")
                    //stop the otp timer
                    cancelTimer()
                    //check if we have user's details.
                    generateUserToken(task.result.user!!)
                } else {
                    logE("signInWithPhoneCredential: ${task.exception?.message}")
                    _loginError.value = "Invalid OTP"
                    _loading.value = false
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