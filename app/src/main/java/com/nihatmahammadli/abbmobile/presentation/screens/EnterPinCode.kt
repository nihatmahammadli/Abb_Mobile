package com.nihatmahammadli.abbmobile.presentation.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentEnterPinCodeBinding
import com.nihatmahammadli.abbmobile.presentation.components.notification.NotificationHelper
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EnterPinCode : Fragment() {
    private lateinit var binding: FragmentEnterPinCodeBinding

    private var currentPin = ""
    private var attemptCount = 0
    private val maxPinLength = 6
    private val maxAttempts = 3

    private val pinDots by lazy {
        listOf(
            binding.pinDot1, binding.pinDot2, binding.pinDot3,
            binding.pinDot4, binding.pinDot5, binding.pinDot6
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterPinCodeBinding.inflate(inflater, container, false)
        setupUI()
        setupClickListeners()
        return binding.root
    }

    private fun setupUI() {
        val user = FirebaseAuth.getInstance().currentUser
        val displayName = user?.displayName ?: "User"
        binding.welcomeText.text = "Welcome, $displayName!"
        binding.forgotPinText.visibility = View.VISIBLE
        updatePinDots()
    }

    private fun setupClickListeners() = with(binding) {
        leftBtn.setOnClickListener { signOutUser() }
        listOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
            .forEach { v -> v.setOnClickListener { onDigit((it as Button).text.toString()) } }
        btnDelete.setOnClickListener { onDelete() }
        btnFaceId.setOnClickListener {
            Toast.makeText(requireContext(), "Biometric coming soon", Toast.LENGTH_SHORT).show()
        }
        forgotPinText.setOnClickListener { handleForgotPin() }
    }

    private fun onDigit(d: String) {
        if (currentPin.length >= maxPinLength) return
        currentPin += d
        updatePinDots()
        if (currentPin.length == maxPinLength) verifyPin()
    }

    private fun onDelete() {
        if (currentPin.isNotEmpty()) {
            currentPin = currentPin.dropLast(1)
            updatePinDots()
        }
    }

    private fun updatePinDots() {
        pinDots.forEachIndexed { index, dot ->
            dot.setBackgroundResource(
                if (index < currentPin.length) R.drawable.circle_filled else R.drawable.circle_empty
            )
        }
    }

    private suspend fun verifyPinPlain(entered: String): Boolean {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("User not logged in")
        val doc = FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .get()
            .await()

        val saved = doc.getString("pin")
        return saved != null && saved == entered
    }

    private fun verifyPin() {
        lifecycleScope.launch {
            try {
                binding.keypadGrid.isEnabled = false

                val ok = verifyPinPlain(currentPin)
                if (ok) {
                    val sharedPref =
                        requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    sharedPref.edit {
                        putBoolean("isLoggedIn", true)
                        putLong("last_login_time", System.currentTimeMillis())
                    }
                    Toast.makeText(requireContext(), "Welcome back!", Toast.LENGTH_SHORT).show()

                    NotificationHelper.generateNotification(
                        requireContext(),
                        "Welcome back to ABB!",
                        "Don't forget your PIN code",
                    )

                    findNavController().navigate(
                        R.id.homePage, null,
                        NavOptions.Builder().setPopUpTo(R.id.nav_graph, true).build()
                    )
                } else {
                    handleWrongPin()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Network/Server error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                clearPinWithAnimation()
            } finally {
                binding.keypadGrid.isEnabled = true
            }
        }
    }

    private fun handleWrongPin() {
        attemptCount++
        if (attemptCount >= maxAttempts) {
            Toast.makeText(
                requireContext(),
                "Too many failed attempts. Please sign in again.",
                Toast.LENGTH_LONG
            ).show()
            signOutUser()
        } else {
            val remaining = maxAttempts - attemptCount
            Toast.makeText(
                requireContext(),
                "Wrong PIN. $remaining attempts remaining.",
                Toast.LENGTH_SHORT
            ).show()
            clearPinWithAnimation()
        }
    }

    private fun clearPinWithAnimation() {
        binding.pinDotsContainer.animate().translationX(-20f).setDuration(80).withEndAction {
            binding.pinDotsContainer.animate().translationX(20f).setDuration(80).withEndAction {
                binding.pinDotsContainer.animate().translationX(0f).setDuration(80).withEndAction {
                    currentPin = ""
                    updatePinDots()
                }
            }
        }
    }

    private fun handleForgotPin() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Forgot PIN?")
            .setMessage("You need to sign in again to reset your PIN.")
            .setPositiveButton("Sign Out") { _, _ -> signOutUser() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun signOutUser() {
        FirebaseAuth.getInstance().signOut()
        val sharedPref = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPref.edit {
            putBoolean("isLoggedIn", false)
            putBoolean("pin_setup_complete", false)
            remove("last_login_time")
        }
        findNavController().navigate(
            R.id.signIn, null,
            NavOptions.Builder().setPopUpTo(R.id.nav_graph, true).build()
        )
    }
}
