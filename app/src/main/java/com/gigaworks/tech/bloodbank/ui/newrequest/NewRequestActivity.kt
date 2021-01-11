package com.gigaworks.tech.bloodbank.ui.newrequest

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.gigaworks.tech.bloodbank.R
import com.gigaworks.tech.bloodbank.databinding.ActivityNewRequestBinding
import com.gigaworks.tech.bloodbank.domain.model.Request
import com.gigaworks.tech.bloodbank.network.Resource
import com.gigaworks.tech.bloodbank.ui.base.BaseActivity
import com.gigaworks.tech.bloodbank.ui.newrequest.viewmodels.NewRequestViewModel
import com.gigaworks.tech.bloodbank.util.*
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewRequestActivity : BaseActivity<ActivityNewRequestBinding>() {
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    private val viewModel: NewRequestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)

        setUpView()
        setUpObservables()
    }

    private fun setUpObservables() {
        viewModel.loading.observe(this, {
            binding.loaderView.loaderOverlay.visible(it)
        })

        viewModel.request.observe(this, {
            when (it) {
                is Resource.Success -> {
                    handleBackPress()
                }
                is Resource.Failure -> {
                    Snackbar.make(
                        binding.root,
                        "Cannot create a request at the moment. Please try again later.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })

        viewModel.error.observe(this, { error ->
            if (error != "") {
                Snackbar.make(
                    binding.root,
                    error,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setUpView() {
        binding.toolbar.setNavigationOnClickListener { handleBackPress() }

        binding.dobTextLayout.setOnClickListener { showDatePicker() }
        binding.dobEndIcon.setOnClickListener { showDatePicker() }

        binding.submitBtn.setOnClickListener {
            if (isValidForm()) {
                hideKeyboard()
                //submit request to server
                val request = Request(
                    bloodType = binding.bloodType.text.toString(),
                    city = binding.city.text.toString(),
                    countryCode = "+91",
                    createdOn = System.currentTimeMillis(),
                    creatorDp = firebaseAuth.currentUser?.photoUrl.toString(),
                    creatorName = firebaseAuth.currentUser?.displayName!!,
                    creatorUid = firebaseAuth.currentUser?.uid!!,
                    desc = binding.desc.text.toString(),
                    expiry = getDate(),
                    hospital = binding.hospital.text.toString(),
                    id = "-1",
                    phone = binding.phoneNumber.text.toString(),
                    state = binding.state.text.toString(),
                    updatedOn = System.currentTimeMillis()
                )
                viewModel.saveRequest(request)
            }
        }
        //populate drop downs
        populateDropDowns()
    }

    private fun showDatePicker() {
        val calender = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val calendarConstraint = CalendarConstraints.Builder()
            .setValidator(RequestValidator())
            .setStart(calender.timeInMillis)
            .build()
        val builder = MaterialDatePicker.Builder
            .datePicker()
            .setCalendarConstraints(calendarConstraint)
        val picker = builder.build()
        picker.show(supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener {
            setDate(it)
        }
    }

    private fun setDate(dateInMillis: Long) {
        if (dateInMillis > 0) {
            binding.dobTextLayout.text = sdf.format(dateInMillis)
        }
    }

    private fun getDate(): Long {
        val dateText = binding.dobTextLayout.text.toString().trim()
        return try {
            sdf.parse(dateText)?.time!!
        } catch (e: ParseException) {
            System.currentTimeMillis()
        }
    }

    private fun populateDropDowns() {
        val bloodTypeListItems = BloodType.values().map { item -> item.type }
        val bloodTypeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            bloodTypeListItems
        )
        binding.bloodType.setAdapter(bloodTypeAdapter)
        val citiesAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            CITIES_LIST
        )
        binding.city.setAdapter(citiesAdapter)
    }

    private fun isValidForm(): Boolean {
        return isValidBloodType() && isValidPhoneNumber() && isValidRequestDate() && isValidDesc() && isValidCity() && isValidState()
    }

    private fun isValidBloodType(): Boolean {
        val bloodType = binding.bloodType.text.toString()
        val isValid = BloodType.values().map { item -> item.type }.contains(bloodType)
        if (!isValid) {
            //show error
            binding.bloodTypeLayout.showError("Not a valid Blood Type")
        } else {
            binding.bloodTypeLayout.hideError()
        }
        return isValid
    }

    private fun isValidPhoneNumber(): Boolean {
        val phoneNumber = binding.phoneNumber.text.toString()
        val isValid = FieldValidation.validatePhoneNumber(phoneNumber)
        if (!isValid) {
            binding.phoneLayout.showError(getString(R.string.invalid_phone_number))
        } else {
            binding.phoneLayout.hideError()
        }
        return isValid
    }

    private fun isValidDesc(): Boolean {
        return binding.descLayout.editText?.text.toString().length <= binding.descLayout.counterMaxLength
    }

    private fun isValidState(): Boolean {
        val state = binding.state.text.toString()
        val isValid = FieldValidation.validateString(state, min = 2)
        if (!isValid) {
            binding.stateLayout.showError("Enter a valid state")
        } else {
            binding.stateLayout.hideError()
        }
        return isValid
    }

    private fun isValidCity(): Boolean {
        val city = binding.city.text.toString()
        val isValid = FieldValidation.validateString(city, min = 2)
        if (!isValid) {
            binding.cityLayout.showError("Enter a valid city")
        } else {
            binding.cityLayout.hideError()
        }
        return isValid
    }

    private fun isValidRequestDate(): Boolean {
        val dob = binding.dobTextLayout.text.toString()
        val isValid = try {
            sdf.parse(dob)
            true
        } catch (e: ParseException) {
            false
        }
        if (!isValid) {
            val tintColor = ContextCompat.getColor(this, R.color.errorColor)
            with(binding) {
                dobHelperText.apply {
                    text = "Not a valid date format"
                    setTextColor(tintColor)
                }
                dobTextLayout.background =
                    ContextCompat.getDrawable(
                        this@NewRequestActivity,
                        R.drawable.rounded_border_error
                    )
                ImageViewCompat.setImageTintList(dobEndIcon, ColorStateList.valueOf(tintColor))
            }
        } else {
            val tintColor = ContextCompat.getColor(this, R.color.disabledColor)
            with(binding) {
                dobHelperText.apply {
                    text = "dd/mm/yyyy"
                    setTextColor(tintColor)
                }
                dobTextLayout.background =
                    ContextCompat.getDrawable(this@NewRequestActivity, R.drawable.rounded_border)
                ImageViewCompat.setImageTintList(dobEndIcon, ColorStateList.valueOf(tintColor))
            }
        }
        return isValid
    }

    override fun onBackPressed() {
        handleBackPress()
    }

    private fun handleBackPress() {
        finish()
    }

    override fun getViewBinding(inflater: LayoutInflater) =
        ActivityNewRequestBinding.inflate(inflater)
}