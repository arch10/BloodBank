package com.gigaworks.tech.bloodbank.util

import com.google.android.material.datepicker.CalendarConstraints
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class DayValidator : CalendarConstraints.DateValidator {
    override fun isValid(date: Long): Boolean {
        val calender = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val currentDate = calender.timeInMillis
        return date < currentDate
    }
}