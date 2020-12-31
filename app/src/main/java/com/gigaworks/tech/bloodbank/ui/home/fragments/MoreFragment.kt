package com.gigaworks.tech.bloodbank.ui.home.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gigaworks.tech.bloodbank.databinding.FragmentMoreBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseFragment

class MoreFragment: BaseFragment<FragmentMoreBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMoreBinding.inflate(inflater, container, false)
}