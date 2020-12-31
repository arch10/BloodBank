package com.gigaworks.tech.bloodbank.network.response

import com.gigaworks.tech.bloodbank.cache.model.UserEntity
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("blood_type")
    val bloodType: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("created_on")
    val createdOn: Long,
    @SerializedName("dob")
    val dob: Long,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("uid")
    val uid: String,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("country_code")
    val countryCode: String,
)

fun UserResponse.toEntity(): UserEntity {
    return UserEntity(
        bloodType = bloodType,
        city = city,
        createdOn = createdOn,
        dob = dob,
        firstName = firstName,
        gender = gender,
        id = id,
        lastName = lastName,
        state = state,
        uid = uid,
        weight = weight,
        phone = phone,
        countryCode = countryCode
    )
}