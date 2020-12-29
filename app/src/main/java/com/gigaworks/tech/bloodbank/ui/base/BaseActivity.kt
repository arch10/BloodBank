package com.gigaworks.tech.bloodbank.ui.base

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.gigaworks.tech.bloodbank.ui.MainActivity
import com.gigaworks.tech.bloodbank.ui.home.HomeActivity
import com.gigaworks.tech.bloodbank.util.logD
import com.google.firebase.auth.FirebaseAuth


abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {
    private var _binding: B? = null
    protected val binding get() = _binding!!

    private var shouldAddFirebaseListener = false

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val authChangeListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            //Go to home activity
            logD("authChangeListener: User ${firebaseUser.uid} logged in")
            if(this !is HomeActivity) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        } else {
            logD("authChangeListener: User not logged in")
            if(this !is MainActivity) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    protected fun addFirebaseListener(shouldAdd: Boolean = false) {
        this.shouldAddFirebaseListener = shouldAdd
        if(shouldAdd) {
            firebaseAuth.addAuthStateListener(authChangeListener)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.statusBarColor = Color.BLACK
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        if(shouldAddFirebaseListener) {
            firebaseAuth.removeAuthStateListener(authChangeListener)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String?>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    fun hideKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    abstract fun getViewBinding(inflater: LayoutInflater): B
}