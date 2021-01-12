package com.gigaworks.tech.bloodbank.domain.model

import com.gigaworks.tech.bloodbank.network.model.RequestDto

data class Request(
    val bloodType: String,
    val city: String,
    val countryCode: String? = null,
    val createdOn: Long? = null,
    val creatorDp: String? = null,
    val creatorName: String? = null,
    val creatorUid: String,
    val desc: String? = null,
    val expiry: Long,
    val hospital: String? = null,
    val id: String? = null,
    val phone: String? = null,
    val state: String,
    val updatedOn: Long? = null
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