package com.gigaworks.tech.bloodbank.repository

import com.gigaworks.tech.bloodbank.cache.dao.RequestDao
import com.gigaworks.tech.bloodbank.cache.model.toDomain
import com.gigaworks.tech.bloodbank.cache.safeCacheCall
import com.gigaworks.tech.bloodbank.domain.model.Request
import com.gigaworks.tech.bloodbank.domain.model.toDto
import com.gigaworks.tech.bloodbank.network.Resource
import com.gigaworks.tech.bloodbank.network.model.toEntity
import com.gigaworks.tech.bloodbank.network.safeApiCall
import com.gigaworks.tech.bloodbank.network.service.RequestService
import com.gigaworks.tech.bloodbank.util.printLogE
import javax.inject.Inject

class RequestRepository @Inject constructor(
    private val network: RequestService,
    private val cache: RequestDao
) {
    suspend fun saveRequest(token: String, request: Request): Resource<Request> {
        return when (val networkResponse =
            safeApiCall { network.saveRequest(token, request.toDto()).toEntity() }) {
            is Resource.Success -> {
                cache.insertRequest(networkResponse.response)
                Resource.Success(networkResponse.response.toDomain())
            }
            is Resource.Failure -> {
                //check if network error
                if (!networkResponse.isNetworkError) {
                    printLogE(
                        this.javaClass.simpleName,
                        "saveRequest: code:code:${networkResponse.errorCode} message:${networkResponse.message}"
                    )
                }
                Resource.Failure(
                    networkResponse.isNetworkError,
                    networkResponse.errorCode,
                    networkResponse.message
                )
            }
        }
    }

    suspend fun getMyRequest(token: String): Resource<List<Request>> {
        return when (val networkResponse =
            safeApiCall { network.getMyRequests(token).map { it.toEntity() } }) {
            is Resource.Success -> {
                cache.insertRequest(*networkResponse.response.toTypedArray())
                Resource.Success(networkResponse.response.map { it.toDomain() })
            }
            is Resource.Failure -> {
                //check if network error
                if (!networkResponse.isNetworkError) {
                    printLogE(
                        this.javaClass.simpleName,
                        "getMyRequest: code:${networkResponse.errorCode} message:${networkResponse.message}"
                    )
                }
                safeCacheCall { cache.getRequests().map { it.toDomain() } }
            }
        }
    }

    suspend fun getRequests(token: String, bloodType: String): Resource<List<Request>> {
        return safeApiCall { network.getRequestByBloodType(token, bloodType).map { it.toEntity().toDomain() } }
    }

    suspend fun removeLocalCache() {
        safeCacheCall { cache.deleteTable() }
    }
}