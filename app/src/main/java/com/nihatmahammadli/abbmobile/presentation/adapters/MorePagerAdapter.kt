package com.nihatmahammadli.abbmobile.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nihatmahammadli.abbmobile.presentation.screens.viewPagerScreens.ProfileAndParameters

class MorePagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProfileAndParameters()
            1 -> ProfileAndParameters()
            2 -> ProfileAndParameters()
            3 -> ProfileAndParameters()
            else -> ProfileAndParameters()
        }
    }

    override fun getItemCount() = 4
}