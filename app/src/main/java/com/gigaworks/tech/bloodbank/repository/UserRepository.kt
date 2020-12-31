package com.gigaworks.tech.bloodbank.repository

import com.gigaworks.tech.bloodbank.cache.dao.UserDao
import com.gigaworks.tech.bloodbank.cache.model.toDomain
import com.gigaworks.tech.bloodbank.cache.safeCacheCall
import com.gigaworks.tech.bloodbank.domain.model.User
import com.gigaworks.tech.bloodbank.domain.model.toDto
import com.gigaworks.tech.bloodbank.network.Resource
import com.gigaworks.tech.bloodbank.network.response.toEntity
import com.gigaworks.tech.bloodbank.network.safeApiCall
import com.gigaworks.tech.bloodbank.network.service.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val network: UserService,
    private val cache: UserDao
) {
    suspend fun getUser(token: String, uid: String): Resource<User> {
        return when (val networkResponse = safeApiCall { network.getUser(token).toEntity() }) {
            is Resource.Success -> {
                cache.insertUser(networkResponse.response)
                Resource.Success(networkResponse.response.toDomain())
            }
            is Resource.Failure -> {
                //check if HTTP 404 - if yes return the same Failure
                //else try to get the user from cache
                return if (networkResponse.isNetworkError) {
                    safeCacheCall { cache.getUserById(uid).toDomain() }
                } else {
                    Resource.Failure(
                        networkResponse.isNetworkError,
                        networkResponse.errorCode,
                        networkResponse.message
                    )
                }
            }
        }
    }

    suspend fun saveUser(token: String, user: User): Resource<User> {
        return when (val networkResponse =
            safeApiCall { network.saveUser(token, user.toDto()).toEntity() }) {
            is Resource.Success -> {
                cache.insertUser(networkResponse.response)
                Resource.Success(networkResponse.response.toDomain())
            }
            is Resource.Failure -> {
                Resource.Failure(
                    networkResponse.isNetworkError,
                    networkResponse.errorCode,
                    networkResponse.message
                )
            }
        }
    }
}