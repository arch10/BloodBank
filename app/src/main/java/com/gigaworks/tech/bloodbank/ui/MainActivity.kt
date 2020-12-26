package com.gigaworks.tech.bloodbank.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.gigaworks.tech.bloodbank.R
import com.gigaworks.tech.bloodbank.databinding.ActivityLoginBinding
import com.gigaworks.tech.bloodbank.databinding.ActivityMainBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding(inflater: LayoutInflater) = ActivityMainBinding.inflate(inflater)
}