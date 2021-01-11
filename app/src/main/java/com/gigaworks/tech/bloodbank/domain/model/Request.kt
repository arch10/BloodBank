package com.gigaworks.tech.bloodbank.domain.model

import com.gigaworks.tech.bloodbank.network.model.RequestDto

data class Request(
    val bloodType: String,
    val city: String,
    val countryCode: String,
    val createdOn: Long = System.currentTimeMillis(),
    val creatorDp: String?,
    val creatorName: String,
    val creatorUid: String,
    val desc: String,
    val expiry: Long,
    val hospital: String,
    val id: String,
    val phone: String,
    val state: String,
    val updatedOn: Long
)

fun Request.toDto(): RequestDto {
    return RequestDto(
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