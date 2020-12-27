package com.gigaworks.tech.bloodbank.ui.base

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.gigaworks.tech.bloodbank.util.logD
import com.gigaworks.tech.bloodbank.util.logE

abstract class BaseFragment<B : ViewBinding> : Fragment() {
    private var _binding: B? = null
    protected val binding get() = _binding!!
    private var _baseActivity: BaseActivity<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // initialize viewBinding
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            _baseActivity = context
        }
    }

    override fun onDetach() {
        _baseActivity = null
        super.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun hideKeyboard() {
        _baseActivity?.hideKeyboard()
    }

    fun setStatusBarColor(@Size(min = 1) color: String) {
        if (color == STATUS_BAR_TRANSPARENT) {
            val colorInt = Color.parseColor("#6f000000")
            activity?.window?.statusBarColor = colorInt
        } else {
            try {
                val colorInt = Color.parseColor(color)
                activity?.window?.statusBarColor = colorInt
            } catch (e: IllegalArgumentException) {
                logE(e.message)
            }
        }
    }

    fun setActionBar(toolbar: Toolbar, title: String = "", onBackIconClick: (View?) -> Unit = {}) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setTitle(title)
        }
        toolbar.setNavigationOnClickListener(onBackIconClick)
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String) {
        _baseActivity?.hasPermission(permission)
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String?>, requestCode: Int) {
        _baseActivity?.requestPermissionsSafely(permissions, requestCode)
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): B

    companion object {
        const val STATUS_BAR_TRANSPARENT = "STATUS_BAR_TRANSPARENT"
    }
}
