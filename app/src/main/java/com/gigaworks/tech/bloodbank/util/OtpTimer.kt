package com.gigaworks.tech.bloodbank.util

import android.os.CountDownTimer


class OtpTimer(
    secondsInFuture: Long = 60L,
    countDownInterval: Long = 1L,
    private val onTimerTick: (minutes: String, seconds: String) -> Unit,
    private val onTimerFinish: () -> Unit = {}
) : CountDownTimer(secondsInFuture * 1000, countDownInterval * 1000) {
    override fun onTick(millisUntilFinished: Long) {
        val min = millisUntilFinished.toFloat() / 60000
        val x = min - min.toLong()
        val sec = (x * 60).toLong()
        var seconds = sec.toString()
        var minutes = min.toLong().toString()
        if (seconds.length == 1) {
            seconds = "0$seconds"
        }
        if (minutes.length == 1) {
            minutes = "0$minutes"
        }
        onTimerTick(minutes, seconds)
    }

    override fun onFinish() {
        onTimerFinish()
    }
}
