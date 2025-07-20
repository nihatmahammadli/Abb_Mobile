package com.nihatmahammadli.abbmobile.presentation.dashboard.card_view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import com.nihatmahammadli.abbmobile.databinding.FragmentCardViewBinding
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.presentation.viewmodel.CardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardView : Fragment() {
    private lateinit var binding: FragmentCardViewBinding
    private val viewModel : CardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCardViewBinding.inflate(inflater, container, false)

        val cardView = binding.cardImage
        cardView.post {
            animateCardWithBackground(cardView)
        }

        observeViewModel()
        goTopUp()
        goBack()
        return binding.root
    }

    fun goBack(){
        binding.leftBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    @SuppressLint("SetTextI18n")
    fun observeViewModel(){
            viewModel.fetchCardsWithBalances()

            viewModel.uiCards.observe(viewLifecycleOwner) { cards ->
                if (!cards.isNullOrEmpty()) {
                    val firstCard = cards[0] // ilk kartı göstərək

                    binding.cardInfoBtn.text = "${firstCard.expiryDate}   •• ${firstCard.cardNumber.takeLast(4)}"
                    binding.amount.text = "${firstCard.balance} AZN"
                } else {
                    binding.cardInfoBtn.text = "•••• •••• •••• ••••"
                    binding.amount.text = "0.00 AZN"
            }
        }
    }

    fun goTopUp(){
        binding.topUpLayout.setOnClickListener {
            findNavController().navigate(R.id.action_cardView_to_topUp)
        }
        binding.topUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_cardView_to_topUp)
        }
    }

    private fun animateCardWithBackground(cardView: View) {
        cardView.translationX = -cardView.width.toFloat() * 0.8f
        cardView.translationY = cardView.height.toFloat() * 0.6f
        cardView.rotation = -15f
        cardView.alpha = 0f
        cardView.scaleX = 0.8f
        cardView.scaleY = 0.8f
        cardView.visibility = View.VISIBLE

        Glide.with(cardView.context)
            .load("https://abb-bank.az/storage/uploads/files/1735458510_tam-digi.png")
            .transition(DrawableTransitionOptions.withCrossFade(400))
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    cardView.background = resource
                    startCardAnimation(cardView)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    cardView.background = placeholder
                }
            })
    }

    private fun startCardAnimation(cardView: View) {
        val translationXAnimator = ObjectAnimator.ofFloat(cardView, "translationX", 0f).apply {
            duration = 1000
            interpolator = OvershootInterpolator(0.8f)
        }

        val translationYAnimator = ObjectAnimator.ofFloat(cardView, "translationY", 0f).apply {
            duration = 1000
            interpolator = OvershootInterpolator(0.8f)
        }

        val rotationAnimator = ObjectAnimator.ofFloat(cardView, "rotation", 0f).apply {
            duration = 1000
            interpolator = OvershootInterpolator(0.6f)
        }

        val alphaAnimator = ObjectAnimator.ofFloat(cardView, "alpha", 1f).apply {
            duration = 800
            interpolator = DecelerateInterpolator()
        }

        val scaleXAnimator = ObjectAnimator.ofFloat(cardView, "scaleX", 1f).apply {
            duration = 1000
            interpolator = OvershootInterpolator(0.4f)
        }

        val scaleYAnimator = ObjectAnimator.ofFloat(cardView, "scaleY", 1f).apply {
            duration = 1000
            interpolator = OvershootInterpolator(0.4f)
        }

        val floatingAnimator = ObjectAnimator.ofFloat(cardView, "translationY", 0f, -8f, 0f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = DecelerateInterpolator()
            startDelay = 1200
        }

        val mainAnimatorSet = AnimatorSet().apply {
            playTogether(
                translationXAnimator,
                translationYAnimator,
                rotationAnimator,
                alphaAnimator,
                scaleXAnimator,
                scaleYAnimator
            )
        }

        // Start animations
        mainAnimatorSet.start()
        floatingAnimator.start()

        // Add a subtle shadow animation
        addShadowEffect(cardView)
    }

    private fun addShadowEffect(cardView: View) {
        val shadowAnimator = ObjectAnimator.ofFloat(cardView, "elevation", 4f, 12f, 8f).apply {
            duration = 1000
            interpolator = DecelerateInterpolator()
        }
        shadowAnimator.start()
    }
}