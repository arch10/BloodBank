package com.gigaworks.tech.bloodbank.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gigaworks.tech.bloodbank.cache.dao.RequestDao
import com.gigaworks.tech.bloodbank.cache.dao.UserDao
import com.gigaworks.tech.bloodbank.cache.model.RequestEntity
import com.gigaworks.tech.bloodbank.cache.model.UserEntity

@Database(entities = [UserEntity::class, RequestEntity::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun requestDao(): RequestDao

}