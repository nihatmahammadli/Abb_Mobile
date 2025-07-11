package com.nihatmahammadli.abbmobile
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import com.nihatmahammadli.abbmobile.databinding.ActivityMainBinding
import com.nihatmahammadli.abbmobile.presentation.dashboard.ui.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun attachBaseContext(newBase: Context) {
        val lang = LocaleHelper.getSavedLanguage(newBase)
        val context = LocaleHelper.setAppLocale(newBase, lang)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        iconFindForMenu()
        menuInFragments()
        addDestinationMenu()

    }

    fun iconFindForMenu(){
        val bottomNavigationView = binding.bottomNavigationView
        val plus = bottomNavigationView.menu.findItem(R.id.plus)
        plus.icon = ContextCompat.getDrawable(this, R.drawable.plus_ic_img)
        plus.icon?.setTintList(null)
    }

    fun menuInFragments(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frgContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homePage -> binding.bottomNavigationView.visibility = View.VISIBLE
                R.id.history -> binding.bottomNavigationView.visibility = View.VISIBLE
                else -> binding.bottomNavigationView.visibility = View.GONE
            }
        }
    }

    fun addDestinationMenu(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frgContainerView) as NavHostFragment
        val navController = navHostFragment.navController


        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    if(navController.currentDestination?.id != R.id.homePage) {
                        navController.navigate(R.id.homePage)
                    }
                    true
                }
                R.id.history -> {
                    if(navController.currentDestination?.id != R.id.history) {
                        navController.navigate(R.id.history)
                    }
                    true
                }
                else -> false
            }
        }
    }


}
