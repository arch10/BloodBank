package com.gigaworks.tech.bloodbank.cache

import com.gigaworks.tech.bloodbank.network.Resource
import com.gigaworks.tech.bloodbank.util.printLogD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

suspend fun <T> safeCacheCall(
    cacheCall: suspend () -> T
): Resource<T> {
    return withContext(Dispatchers.IO) {
        try {
            Resource.Success(cacheCall.invoke())
        } catch (e: Exception) {
            Resource.Failure(false, null, e.message)
        }
    }
}