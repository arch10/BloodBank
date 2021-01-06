package com.gigaworks.tech.bloodbank.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDelegate
import com.gigaworks.tech.bloodbank.databinding.ActivitySplashBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseActivity
import com.gigaworks.tech.bloodbank.util.AppPreference
import com.gigaworks.tech.bloodbank.util.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun getViewBinding(inflater: LayoutInflater) = ActivitySplashBinding.inflate(inflater)

    @Inject
    lateinit var appPreference: AppPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAppTheme()
        addFirebaseListener()
    }

    private fun setAppTheme() {
        val themeMode = when (getSelectedTheme()) {
            AppTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            AppTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    private fun getSelectedTheme(): AppTheme {
        val themeName = appPreference.getStringPreference(AppPreference.APP_THEME, AppTheme.SYSTEM_DEFAULT.name)
        return try {
            AppTheme.valueOf(themeName)
        } catch (e: IllegalArgumentException) {
            AppTheme.SYSTEM_DEFAULT
        }
    }
}