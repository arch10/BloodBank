package com.gigaworks.tech.bloodbank.network.model


import com.gigaworks.tech.bloodbank.cache.model.RequestEntity
import com.google.gson.annotations.SerializedName

data class RequestDto(
    @SerializedName("blood_type")
    val bloodType: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("created_on")
    val createdOn: Long,
    @SerializedName("creator_dp")
    val creatorDp: String?,
    @SerializedName("creator_name")
    val creatorName: String,
    @SerializedName("creator_uid")
    val creatorUid: String,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("expiry")
    val expiry: Long,
    @SerializedName("hospital")
    val hospital: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("updated_on")
    val updatedOn: Long
)

fun RequestDto.toEntity(): RequestEntity {
    return RequestEntity(
        bloodType=bloodType,
        city=city,
        countryCode=countryCode,
        createdOn=createdOn,
        creatorDp=creatorDp,
        creatorName=creatorName,
        creatorUid=creatorUid,
        desc=desc,
        expiry=expiry,
        hospital=hospital,
        id=id,
        phone=phone,
        state=state,
        updatedOn=updatedOn
    )
}