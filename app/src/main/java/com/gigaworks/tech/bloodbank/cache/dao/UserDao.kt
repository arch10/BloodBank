package com.gigaworks.tech.bloodbank.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gigaworks.tech.bloodbank.cache.model.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(vararg users: UserEntity)

    @Query("SELECT * FROM users WHERE uid=:uid LIMIT 1")
    suspend fun getUserById(uid: String): UserEntity

}