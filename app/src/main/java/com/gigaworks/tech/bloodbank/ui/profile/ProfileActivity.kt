package com.gigaworks.tech.bloodbank.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.gigaworks.tech.bloodbank.databinding.ActivityProfileBinding
import com.gigaworks.tech.bloodbank.network.Resource
import com.gigaworks.tech.bloodbank.ui.base.BaseActivity
import com.gigaworks.tech.bloodbank.ui.profile.viewmodels.ProfileViewModel
import com.gigaworks.tech.bloodbank.util.logD
import com.gigaworks.tech.bloodbank.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding>() {
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)

        setUpView()
        setUpObservables()
    }

    private fun setUpObservables() {
        viewModel.loading.observe(this, {
            binding.loaderView.visible(it)
            binding.profileView.visible(!it)
        })
        viewModel.user.observe(this, {
            when (it) {
                is Resource.Success -> {
                    val user = it.response
                    with(binding) {
                        val fullName = "${user.firstName} ${user.lastName}"
                        name.text = fullName
                        val diff = System.currentTimeMillis() - user.dob
                        // 31449600000 is years in milliseconds
                        val yrs = diff / 31449600000
                        val actualAge = "$yrs yrs"
                        age.text = actualAge
                        val phone = "${user.countryCode} ${user.phoneNumber}"
                        phoneNumber.text = phone
                        city.text = user.location.city
                        state.text = user.location.state
                        gender.text = user.gender.type
                        bloodType.text = user.bloodType.type
                        val wt = "${user.weight} kgs"
                        weight.text = wt
                    }
                }
                is Resource.Failure -> {
                    logD("observeUser: ${it.message}")
                    //TODO("Show a dialog, 'Error connecting to server at the moment. Please try again later'. And close the activity.")
                }
            }
        })
    }

    private fun setUpView() {
        binding.toolbar.setNavigationOnClickListener { handleBackPress() }

        binding.signOutCard.setOnClickListener {
            viewModel.signOut()
        }

        binding.changePic.setOnClickListener {

        }
    }

    private fun handleBackPress() {
        finish()
    }

    override fun getViewBinding(inflater: LayoutInflater) = ActivityProfileBinding.inflate(inflater)
}