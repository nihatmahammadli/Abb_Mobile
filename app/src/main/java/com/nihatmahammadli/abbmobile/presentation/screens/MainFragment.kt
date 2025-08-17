package com.nihatmahammadli.abbmobile.presentation.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentMainBinding
import com.nihatmahammadli.abbmobile.presentation.adapters.MyPagerAdapter

class MainFragment : Fragment() {
    private var isRedirected = false
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: MyPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            isRedirected = true
            findNavController().navigate(R.id.action_mainFragment_to_homePage)
        } else {
            adapter = MyPagerAdapter(this)
            binding.viewPager.adapter = adapter

            binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateSwipeIndicators(position)
                }
            })

            binding.viewPager.post {
                updateSwipeIndicators(0)
            }
        }
    }

    private fun updateSwipeIndicators(position: Int) {
        binding.viewPager.post {
            val fragmentTag = "f$position"
            val currentFragment = childFragmentManager.findFragmentByTag(fragmentTag)

            when (position) {
                0 -> {
                    (currentFragment as? CustomerTypeSelection)?.showSwipeUp()

                    val nextFragmentTag = "f1"
                    val nextFragment = childFragmentManager.findFragmentByTag(nextFragmentTag)
                    (nextFragment as? MenuFragment)?.hideSwipe()
                }
                1 -> {
                    (currentFragment as? MenuFragment)?.showSwipeDown()

                    val prevFragmentTag = "f0"
                    val prevFragment = childFragmentManager.findFragmentByTag(prevFragmentTag)
                    (prevFragment as? CustomerTypeSelection)?.hideSwipe()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isRedirected) {
            binding.viewPager.visibility = View.GONE
        }
    }
}