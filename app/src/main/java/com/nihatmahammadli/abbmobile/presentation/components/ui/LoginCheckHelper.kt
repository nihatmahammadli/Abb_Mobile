package com.nihatmahammadli.abbmobile.presentation.utils

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.nihatmahammadli.abbmobile.R

object LoginCheckHelper {

    fun getStartDestination(context: Context): Int {
        val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser == null) {
            return R.id.signIn
        }

        val pinSetupComplete = sharedPref.getBoolean("pin_setup_complete", false)
        if (!pinSetupComplete) {
            val dateOfBirthComplete = sharedPref.getBoolean("date_of_birth_complete", false)
            return if (dateOfBirthComplete) {
                R.id.setPinCode
            } else {
                R.id.enterDateOfBirth
            }
        }

        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        if (!isLoggedIn) {
            return R.id.enterPinCode
        }

        // Session vaxtını yoxla (24 saat sonra yenidən PIN istə)
        val lastLoginTime = sharedPref.getLong("last_login_time", 0)
        val currentTime = System.currentTimeMillis()
        val sessionTimeout = 24 * 60 * 60 * 1000L // 24 saat

        if (currentTime - lastLoginTime > sessionTimeout) {
            // Session bitib - PIN istə
            sharedPref.edit().putBoolean("isLoggedIn", false).apply()
            return R.id.enterPinCode
        }

        // Hər şey qaydasında - ana səhifə
        return R.id.homePage
    }

    /**
     * İstifadəçini logout edir
     */
    fun logout(context: Context) {
        FirebaseAuth.getInstance().signOut()

        val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPref.edit()
            .putBoolean("isLoggedIn", false)
            .remove("last_login_time")
            .remove("app_paused_time")
            .apply()
    }

    /**
     * PIN resetləyir (Forgot PIN zamanı)
     */
    fun resetPin(context: Context) {
        val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPref.edit()
            .putBoolean("pin_setup_complete", false)
            .putBoolean("isLoggedIn", false)
            .remove("user_pin")
            .remove("last_login_time")
            .remove("app_paused_time")
            .apply()
    }

    /**
     * Tətbiq background-a getdikdə çağrılır
     */
    fun onAppPaused(context: Context) {
        val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPref.edit()
            .putLong("app_paused_time", System.currentTimeMillis())
            .apply()
    }

    /**
     * Tətbiq foreground-a gəldikdə çağrılır
     * Müəyyən müddət sonra PIN istəyə bilər
     */
    fun onAppResumed(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val pausedTime = sharedPref.getLong("app_paused_time", 0)
        val currentTime = System.currentTimeMillis()
        val lockTimeout = 5 * 60 * 1000L // 5 dəqiqə

        // Əgər 5 dəqiqədən çox background-da qalıbsa PIN istə
        if (pausedTime > 0 && currentTime - pausedTime > lockTimeout) {
            sharedPref.edit()
                .putBoolean("isLoggedIn", false)
                .remove("app_paused_time")
                .apply()
            return false // PIN lazımdır
        }

        sharedPref.edit().remove("app_paused_time").apply()
        return true // PIN lazım deyil
    }

    /**
     * PIN doğru olub-olmadığını yoxlayır
     */
    fun verifyPin(context: Context, enteredPin: String): Boolean {
        val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val savedPin = sharedPref.getString("user_pin", "")
        return enteredPin == savedPin
    }

    /**
     * PIN yadda saxlayır
     */
    fun savePin(context: Context, pin: String) {
        val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPref.edit()
            .putString("user_pin", pin)
            .putBoolean("pin_setup_complete", true)
            .apply()
    }

    /**
     * Login uğurlu olduqdan sonra çağrılır
     */
    fun setLoginSuccess(context: Context) {
        val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPref.edit()
            .putBoolean("isLoggedIn", true)
            .putLong("last_login_time", System.currentTimeMillis())
            .apply()
    }

    /**
     * İstifadəçinin PIN təyin edib-etmədiyini yoxlayır
     */
    fun isPinSetupComplete(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("pin_setup_complete", false)
    }

    /**
     * İstifadəçinin login olub-olmadığını yoxlayır
     */
    fun isUserLoggedIn(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) return false

        // Session timeout yoxla
        val lastLoginTime = sharedPref.getLong("last_login_time", 0)
        val currentTime = System.currentTimeMillis()
        val sessionTimeout = 24 * 60 * 60 * 1000L // 24 saat

        if (currentTime - lastLoginTime > sessionTimeout) {
            // Session bitib
            sharedPref.edit().putBoolean("isLoggedIn", false).apply()
            return false
        }

        return true
    }
}