package com.nihatmahammadli.abbmobile.presentation.dashboard.order_card

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentOrderNewCardBinding
import com.nihatmahammadli.abbmobile.presentation.adapters.AllCardsAdapter
import com.nihatmahammadli.abbmobile.presentation.adapters.CardAdapter
import com.nihatmahammadli.abbmobile.presentation.providers.CardProvider
import com.nihatmahammadli.abbmobile.presentation.providers.OrderCardProvider
import kotlin.math.abs

class OrderNewCard : Fragment() {
    private lateinit var binding: FragmentOrderNewCardBinding
    private lateinit var cardAdapter: AllCardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentOrderNewCardBinding.inflate(inflater,container,false)
        setUpAdapter()
        viewPagerOverlapEffect()
        return binding.root
    }

    fun setUpAdapter(){
        val cards = OrderCardProvider.getCards()

        val adapter = AllCardsAdapter(cards)
        binding.viewPager.adapter = adapter
    }

    fun viewPagerOverlapEffect() {
        binding.viewPager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            setPageTransformer { page, position ->
                val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)

                when {
                    position < -1 -> {
                        page.alpha = 0f
                    }
                    position <= 1 -> {
                        val startOffset = -position * offsetPx
                        page.translationX = startOffset

                        val scaleFactor = 1 - 0.1f * abs(position)
                        page.scaleX = scaleFactor
                        page.scaleY = scaleFactor

                        page.alpha = 1 - 0.3f * abs(position)
                    }
                    else -> {
                        page.alpha = 0f
                    }
                }
            }
        }
    }



}