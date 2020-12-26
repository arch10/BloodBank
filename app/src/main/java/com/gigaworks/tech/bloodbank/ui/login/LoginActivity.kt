package com.gigaworks.tech.bloodbank.ui.login

import android.view.LayoutInflater
import com.gigaworks.tech.bloodbank.databinding.ActivityLoginBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override fun getViewBinding(inflater: LayoutInflater) = ActivityLoginBinding.inflate(inflater)
}