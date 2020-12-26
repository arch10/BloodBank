package com.gigaworks.tech.bloodbank.util

import android.util.Log
import com.gigaworks.tech.bloodbank.BuildConfig.DEBUG

fun printLogD(className: String?, message: String?) {
    if (DEBUG) {
        Log.d(TAG, "$className: $message")
    }
}