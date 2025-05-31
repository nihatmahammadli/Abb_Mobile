package com.nihatmahammadli.abbmobile.presentation.dashboard.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.nihatmahammadli.abbmobile.databinding.FragmentMainBinding
import com.nihatmahammadli.abbmobile.presentation.adapters.MyPagerAdapter

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        val adapter = MyPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

        return binding.root
    }
}