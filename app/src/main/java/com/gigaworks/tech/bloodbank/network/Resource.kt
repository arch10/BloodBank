package com.gigaworks.tech.bloodbank.network

sealed class Resource<out T> {
    data class Success<T>(val response: T) : Resource<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val message: String?
    ) : Resource<Nothing>()
}