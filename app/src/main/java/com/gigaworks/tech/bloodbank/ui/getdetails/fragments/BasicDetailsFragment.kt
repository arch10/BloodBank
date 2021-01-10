package com.gigaworks.tech.bloodbank.ui.getdetails.fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gigaworks.tech.bloodbank.R
import com.gigaworks.tech.bloodbank.databinding.FragmentBasicDetailsBinding
import com.gigaworks.tech.bloodbank.ui.base.BaseFragment
import com.gigaworks.tech.bloodbank.ui.getdetails.viewmodels.GetDetailsViewModel
import com.gigaworks.tech.bloodbank.util.*
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class BasicDetailsFragment : BaseFragment<FragmentBasicDetailsBinding>() {
    private val viewModel by activityViewModels<GetDetailsViewModel>()
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        setUpView()
        setUpObservables()
        return binding.root
    }

    private fun setUpView() {
        setActionBar(binding.toolbar, onBackIconClick = {
            handleBackPress()
        })
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handleBackPress()
                }
            })
        //populate drop downs
        populateDropDowns()
        //set values if any
        setValues()
    }

    private fun populateDropDowns() {
        val genderListItems = Gender.values().map { item -> item.type }
        val bloodTypeListItems = BloodType.values().map { item -> item.type }
        val genderAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            genderListItems
        )
        val bloodTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            bloodTypeListItems
        )

        with(binding) {
            gender.setAdapter(genderAdapter)
            bloodType.setAdapter(bloodTypeAdapter)
        }
    }

    private fun setValues() {
        val greeting = "Hey, ${viewModel.fName}"
        binding.title.text = greeting
        setDob(viewModel.dob)
        with(binding) {
            gender.setText(viewModel.gender, false)
            bloodType.setText(viewModel.bloodType, false)
            weight.setText(
                if (viewModel.weight == 0) {
                    ""
                } else {
                    viewModel.weight.toString()
                }
            )
        }
    }

    private fun setUpObservables() {
    }

    private fun handleBackPress() {
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dobTextLayout.setOnClickListener { showDatePicker() }
        binding.dobEndIcon.setOnClickListener { showDatePicker() }

        binding.nextBtn.setOnClickListener {
            if (isValidForm()) {
                hideKeyboard()
                findNavController().navigate(R.id.action_basicDetailsFragment_to_locationFragment)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun showDatePicker() {
        val calender = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val calendarConstraint = CalendarConstraints.Builder()
            .setValidator(DobValidator())
            .setEnd(calender.timeInMillis)
            .build()
        val builder = MaterialDatePicker.Builder
            .datePicker()
            .setCalendarConstraints(calendarConstraint)
            .setSelection(viewModel.dob)
        val picker = builder.build()
        picker.show(parentFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener {
            setDob(it)
        }
    }

    private fun setDob(dateInMillis: Long) {
        if (dateInMillis > 0) {
            binding.dobTextLayout.text = sdf.format(dateInMillis)
            viewModel.dob = dateInMillis
        }
    }

    private fun isValidForm(): Boolean {
        return isValidGender() && isValidDob() && isValidWeight() && isValidBloodType()
    }

    private fun isValidWeight(): Boolean {
        val weight = binding.weight.text.toString()
        val isValid = try {
            val wt = weight.toInt()
            wt in 21..399
        } catch (e: NumberFormatException) {
            false
        }
        if (!isValid) {
            //show error
            binding.weightLayout.error = "Not a valid weight"
        } else {
            binding.weightLayout.error = null
            viewModel.weight = weight.toInt()
        }
        return isValid
    }

    @SuppressLint("SimpleDateFormat")
    private fun isValidDob(): Boolean {
        val dob = binding.dobTextLayout.text.toString()
        val isValid = try {
            sdf.parse(dob)
            true
        } catch (e: ParseException) {
            false
        }
        if (!isValid) {
            val tintColor = ContextCompat.getColor(requireContext(), R.color.errorColor)
            with(binding) {
                dobHelperText.apply {
                    text = "Not a valid date format"
                    setTextColor(tintColor)
                }
                dobTextLayout.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border_error)
                ImageViewCompat.setImageTintList(dobEndIcon, ColorStateList.valueOf(tintColor))
            }
        } else {
            val tintColor = ContextCompat.getColor(requireContext(), R.color.disabledColor)
            with(binding) {
                dobHelperText.apply {
                    text = "dd/mm/yyyy"
                    setTextColor(tintColor)
                }
                dobTextLayout.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
                ImageViewCompat.setImageTintList(dobEndIcon, ColorStateList.valueOf(tintColor))
            }
        }
        return isValid
    }

    private fun isValidGender(): Boolean {
        val gender = binding.gender.text.toString()
        val isValid = Gender.values().map { item -> item.type }.contains(gender)
        if (!isValid) {
            //show error
            binding.genderLayout.error = "Not a valid Gender"
        } else {
            binding.genderLayout.error = null
            viewModel.gender = gender
        }
        return isValid
    }

    private fun isValidBloodType(): Boolean {
        val bloodType = binding.bloodType.text.toString()
        val isValid = BloodType.values().map { item -> item.type }.contains(bloodType)
        if (!isValid) {
            //show error
            binding.bloodTypeLayout.error = "Not a valid Blood Type"
        } else {
            binding.bloodTypeLayout.error = null
            viewModel.bloodType = bloodType
        }
        return isValid
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentBasicDetailsBinding.inflate(inflater, container, false)
}