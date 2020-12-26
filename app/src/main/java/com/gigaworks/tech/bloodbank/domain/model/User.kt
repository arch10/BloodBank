package com.gigaworks.tech.bloodbank.domain.model

import com.gigaworks.tech.bloodbank.util.BloodType
import com.gigaworks.tech.bloodbank.util.Gender

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val weight: Int,
    val bloodType: BloodType,
    val gender: Gender,
    val dob: Long,
    val location: Location
) {
    data class Location(
        val city: String,
        val state: String
    )
}