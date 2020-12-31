package com.gigaworks.tech.bloodbank.network.service

import com.gigaworks.tech.bloodbank.network.model.UserDto
import com.gigaworks.tech.bloodbank.network.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {

    @GET("user")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): UserResponse

    @POST("user")
    suspend fun saveUser(
        @Header("Authorization") token: String,
        @Body user: UserDto
    ): UserResponse

}