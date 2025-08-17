package com.nihatmahammadli.abbmobile.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nihatmahammadli.abbmobile.presentation.screens.CustomerTypeSelection
import com.nihatmahammadli.abbmobile.presentation.screens.MenuFragment

class MyPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CustomerTypeSelection()
            1 -> MenuFragment()
            else -> CustomerTypeSelection()
        }
    }
}