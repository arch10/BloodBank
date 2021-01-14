package com.gigaworks.tech.bloodbank.network.service

import com.gigaworks.tech.bloodbank.network.model.RequestDto
import retrofit2.http.*

interface RequestService {

    @POST("request")
    suspend fun saveRequest(
        @Header("Authorization") token: String,
        @Body user: RequestDto
    ) : RequestDto

    @GET("request")
    suspend fun getMyRequests(
        @Header("Authorization") token: String
    ) : List<RequestDto>

    @GET("request/{bloodType}")
    suspend fun getRequestByBloodType(
        @Header("Authorization") token: String,
        @Path("bloodType") bloodType: String
    ) : List<RequestDto>

}