package com.gigaworks.tech.bloodbank.ui.home.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.gigaworks.tech.bloodbank.R
import com.gigaworks.tech.bloodbank.databinding.FragmentMoreBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseFragment
import com.gigaworks.tech.bloodbank.ui.profile.ProfileActivity

class MoreFragment: BaseFragment<FragmentMoreBinding>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.card1.setOnClickListener {
            startActivity(Intent(activity, ProfileActivity::class.java))
        }
        return binding.root
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMoreBinding.inflate(inflater, container, false)
}