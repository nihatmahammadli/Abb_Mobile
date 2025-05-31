package com.nihatmahammadli.abbmobile.presentation.dashboard.onboarding.sign.sign_up

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.databinding.FragmentEnterDateOfBirthBinding
import java.time.LocalDate

class EnterDateOfBirth : Fragment() {
    private lateinit var binding : FragmentEnterDateOfBirthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterDateOfBirthBinding.inflate(inflater,container,false)
        binding.btnContinue.isEnabled = false
        binding.btnContinue.alpha = 0.5f
        watchDateText()
        selectBirth()
        goBackSignUp()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectBirth(){
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


                val birthDate = LocalDate.of(selectedYear,selectedMonth+1, selectedDay)
                val currentDate = LocalDate.now()

                val isOver18 = birthDate.plusYears(18).isBefore(currentDate) || birthDate.plusYears(18).isEqual(currentDate)

                Log.i("MYTAG", "Is over 18: $isOver18")

                if (!isOver18) {
                    selectYourDate.error = "You must be over 18 years of age"
                    binding.btnContinue.isEnabled = false
                    binding.btnContinue.alpha = 0.5f
                } else {
                    selectYourDate.error = null
                    binding.btnContinue.isEnabled = true
                    binding.btnContinue.alpha = 1f
                }

            },year,month,day)
            datePicker.show()
        }
    }

    fun goBackSignUp(){
        binding.leftBtn.setOnClickListener {
            binding.selectDate.text?.clear()
            findNavController().popBackStack()
        }
    }


    fun watchDateText(){
        binding.selectDate.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().trim()
                val isValid = input.isNotEmpty()

                binding.btnContinue.isEnabled = true
                binding.btnContinue.alpha = if(isValid) 1f else 0.5f
            }

        })
    }

}