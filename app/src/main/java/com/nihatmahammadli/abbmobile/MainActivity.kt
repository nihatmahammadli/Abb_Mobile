package com.nihatmahammadli.abbmobile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.nihatmahammadli.abbmobile.databinding.ActivityMainBinding
import com.nihatmahammadli.abbmobile.presentation.components.sheet.FabDialogFragment
import com.nihatmahammadli.abbmobile.presentation.components.ui.LocaleHelper
import com.nihatmahammadli.abbmobile.presentation.utils.LoginCheckHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun attachBaseContext(newBase: Context) {
        val lang = LocaleHelper.getSavedLanguage(newBase)
        val context = LocaleHelper.setAppLocale(newBase, lang)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        onBack()
        clickFab()
        menuInFragments()
        addDestinationMenu()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frgContainerView) as NavHostFragment
        navController = navHostFragment.navController

        val graph = navController.navInflater.inflate(R.navigation.nav_graph)
        val user = FirebaseAuth.getInstance().currentUser

        graph.setStartDestination(if (user != null) R.id.enterPinCode else R.id.mainFragment)

        navController.graph = graph
    }

    private fun menuInFragments() {
        val bottomNav = binding.bottomNavigationView
        val fab = binding.fabIcon

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homePage -> {
                    bottomNav.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE
                    bottomNav.menu.findItem(R.id.home)?.isChecked = true
                }

                R.id.history -> {
                    bottomNav.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE
                    bottomNav.menu.findItem(R.id.history)?.isChecked = true
                }

                R.id.forYou -> {
                    bottomNav.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE
                    bottomNav.menu.findItem(R.id.for_you)?.isChecked = true
                }

                R.id.more -> {
                    bottomNav.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE
                    bottomNav.menu.findItem(R.id.more)?.isChecked = true
                }

                R.id.enterPinCode, R.id.setPinCode -> {
                    bottomNav.visibility = View.GONE
                    fab.visibility = View.GONE
                }

                else -> {
                    bottomNav.visibility = View.GONE
                    fab.visibility = View.GONE
                }
            }
        }
    }

    private fun addDestinationMenu() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            val currentDestination = navController.currentDestination?.id

            val (destId, popToId) = when (menuItem.itemId) {
                R.id.home -> R.id.homePage to R.id.homePage
                R.id.history -> R.id.history to R.id.homePage
                R.id.for_you -> R.id.forYou to R.id.homePage
                R.id.more -> R.id.more to R.id.homePage
                else -> return@setOnItemSelectedListener false
            }

            if (currentDestination == destId) return@setOnItemSelectedListener true

            val options = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(popToId, false) // stack şişməsin
                .build()

            try {
                navController.navigate(destId, null, options)
            } catch (_: Exception) {
                navigateToDestination(destId)
            }
            true
        }
    }

    private fun navigateToDestination(destinationId: Int) {
        try {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.homePage, false)
                .setLaunchSingleTop(true)
                .build()

            navController.navigate(destinationId, null, navOptions)
        } catch (_: Exception) {
            val navInflater = navController.navInflater
            val navGraph = navInflater.inflate(R.navigation.nav_graph)
            navGraph.setStartDestination(destinationId)
            navController.graph = navGraph
        }
    }

    private fun clickFab() {
        binding.fabIcon.setOnClickListener {
            val bottomSheet = FabDialogFragment()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

    override fun onPause() {
        super.onPause()
        // Tətbiq background-a getdi - vaxtı qeyd et
        LoginCheckHelper.onAppPaused(this)
    }

    override fun onResume() {
        super.onResume()

        val isPinRequired = !LoginCheckHelper.onAppResumed(this)
        val currentDestination = navController.currentDestination?.id

        // Əgər PIN lazım deyil və ya artıq EnterPinCode-dasansa → heç nə etmə
        if (!isPinRequired || currentDestination == R.id.enterPinCode) return

        // Ana səhifələrdən birindədirsə → PIN istəyək
        if (isMainDestination(currentDestination)) {
            val opts = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(currentDestination ?: R.id.homePage, true)
                .build()
            navController.navigate(R.id.enterPinCode, null, opts)
        }
    }

    private fun isMainDestination(destinationId: Int?): Boolean {
        return when (destinationId) {
            R.id.homePage, R.id.history, R.id.forYou, R.id.more -> true
            else -> false
        }
    }

    fun onBack() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentDestination = navController.currentDestination?.id
                when (currentDestination) {
                    R.id.homePage -> {
                        // Ana səhifədən geri gedəndə tətbiqi bağla
                        finish()
                    }

                    R.id.enterPinCode -> {
                        // PIN səhifəsindən geri gedəndə çıxış et
                        LoginCheckHelper.logout(this@MainActivity)
                        val navInflater = navController.navInflater
                        val navGraph = navInflater.inflate(R.navigation.nav_graph)
                        navGraph.setStartDestination(R.id.signIn)
                        navController.graph = navGraph
                    }

                    R.id.setPinCode -> {
                        // SetPinCode-dan geri
                        navController.popBackStack()
                    }

                    else -> {
                        navController.popBackStack()
                    }
                }
            }
        })
    }

    /**
     * İstənilən fragment-dən PIN tələb etmək üçün helper.
     * (Məsələn, təhlükəli əməliyyatdan əvvəl çağır.)
     */
    fun requirePinAuthentication() {
        val current = navController.currentDestination?.id
        if (current == R.id.enterPinCode) return

        val opts = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(current ?: R.id.homePage, true)
            .build()

        // Session-u kilidləyirik ki, PIN istəsin
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("isLoggedIn", false).apply()

        navController.navigate(R.id.enterPinCode, null, opts)
    }
}
