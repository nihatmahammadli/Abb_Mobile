package com.nihatmahammadli.abbmobile.presentation.screens

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentEnterDateOfBirthBinding
import java.time.LocalDate
import androidx.core.content.edit
import androidx.navigation.NavOptions

class EnterDateOfBirth : Fragment() {
    private lateinit var binding: FragmentEnterDateOfBirthBinding
    private var isValidAge = false


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterDateOfBirthBinding.inflate(inflater, container, false)

        updateButtonState()

        setupListeners()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupListeners() {
        selectBirth()
        goBackSignUp()
        setupContinueButton()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun selectBirth() {
        val selectYourDate = binding.selectDate

        selectYourDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedDay.toString().padStart(2, '0')}/" +
                        "${(selectedMonth + 1).toString().padStart(2, '0')}/" +
                        "$selectedYear"
                selectYourDate.setText(selectedDate)

                val birthDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                val currentDate = LocalDate.now()

                val isOver18 = birthDate.plusYears(18).isBefore(currentDate) || birthDate.plusYears(18).isEqual(currentDate)

                Log.i("MYTAG", "Is over 18: $isOver18")

                if (!isOver18) {
                    selectYourDate.error = "You must be over 18 years of age"
                    isValidAge = false
                } else {
                    selectYourDate.error = null
                    isValidAge = true
                }

                updateButtonState()

            }, year, month, day)
            datePicker.show()
        }
    }

    private fun goBackSignUp() {
        binding.leftBtn.setOnClickListener {
            binding.selectDate.text?.clear()
            findNavController().popBackStack()
        }
    }

    private fun setupContinueButton() {
        binding.btnContinue.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null && isValidAge && binding.selectDate.text?.isNotEmpty() == true) {

                val sharedPref = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                sharedPref.edit { putBoolean("isLoggedIn", true) }

                findNavController().navigate(
                    R.id.homePage, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.nav_graph, true)
                        .build()
                )
            } else {
                when {
                    user == null -> Toast.makeText(requireContext(), "Please sign in", Toast.LENGTH_SHORT).show()
                    !isValidAge -> Toast.makeText(requireContext(), "You must be over 18 years of age", Toast.LENGTH_SHORT).show()
                    binding.selectDate.text?.isEmpty() == true -> Toast.makeText(requireContext(), "Please select your date of birth", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateButtonState() {
        val hasValidDate = binding.selectDate.text?.isNotEmpty() == true
        val user = FirebaseAuth.getInstance().currentUser
        val isEnabled = hasValidDate && isValidAge && user != null

        binding.btnContinue.isEnabled = isEnabled
        binding.btnContinue.alpha = if (isEnabled) 1f else 0.5f
    }
}