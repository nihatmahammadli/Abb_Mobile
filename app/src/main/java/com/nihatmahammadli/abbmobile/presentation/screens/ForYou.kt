package com.nihatmahammadli.abbmobile.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentForYouBinding
import com.nihatmahammadli.abbmobile.presentation.adapters.ForYouButtonAdapter
import com.nihatmahammadli.abbmobile.presentation.adapters.NewsAdapter
import com.nihatmahammadli.abbmobile.presentation.model.ForYouButton
import com.nihatmahammadli.abbmobile.presentation.model.NewsItem
import com.nihatmahammadli.abbmobile.presentation.viewmodel.ForYouViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForYou : Fragment() {
    private lateinit var binding: FragmentForYouBinding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var buttonsAdapter: ForYouButtonAdapter

    private val viewModel: ForYouViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForYouBinding.inflate(inflater, container, false)
        setUpNewsAdapter()
        setUpButtonAdapter()

        return binding.root
    }

    fun setUpNewsAdapter() {
        newsAdapter = NewsAdapter( mutableListOf(
            NewsItem(0,"Xəbərlər",R.drawable.news_1),
            NewsItem(1,"Sığorta",R.drawable.news_2),
            NewsItem(2,"Tətbiqdə yeniliklər",R.drawable.news_3),
            NewsItem(3,"Bilirdinizmi?",R.drawable.news_4),
            NewsItem(4,"İnvestisiya xəbərləri",R.drawable.news_5),
            NewsItem(5,"Partnyor endirimləri",R.drawable.news_6)
        ))
        binding.newRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.newRecyclerView.adapter = newsAdapter
    }



    fun setUpButtonAdapter() {
        buttonsAdapter = ForYouButtonAdapter()
        binding.secondRecyclerView.adapter = buttonsAdapter
        binding.secondRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.fetchTotalCashback()
        viewModel.cashbackTotal.observe(viewLifecycleOwner) { cashback ->
            val buttonList = listOf(
                ForYouButton("Cashback", "$cashback ₼", R.drawable.finance_ic),
                ForYouButton("ƏDV", "0.0 ₼", R.drawable.qr_ic),
                ForYouButton("Miles", "Order", R.drawable.miles_ic),

                )

            buttonsAdapter.setData(buttonList)
        }
    }




}