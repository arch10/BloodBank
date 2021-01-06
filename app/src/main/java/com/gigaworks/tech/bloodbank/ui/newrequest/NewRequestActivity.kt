package com.gigaworks.tech.bloodbank.ui.newrequest

import android.os.Bundle
import android.view.LayoutInflater
import com.gigaworks.tech.bloodbank.databinding.ActivityNewRequestBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseActivity

class NewRequestActivity: BaseActivity<ActivityNewRequestBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)

        setUpView()
        setUpObservables()

    }

    private fun setUpObservables() {
    }

    private fun setUpView() {
        binding.toolbar.setNavigationOnClickListener { handleBackPress() }
    }

    override fun onBackPressed() {
        handleBackPress()
    }

    private fun handleBackPress() {
        finish()
    }

    override fun getViewBinding(inflater: LayoutInflater) = ActivityNewRequestBinding.inflate(inflater)
}