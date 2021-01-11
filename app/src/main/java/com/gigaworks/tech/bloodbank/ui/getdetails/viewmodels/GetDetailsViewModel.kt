package com.gigaworks.tech.bloodbank.ui.getdetails.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigaworks.tech.bloodbank.domain.model.User
import com.gigaworks.tech.bloodbank.network.Resource
import com.gigaworks.tech.bloodbank.network.bearer
import com.gigaworks.tech.bloodbank.repository.UserRepository
import com.gigaworks.tech.bloodbank.util.BloodType
import com.gigaworks.tech.bloodbank.util.Gender
import com.gigaworks.tech.bloodbank.util.logD
import com.gigaworks.tech.bloodbank.util.logE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.launch

class GetDetailsViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    var fName: String = ""
    var lName: String = ""
    var gender: String = ""
    var dob: Long = 0L
    var weight: Int = 0
    var bloodType: String = ""
    var state: String = ""
    var city: String = ""
    var phoneNumber: String = ""

    private val _user = MutableLiveData<Resource<User>>()
    val user: LiveData<Resource<User>>
        get() = _user
    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String>
        get() = _loginError
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun saveUser() {
        val user = User(
            id = "current_user",
            firstName = fName,
            lastName = lName,
            dob = dob,
            weight = weight,
            bloodType = BloodType.values().find { it.type == bloodType } ?: BloodType.A_POSITIVE,
            location = User.Location(city, state),
            phoneNumber = phoneNumber,
            gender = Gender.values().find { it.type == gender } ?: Gender.MALE,
            countryCode = "+91",
            createdOn = 0L,
            uid = "current_user"
        )
        _loading.value = true
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null) {
            firebaseUser.getIdToken(false).addOnCompleteListener { task->
                if(task.isSuccessful) {
                    saveUserName(task.result.token!!, user, firebaseUser)
                } else {
                    logE("saveUser: ${task.exception?.message}")
                    _loginError.value = "Unknown Error"
                    _loading.value = false
                }
            }
        } else {
            logE("saveUser: Cannot find firebase user")
            _loading.value = false
            _loginError.value = "Please verify the phone number again"
        }
    }

    private fun saveUserName(token: String, user: User, firebaseUser: FirebaseUser) {
        val profileUpdates = userProfileChangeRequest {
            displayName = "${user.firstName} ${user.lastName}"
        }
        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                createUser(token, user)
            } else {
                _loading.value = false
                _loginError.value = "Error occurred while creating user. Please try again later."
                logE("saveUserName: Failed to update display name. ${task.exception?.message}")
            }
        }
    }

    private fun createUser(token: String, user: User) {
        viewModelScope.launch {
            _user.value = userRepository.saveUser(bearer(token), user)
            _loading.value = false
        }
    }

}