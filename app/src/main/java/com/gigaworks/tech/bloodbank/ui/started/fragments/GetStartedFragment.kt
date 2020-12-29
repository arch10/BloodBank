package com.gigaworks.tech.bloodbank.ui.started.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gigaworks.tech.bloodbank.databinding.FragmentGetstartedBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseFragment
import com.gigaworks.tech.bloodbank.ui.register.RegisterActivity

class GetStartedFragment : BaseFragment<FragmentGetstartedBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.getStartedBtn.setOnClickListener {
            startActivity(Intent(activity, RegisterActivity::class.java))
        }

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentGetstartedBinding.inflate(inflater, container, false)
}