package com.gigaworks.tech.bloodbank.ui.myrequests

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import com.gigaworks.tech.bloodbank.databinding.ActivityMyRequestsBinding
import com.gigaworks.tech.bloodbank.domain.model.Request
import com.gigaworks.tech.bloodbank.network.Resource
import com.gigaworks.tech.bloodbank.ui.base.BaseActivity
import com.gigaworks.tech.bloodbank.ui.myrequests.viewmodels.MyRequestViewModel
import com.gigaworks.tech.bloodbank.ui.requestdetail.RequestDetails
import com.gigaworks.tech.bloodbank.util.visible
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyRequests : BaseActivity<ActivityMyRequestsBinding>() {
    private val viewModel: MyRequestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)

        setUpView()
        setUpObservables()
    }

    private fun setUpObservables() {
        viewModel.loading.observe(this, {
            binding.loaderView.visible(it)
        })
        viewModel.error.observe(this, {
            if (it != "") {
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
        viewModel.request.observe(this, {
            when (it) {
                is Resource.Success -> {
                    val rvAdapter = MyRequestAdapter(
                        it.response,
                        object : MyRequestAdapter.OnInteractionListener {
                            override fun onCardClick(request: Request) {
                                startActivity(Intent(this@MyRequests, RequestDetails::class.java).apply {
                                    putExtra(RequestDetails.REQUEST, request)
                                })
                            }
                        })
                    binding.rv.adapter = rvAdapter
                }
                is Resource.Failure -> {
                    Snackbar.make(
                        binding.root,
                        "Cannot get requests at the moment. Please try again later.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun setUpView() {
        binding.toolbar.setNavigationOnClickListener { handleBackPress() }
    }

    private fun handleBackPress() {
        finish()
    }

    override fun getViewBinding(inflater: LayoutInflater) =
        ActivityMyRequestsBinding.inflate(inflater)
}