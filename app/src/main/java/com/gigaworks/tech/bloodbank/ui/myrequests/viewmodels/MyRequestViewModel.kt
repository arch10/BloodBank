package com.gigaworks.tech.bloodbank.ui.myrequests.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigaworks.tech.bloodbank.domain.model.Request
import com.gigaworks.tech.bloodbank.network.Resource
import com.gigaworks.tech.bloodbank.network.bearer
import com.gigaworks.tech.bloodbank.repository.RequestRepository
import com.gigaworks.tech.bloodbank.util.logE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class MyRequestViewModel @ViewModelInject constructor(
    private val requestRepository: RequestRepository
): ViewModel() {

    private val _request = MutableLiveData<Resource<List<Request>>>()
    val request: LiveData<Resource<List<Request>>>
        get() = _request
    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    init {
        getMyRequests()
    }

    private fun getMyRequests() {
        _loading.value = true
        firebaseAuth.currentUser?.let { generateUserToken(it) }
    }

    private fun generateUserToken(firebaseUser: FirebaseUser) {
        firebaseUser.getIdToken(false).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                getRequests(token = task.result.token!!)
            } else {
                logE("generateUserToken: ${task.exception?.message}")
                _error.value = "Unknown Error"
                _loading.value = false
            }
        }
    }

    private fun getRequests(token: String) {
        viewModelScope.launch {
            _request.value = requestRepository.getMyRequest(bearer(token))
            _loading.value = false
        }
    }
}