package com.gigaworks.tech.bloodbank.ui.started

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gigaworks.tech.bloodbank.databinding.FragmentGetstartedBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseFragment
import com.gigaworks.tech.bloodbank.ui.login.LoginActivity
import com.gigaworks.tech.bloodbank.ui.register.RegisterActivity

class GetStartedFragment : BaseFragment<FragmentGetstartedBinding>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        setStatusBarColor(STATUS_BAR_TRANSPARENT)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createAccountBtn.setOnClickListener {
            startActivity(Intent(activity, RegisterActivity::class.java))
        }

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentGetstartedBinding.inflate(inflater, container, false)
}