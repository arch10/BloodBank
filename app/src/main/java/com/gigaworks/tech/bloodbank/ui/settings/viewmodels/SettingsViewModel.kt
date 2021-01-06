package com.gigaworks.tech.bloodbank.ui.settings.viewmodels

import androidx.appcompat.app.AppCompatDelegate.*
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gigaworks.tech.bloodbank.util.AppPreference
import com.gigaworks.tech.bloodbank.util.AppPreference.Companion.APP_THEME
import com.gigaworks.tech.bloodbank.util.AppTheme

class SettingsViewModel @ViewModelInject constructor(
    private val appPreference: AppPreference
) : ViewModel() {
    private val _selectedTheme = MutableLiveData(getSelectedTheme())
    val selectedTheme: LiveData<AppTheme>
        get() = _selectedTheme

    fun changeTheme(themeId: Int) {
        val theme = getAppThemeByOrdinal(themeId)
        appPreference.setStringPreference(APP_THEME, theme.name)
        val themeMode = when (theme) {
            AppTheme.DARK -> MODE_NIGHT_YES
            AppTheme.LIGHT -> MODE_NIGHT_NO
            else -> MODE_NIGHT_FOLLOW_SYSTEM
        }
        setDefaultNightMode(themeMode)
        _selectedTheme.value = theme
    }

    private fun getSelectedTheme(): AppTheme {
        val themeName = appPreference.getStringPreference(APP_THEME, AppTheme.SYSTEM_DEFAULT.name)
        return try {
            AppTheme.valueOf(themeName)
        } catch (e: IllegalArgumentException) {
            AppTheme.SYSTEM_DEFAULT
        }
    }

    private fun getAppThemeByOrdinal(ordinal: Int): AppTheme {
        return AppTheme.values().find { it.ordinal == ordinal } ?: AppTheme.SYSTEM_DEFAULT
    }
}