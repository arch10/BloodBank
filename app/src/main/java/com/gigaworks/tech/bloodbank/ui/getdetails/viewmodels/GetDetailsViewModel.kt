package com.gigaworks.tech.bloodbank.ui.getdetails.viewmodels

import androidx.lifecycle.ViewModel
import com.gigaworks.tech.bloodbank.domain.model.User
import com.gigaworks.tech.bloodbank.util.BloodType
import com.gigaworks.tech.bloodbank.util.Gender

class GetDetailsViewModel : ViewModel() {
    var fName: String = ""
    var lName: String = ""
    var gender: String = ""
    var dob: Long = 0L
    var weight: Int = 0
    var bloodType: String = ""
    var state: String = ""
    var city: String = ""
    var phoneNumber: String = ""

    fun saveUser() {
        val user = User(
            id = "current_user",
            firstName = fName,
            lastName = lName,
            dob = dob,
            weight = weight,
            bloodType = BloodType.values().find { it.type == bloodType } ?: BloodType.A_POSITIVE,
            location = User.Location(city, state),
            phoneNumber = phoneNumber,
            gender = Gender.values().find { it.type == gender } ?: Gender.MALE
        )
    }
}