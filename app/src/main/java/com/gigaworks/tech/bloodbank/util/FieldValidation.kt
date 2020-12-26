package com.gigaworks.tech.bloodbank.util

import androidx.core.text.isDigitsOnly

object FieldValidation {
    fun validatePhoneNumber(phoneNumber: String?): Boolean {
        if (!phoneNumber.isNullOrBlank() && phoneNumber.isDigitsOnly() && phoneNumber.length == 10) {
            return true
        }
        return false
    }

    fun validateNumber(number: String?): Boolean {
        return !number.isNullOrBlank() && number.isDigitsOnly()
    }

    fun validateString(string: String?, min: Int = 0, max: Int = string?.length ?: 0): Boolean {
        return !string.isNullOrBlank() && string.length >= min && string.length <= max
    }
}