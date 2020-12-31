package com.gigaworks.tech.bloodbank.domain.model

import com.gigaworks.tech.bloodbank.network.model.UserDto
import com.gigaworks.tech.bloodbank.util.BloodType
import com.gigaworks.tech.bloodbank.util.Gender
import java.util.*

data class User(
    val id: String = "current_user",
    val uid: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val countryCode: String = "+91",
    val weight: Int,
    val bloodType: BloodType,
    val gender: Gender,
    val dob: Long,
    val location: Location,
    val createdOn: Long = Date().time
) {
    data class Location(
        val city: String,
        val state: String
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        city = location.city,
        phone = phoneNumber,
        countryCode = countryCode,
        dob = dob,
        gender = gender.type,
        bloodType = bloodType.type,
        weight = weight,
        uid = uid,
        state = location.state,
        lastName = lastName,
        firstName = firstName,
        createdOn = createdOn
    )
}