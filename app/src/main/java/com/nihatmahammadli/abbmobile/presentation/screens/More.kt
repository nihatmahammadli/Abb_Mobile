package com.nihatmahammadli.abbmobile.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentMoreBinding
import com.nihatmahammadli.abbmobile.presentation.adapters.MoreButtonsAdapter
import com.nihatmahammadli.abbmobile.presentation.components.dummyData.MoreButtonDummy

class More : Fragment() {
    private lateinit var binding: FragmentMoreBinding
    private lateinit var adapter: MoreButtonsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        adapter = MoreButtonsAdapter(MoreButtonDummy.btnList) {}
        binding.buttonsRcyView.adapter = adapter
        binding.buttonsRcyView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

    }

}