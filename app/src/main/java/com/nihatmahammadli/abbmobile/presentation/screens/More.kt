package com.nihatmahammadli.abbmobile.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentMoreBinding
import com.nihatmahammadli.abbmobile.presentation.adapters.MoreButtonsAdapter
import com.nihatmahammadli.abbmobile.presentation.adapters.MorePagerAdapter
import com.nihatmahammadli.abbmobile.presentation.components.dummyData.MoreButtonDummy

class More : Fragment() {
    private lateinit var binding: FragmentMoreBinding
    private lateinit var adapter: MoreButtonsAdapter
    private lateinit var pagerAdapter: MorePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(inflater,container,false)

        initUI()
        return binding.root
    }

    fun initUI(){
        setUpAdapter()
    }
    fun setUpAdapter(){
        pagerAdapter = MorePagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        adapter = MoreButtonsAdapter(MoreButtonDummy.btnList) { position ->
            binding.viewPager.setCurrentItem(position,true)
        }
        binding.buttonsRcyView.adapter = adapter
        binding.buttonsRcyView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                adapter.setSelected(position)
            }
    })
    }
}