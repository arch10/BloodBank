package com.gigaworks.tech.bloodbank.ui.newrequest

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.gigaworks.tech.bloodbank.R
import com.gigaworks.tech.bloodbank.databinding.ActivityNewRequestBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseActivity
import com.gigaworks.tech.bloodbank.util.*
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NewRequestActivity : BaseActivity<ActivityNewRequestBinding>() {
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)

        setUpView()
        setUpObservables()
    }

    private fun setUpObservables() {
    }

    private fun setUpView() {
        binding.toolbar.setNavigationOnClickListener { handleBackPress() }

        binding.dobTextLayout.setOnClickListener { showDatePicker() }
        binding.dobEndIcon.setOnClickListener { showDatePicker() }

        binding.submitBtn.setOnClickListener {
            if (isValidForm()) {
                hideKeyboard()
                //submit request to server
                Snackbar.make(binding.root, "Request Submitted!", Snackbar.LENGTH_SHORT).show()
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
            setDob(it)
        }
    }

    private fun setDob(dateInMillis: Long) {
        if (dateInMillis > 0) {
            binding.dobTextLayout.text = sdf.format(dateInMillis)
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
            binding.bloodTypeLayout.error = "Not a valid Blood Type"
        } else {
            binding.bloodTypeLayout.error = null
        }
        return isValid
    }

    private fun isValidPhoneNumber(): Boolean {
        val phoneNumber = binding.phoneNumber.text.toString()
        val isValid = FieldValidation.validatePhoneNumber(phoneNumber)
        if (!isValid) {
            binding.phoneLayout.showError(getString(com.gigaworks.tech.bloodbank.R.string.invalid_phone_number))
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
            binding.stateLayout.error = "Enter a valid state"
        } else {
            binding.stateLayout.error = null
        }
        return isValid
    }

    private fun isValidCity(): Boolean {
        val city = binding.city.text.toString()
        val isValid = FieldValidation.validateString(city, min = 2)
        if (!isValid) {
            binding.cityLayout.error = "Enter a valid city"
        } else {
            binding.cityLayout.error = null
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