package com.nihatmahammadli.abbmobile.presentation.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentSetPinCodeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

class SetPinCode : Fragment() {
    private lateinit var binding: FragmentSetPinCodeBinding

    private var currentPin = ""
    private var firstPin = ""
    private var isConfirmingPin = false
    private val maxPinLength = 6

    private val pinDots by lazy {
        listOf(
            binding.pinDot1,binding.pinDot2,binding.pinDot3,
            binding.pinDot4,binding.pinDot5,binding.pinDot6
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetPinCodeBinding.inflate(inflater, container, false)
        setupUI()
        setupClickListeners()
        return binding.root
    }

    private fun setupUI() {
        updateWelcomeText()
        updatePinDots()
        binding.forgotPinText.visibility = View.GONE
    }

    private fun updateWelcomeText() {
        binding.welcomeText.text = if (isConfirmingPin) "Confirm your PIN" else "Set your PIN code"
    }

    private fun setupClickListeners() = with(binding) {
        leftBtn.setOnClickListener {
            if (isConfirmingPin) {
                isConfirmingPin = false
                currentPin = ""
                firstPin = ""
                updateWelcomeText()
                updatePinDots()
            } else {
                findNavController().popBackStack()
            }
        }

        listOf(btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9)
            .forEach { v -> v.setOnClickListener { addDigit((v as android.widget.Button).text.toString()) } }

        btnDelete.setOnClickListener { removeDigit() }
        btnFaceId.setOnClickListener {
            Toast.makeText(requireContext(),"Face ID not available",Toast.LENGTH_SHORT).show()
        }
    }

    private fun addDigit(digit: String) {
        if (currentPin.length < maxPinLength) {
            currentPin += digit
            updatePinDots()
            if (currentPin.length == maxPinLength) handleCompletePin()
        }
    }

    private fun removeDigit() {
        if (currentPin.isNotEmpty()) {
            currentPin = currentPin.dropLast(1)
            updatePinDots()
        }
    }

    private fun updatePinDots() {
        pinDots.forEachIndexed { index, dot ->
            dot.setBackgroundResource(if (index < currentPin.length) R.drawable.circle_filled else R.drawable.circle_empty)
        }
    }

    private fun handleCompletePin() {
        if (!isConfirmingPin) {
            firstPin = currentPin
            currentPin = ""
            isConfirmingPin = true
            binding.root.postDelayed({
                updateWelcomeText()
                updatePinDots()
            }, 150)
        } else {
            if (currentPin == firstPin) {
                savePinAndNavigate()
            } else {
                Toast.makeText(requireContext(), "PINs don't match. Please try again.", Toast.LENGTH_LONG).show()
                resetPinSetup()
            }
        }
    }

    private suspend fun setPinForCurrentUser(pin: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("User not logged in")

        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(uid)
            .set(mapOf("pin" to pin), SetOptions.merge())
            .await()
    }

    private fun savePinAndNavigate() {
        lifecycleScope.launch {
            try {
                binding.keypadGrid.isEnabled = false

                setPinForCurrentUser(firstPin)

                val sharedPref = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                sharedPref.edit {
                    putBoolean("pin_setup_complete", true)
                    putBoolean("isLoggedIn", true)
                    putLong("last_login_time", System.currentTimeMillis())
                }

                Toast.makeText(requireContext(), "PIN code set successfully!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    R.id.homePage, null,
                    NavOptions.Builder().setPopUpTo(R.id.nav_graph, true).build()
                )
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error saving PIN: ${e.message}", Toast.LENGTH_LONG).show()
                resetPinSetup()
            } finally {
                binding.keypadGrid.isEnabled = true
            }
        }
    }

    private fun resetPinSetup() {
        currentPin = ""
        firstPin = ""
        isConfirmingPin = false
        updateWelcomeText()
        updatePinDots()
    }
}
