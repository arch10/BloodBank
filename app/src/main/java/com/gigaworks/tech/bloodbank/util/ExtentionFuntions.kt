package com.gigaworks.tech.bloodbank.util

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
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

fun ViewModel.getClassName():String {
    return this.javaClass.simpleName
}

fun ViewModel.logD(msg: String?) {
    printLogD(this.getClassName(), msg)
}

fun ViewModel.logE(msg: String?) {
    printLogE(this.getClassName(), msg)
}

fun Activity.getClassName():String {
    return this.javaClass.simpleName
}

fun Activity.logD(msg: String?) {
    printLogD(this.getClassName(), msg)
}

fun Activity.logE(msg: String?) {
    printLogE(this.getClassName(), msg)
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}