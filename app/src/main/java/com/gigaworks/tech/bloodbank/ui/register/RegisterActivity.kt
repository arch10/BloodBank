package com.gigaworks.tech.bloodbank.ui.register

import android.view.LayoutInflater
import com.gigaworks.tech.bloodbank.databinding.ActivityRegisterBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseActivity

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {
    override fun getViewBinding(inflater: LayoutInflater) =
        ActivityRegisterBinding.inflate(inflater)
}