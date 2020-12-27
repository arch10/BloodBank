package com.gigaworks.tech.bloodbank.util

import android.util.Log
import com.gigaworks.tech.bloodbank.BuildConfig.DEBUG
import com.google.firebase.crashlytics.FirebaseCrashlytics

fun printLogD(className: String, message: String?) {
    if (DEBUG) {
        Log.d(TAG, "$className: $message")
    }
}

fun printLogE(className: String, message: String?) {
    if (DEBUG) {
        Log.e(TAG, "$className: $message")
    } else {
        message?.let {
            FirebaseCrashlytics.getInstance().log("$className: $it")
        }
    }
}