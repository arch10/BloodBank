package com.gigaworks.tech.bloodbank.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.gigaworks.tech.bloodbank.domain.model.User
import com.gigaworks.tech.bloodbank.util.BloodType
import com.gigaworks.tech.bloodbank.util.Gender

@Entity(tableName = "users")
data class UserEntity(
    @ColumnInfo(name = "blood_type") val bloodType: String,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "created_on") val createdOn: Long,
    @ColumnInfo(name = "dob") val dob: Long,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "gender") val gender: String,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "state") val state: String,
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "country_code") val countryCode: String,
)

fun UserEntity.toDomain() : User {
    return User(
        id = id,
        uid = uid,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phone,
        countryCode= countryCode,
        weight = weight,
        bloodType = BloodType.values().find { it.type == bloodType }?:BloodType.A_POSITIVE,
        gender = Gender.values().find { it.type == gender }?:Gender.MALE,
        dob = dob,
        location = User.Location(city = city, state= state),
        createdOn = createdOn
    )
}