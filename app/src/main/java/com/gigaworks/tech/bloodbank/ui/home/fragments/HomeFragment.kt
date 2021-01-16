package com.gigaworks.tech.bloodbank.ui.home.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.gigaworks.tech.bloodbank.databinding.FragmentHomeBinding
import com.gigaworks.tech.bloodbank.domain.model.Request
import com.gigaworks.tech.bloodbank.network.Resource
import com.gigaworks.tech.bloodbank.ui.base.BaseFragment
import com.gigaworks.tech.bloodbank.ui.home.adapter.RequestAdapter
import com.gigaworks.tech.bloodbank.ui.home.viewmodels.HomeViewModel
import com.gigaworks.tech.bloodbank.ui.requestdetail.RequestDetails
import com.gigaworks.tech.bloodbank.util.visible
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        setUpObservables()
    }

    private fun setUpObservables() {
        viewModel.loading.observe(viewLifecycleOwner, {
            binding.loaderView.visible(it)
            binding.rv.visible(!it)
        })
        viewModel.error.observe(viewLifecycleOwner, {
            if (it != "") {
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
        viewModel.request.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    val rvAdapter = RequestAdapter(
                        it.response,
                        object : RequestAdapter.OnInteractionListener {
                            override fun onCardClick(request: Request) {
                                startActivity(Intent(context, RequestDetails::class.java).apply {
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

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)
}