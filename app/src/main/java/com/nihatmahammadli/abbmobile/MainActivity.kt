package com.nihatmahammadli.abbmobile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.nihatmahammadli.abbmobile.databinding.ActivityMainBinding
import com.nihatmahammadli.abbmobile.presentation.components.sheet.FabDialogFragment
import com.nihatmahammadli.abbmobile.presentation.components.ui.LocaleHelper
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

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frgContainerView) as NavHostFragment
        navController = navHostFragment.navController



        // binding.bottomNavigationView.setupWithNavController(navController)

        onBack()
        clickFab()
        menuInFragments()
        addDestinationMenu()
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
                else -> {
                    bottomNav.visibility = View.GONE
                    fab.visibility = View.GONE
                }
            }
        }
    }

    private fun addDestinationMenu() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(navController.graph.startDestinationId, true) // ✅ stack-i təmizlə
                .setLaunchSingleTop(true)
                .build()

            when (it.itemId) {
                R.id.home -> {
                    if (navController.currentDestination?.id != R.id.homePage) {
                        navController.navigate(R.id.homePage, null, navOptions)
                    }
                    true
                }
                R.id.history -> {
                    if (navController.currentDestination?.id != R.id.history) {
                        navController.navigate(R.id.history, null, navOptions)
                    }
                    true
                }
                R.id.for_you -> {
                    if (navController.currentDestination?.id != R.id.forYou) {
                        navController.navigate(R.id.forYou, null, navOptions)
                    }
                    true
                }
                R.id.more -> {
                    if (navController.currentDestination?.id != R.id.more) {
                        navController.navigate(R.id.more, null, navOptions)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun clickFab() {
        binding.fabIcon.setOnClickListener {
            val bottomSheet = FabDialogFragment()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }


    fun onBack(){
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.id == R.id.homePage) {
                    finish()
                } else {
                    navController.popBackStack()
                }
            }
        })
    }
}
