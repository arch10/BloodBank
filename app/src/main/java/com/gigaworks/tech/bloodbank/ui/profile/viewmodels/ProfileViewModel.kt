package com.gigaworks.tech.bloodbank.ui.profile.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigaworks.tech.bloodbank.domain.model.User
import com.gigaworks.tech.bloodbank.network.Resource
import com.gigaworks.tech.bloodbank.network.bearer
import com.gigaworks.tech.bloodbank.repository.UserRepository
import com.gigaworks.tech.bloodbank.util.logE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class ProfileViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableLiveData<Resource<User>>()
    val user: LiveData<Resource<User>>
        get() = _user
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
        loadUser()
    }

    private fun loadUser() {
        _loading.value = true
        firebaseAuth.currentUser?.let { generateUserToken(it) }
    }

    private fun generateUserToken(firebaseUser: FirebaseUser) {
        firebaseUser.getIdToken(false).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                checkUserInfo(token = task.result.token!!, firebaseUser.uid)
            } else {
                logE("generateUserToken: ${task.exception?.message}")
                _error.value = "Unknown Error"
                _loading.value = false
            }
        }
    }

    private fun checkUserInfo(token: String, uid: String) {
        viewModelScope.launch {
            _user.value = userRepository.getUserCache(bearer(token), uid)
            _loading.value = false
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userRepository.removeLocalCache()
            firebaseAuth.signOut()
        }
    }

}