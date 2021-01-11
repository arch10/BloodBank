package com.gigaworks.tech.bloodbank.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gigaworks.tech.bloodbank.cache.model.RequestEntity

@Dao
interface RequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(vararg requests: RequestEntity)

    @Query("SELECT * FROM requests")
    suspend fun getRequests(): List<RequestEntity>

    @Query("DELETE FROM requests")
    suspend fun deleteTable()

}