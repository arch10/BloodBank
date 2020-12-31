package com.gigaworks.tech.bloodbank.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): Resource<T> {
    return withContext(Dispatchers.IO) {
        try {
            Resource.Success(apiCall.invoke())
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    Resource.Failure(false, e.code(), e.message)
                }
                else -> {
                    Resource.Failure(true, null, e.message)
                }
            }
        }
    }
}

fun bearer(token: String): String {
    return "Bearer $token"
}