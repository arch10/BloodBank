package com.gigaworks.tech.bloodbank.ui.login.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gigaworks.tech.bloodbank.util.OTP_TIMER
import com.gigaworks.tech.bloodbank.util.OtpTimer

class LoginConfirmViewModel : ViewModel() {
    private var resendBackOff = 0
    private lateinit var timer: OtpTimer
    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String>
        get() = _timerText
    private val _timerFinished = MutableLiveData(false)
    val timerFinished: LiveData<Boolean>
        get() = _timerFinished

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

    fun cancelTimer() {
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

    override fun onCleared() {
        super.onCleared()
        cancelTimer()
    }
}