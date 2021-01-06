package com.gigaworks.tech.bloodbank.ui.home.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.gigaworks.tech.bloodbank.R
import com.gigaworks.tech.bloodbank.databinding.FragmentMoreBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseFragment
import com.gigaworks.tech.bloodbank.ui.profile.ProfileActivity
import com.gigaworks.tech.bloodbank.ui.settings.SettingsActivity
import com.google.firebase.auth.FirebaseAuth

class MoreFragment: BaseFragment<FragmentMoreBinding>() {

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.card1.setOnClickListener {
            startActivity(Intent(activity, ProfileActivity::class.java))
        }
        binding.card1.setOnClickListener {
            startActivity(Intent(activity, ProfileActivity::class.java))
        }
        binding.card3.setOnClickListener {
            startActivity(Intent(activity, SettingsActivity::class.java))
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setProfilePic(firebaseAuth.currentUser?.photoUrl)
    }

    private fun setProfilePic(uri: Uri?) {
        Glide.with(this)
            .load(uri)
            .placeholder(R.drawable.default_profile)
            .into(binding.profileIcon)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMoreBinding.inflate(inflater, container, false)
}