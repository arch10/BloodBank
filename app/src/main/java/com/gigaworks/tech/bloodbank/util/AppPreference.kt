package com.gigaworks.tech.bloodbank.util

import android.content.Context
import android.content.SharedPreferences
import com.gigaworks.tech.bloodbank.BuildConfig

class AppPreference(
    context: Context
) {

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
    }

    fun setStringPreference(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getStringPreference(key: String, def: String = ""): String {
        return sharedPreferences.getString(key, def)!!
    }

    fun setIntPreference(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getIntPreference(key: String, def: Int = 0): Int {
        return sharedPreferences.getInt(key, def)
    }

    companion object {
        private const val SHARED_PREF = BuildConfig.APPLICATION_ID
        const val APP_THEME = "AppTheme"
    }
}