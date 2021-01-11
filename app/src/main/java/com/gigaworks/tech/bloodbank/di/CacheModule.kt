package com.gigaworks.tech.bloodbank.di

import android.content.Context
import androidx.room.Room
import com.gigaworks.tech.bloodbank.cache.Database
import com.gigaworks.tech.bloodbank.cache.dao.RequestDao
import com.gigaworks.tech.bloodbank.cache.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) : Database{
        return Room.databaseBuilder(context, Database::class.java, "blood-bank-db").build()
    }

    @Provides
    fun provideUserDao(database: Database) : UserDao {
        return database.userDao()
    }

    @Provides
    fun provideRequestDao(database: Database) : RequestDao {
        return database.requestDao()
    }

}