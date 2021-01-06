package com.gigaworks.tech.bloodbank.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.gigaworks.tech.bloodbank.R
import com.gigaworks.tech.bloodbank.databinding.ActivitySettingsBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseActivity
import com.gigaworks.tech.bloodbank.ui.settings.viewmodels.SettingsViewModel
import com.gigaworks.tech.bloodbank.util.AppTheme
import com.gigaworks.tech.bloodbank.util.logD
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {
    private val themeItems by lazy {
        arrayOf(
            getString(R.string.system_default),
            getString(R.string.light),
            getString(R.string.dark)
        )
    }
    private val viewModel by viewModels<SettingsViewModel>()
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)

        setUpView()
        setUpObservables()
    }

    private fun setUpObservables() {
        viewModel.selectedTheme.observe(this, {
            binding.themeSubtitle.text = themeItems[it.ordinal]
        })
    }

    private fun setUpView() {
        binding.toolbar.setNavigationOnClickListener { handleBackPress() }
        binding.themeCard.setOnClickListener {
            var selectedThemeChoice = viewModel.selectedTheme.value!!.ordinal
            dialog = MaterialAlertDialogBuilder(this)
                .setTitle("Choose theme")
                .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                    viewModel.changeTheme(selectedThemeChoice)
                    dialog.dismiss()
                }
                .setSingleChoiceItems(themeItems, viewModel.selectedTheme.value!!.ordinal) { _, which ->
                    selectedThemeChoice = which
                }
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
    }

    override fun onBackPressed() {
        dialog?.dismiss()
        handleBackPress()
    }

    private fun handleBackPress() {
        finish()
    }

    override fun getViewBinding(inflater: LayoutInflater) =
        ActivitySettingsBinding.inflate(inflater)
}