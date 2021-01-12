package com.gigaworks.tech.bloodbank.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gigaworks.tech.bloodbank.domain.model.Request

@Entity(tableName = "requests")
data class RequestEntity(
    @ColumnInfo(name = "blood_type") val bloodType: String,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "country_code") val countryCode: String?,
    @ColumnInfo(name = "created_on") val createdOn: Long?,
    @ColumnInfo(name = "creator_dp") val creatorDp: String?,
    @ColumnInfo(name = "creator_name") val creatorName: String?,
    @ColumnInfo(name = "creator_uid") val creatorUid: String,
    @ColumnInfo(name = "desc") val desc: String?,
    @ColumnInfo(name = "expiry") val expiry: Long,
    @ColumnInfo(name = "hospital") val hospital: String?,
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "phone") val phone: String?,
    @ColumnInfo(name = "state") val state: String,
    @ColumnInfo(name = "updated_on") val updatedOn: Long?
)

fun RequestEntity.toDomain(): Request {
    return Request(
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