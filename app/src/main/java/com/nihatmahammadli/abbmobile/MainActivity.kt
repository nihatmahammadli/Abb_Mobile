package com.nihatmahammadli.abbmobile
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    }
}
