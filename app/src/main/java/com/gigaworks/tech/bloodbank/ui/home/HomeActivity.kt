package com.gigaworks.tech.bloodbank.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.gigaworks.tech.bloodbank.R
import com.gigaworks.tech.bloodbank.databinding.ActivityHomeBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseActivity
import com.gigaworks.tech.bloodbank.ui.home.fragments.BottomSheetDialog
import com.gigaworks.tech.bloodbank.ui.profile.ProfileActivity
import com.gigaworks.tech.bloodbank.util.logD
import com.google.android.material.snackbar.Snackbar


class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_recent,
                R.id.navigation_new_request,
                R.id.navigation_nearby,
                R.id.navigation_more
            )
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        addFirebaseListener()

        binding.dp.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

    }

    fun add(item: MenuItem) {
        BottomSheetDialog().show(supportFragmentManager, "eee")
    }

    override fun onStart() {
        super.onStart()
        setProfilePic(firebaseAuth.currentUser?.photoUrl)
    }

    private fun setProfilePic(uri: Uri?) {
        Glide.with(this)
            .load(uri)
            .placeholder(R.drawable.default_profile)
            .into(binding.dp)
    }

    override fun getViewBinding(inflater: LayoutInflater) = ActivityHomeBinding.inflate(inflater)
}