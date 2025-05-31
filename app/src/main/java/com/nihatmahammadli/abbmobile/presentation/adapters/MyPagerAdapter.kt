package com.nihatmahammadli.abbmobile.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nihatmahammadli.abbmobile.presentation.dashboard.onboarding.CustomerTypeSelection
import com.nihatmahammadli.abbmobile.presentation.dashboard.onboarding.MenuFragment


class MyPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment){
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CustomerTypeSelection()
            1 -> MenuFragment()
            else -> CustomerTypeSelection()
        }
    }
    override fun getItemCount(): Int = 2
}