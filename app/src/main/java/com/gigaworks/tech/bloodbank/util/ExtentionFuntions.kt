package com.gigaworks.tech.bloodbank.util

import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.showError(errorString: String) {
    this.error = errorString
}

fun TextInputLayout.hideError() {
    this.error = null
}

fun Fragment.getClassName(): String {
    return this.javaClass.simpleName
}

fun Fragment.logD(msg: String?) {
    printLogD(this.getClassName(), msg)
}

fun Fragment.logE(msg: String?) {
    printLogE(this.getClassName(), msg)
}