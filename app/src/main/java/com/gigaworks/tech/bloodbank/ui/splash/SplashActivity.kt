package com.gigaworks.tech.bloodbank.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import com.gigaworks.tech.bloodbank.databinding.ActivitySplashBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun getViewBinding(inflater: LayoutInflater) = ActivitySplashBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFirebaseListener(true)
    }
}