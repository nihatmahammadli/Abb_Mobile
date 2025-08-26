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
import com.nihatmahammadli.abbmobile.presentation.adapters.HorizontalImageAdapter
import com.nihatmahammadli.abbmobile.presentation.adapters.NewsAdapter
import com.nihatmahammadli.abbmobile.presentation.adapters.OffersAdapter
import com.nihatmahammadli.abbmobile.presentation.components.decoration.FirstItemSpacingDecoration
import com.nihatmahammadli.abbmobile.presentation.components.dummyData.ImageListDummy
import com.nihatmahammadli.abbmobile.presentation.model.ForYouButton
import com.nihatmahammadli.abbmobile.presentation.model.NewsItem
import com.nihatmahammadli.abbmobile.presentation.viewmodel.ForYouViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForYou : Fragment() {
    private lateinit var binding: FragmentForYouBinding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var buttonsAdapter: ForYouButtonAdapter
    private lateinit var offersAdapter: OffersAdapter
    private val viewModel: ForYouViewModel by viewModels()

    val newsImageList = ImageListDummy.newsImageList
    val imageList = ImageListDummy.offersImageList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForYouBinding.inflate(inflater, container, false)
        setUpButtonAdapter()
        setUpAdapters()

        return binding.root
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

    fun setUpAdapters(){
        offersAdapter = OffersAdapter(imageList)
        newsAdapter = NewsAdapter(newsImageList)

        binding.offersRcyView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offersAdapter

            val spacing = resources.getDimensionPixelSize(R.dimen.item_spacing)
            addItemDecoration(FirstItemSpacingDecoration(spacing))
        }

        binding.newRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.newRecyclerView.adapter = newsAdapter
    }


}