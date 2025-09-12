package com.nihatmahammadli.abbmobile.presentation.screens

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentShowMyCardBinding
import com.nihatmahammadli.abbmobile.presentation.viewmodel.CardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowMyCard : Fragment() {
    private lateinit var binding: FragmentShowMyCardBinding
    private val viewModel: CardViewModel by activityViewModels()
    private var isCvvVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowMyCardBinding.inflate(inflater, container, false)

        binding.cardVisual.visibility = View.INVISIBLE

        fetchCards()
        setUpObserver()
        goBack()
        setupCvvToggle()
        setupCardCopy()

        return binding.root
    }

    private fun fetchCards() {
        viewModel.fetchCardsFromFirebase()
    }

    private fun setUpObserver() {
        viewModel.uiCards.observe(viewLifecycleOwner) { cards ->
            cards?.firstOrNull()?.let { card ->
                binding.cardNumberFull.text = card.cardNumber
                binding.cardCvv.text = "•••"
                binding.cardExpiry.text = card.expiryDate

                animateCardEntrance(binding.cardVisual)
            }
        }
    }

    private fun goBack() {
        binding.closeButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupCvvToggle() {
        binding.cvvVisibilityToggle.setOnClickListener {
            viewModel.uiCards.value?.firstOrNull()?.let { card ->
                if (isCvvVisible) {
                    binding.cardCvv.text = "•••"
                    binding.cvvVisibilityToggle.setImageResource(R.drawable.eye_closed)
                } else {
                    binding.cardCvv.text = card.cvv
                    binding.cvvVisibilityToggle.setImageResource(R.drawable.eye_open)
                }
                isCvvVisible = !isCvvVisible

                animateCvvToggle()
            }
        }
    }

    private fun setupCardCopy() {
        binding.copyCardIcon.setOnClickListener {
            copyCardNumber()
        }

        binding.cardNumberFull.setOnLongClickListener {
            copyCardNumber()
            true
        }
    }

    private fun copyCardNumber() {
        viewModel.uiCards.value?.firstOrNull()?.let { card ->
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Card Number", card.cardNumber)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(requireContext(), "Card number copied", Toast.LENGTH_SHORT).show()

            animateCopyFeedback()
        }
    }

    private fun animateCardEntrance(cardView: View) {
        cardView.translationX = -cardView.width.toFloat() * 0.8f
        cardView.translationY = cardView.height.toFloat() * 0.6f
        cardView.rotation = -15f
        cardView.alpha = 0f
        cardView.scaleX = 0.8f
        cardView.scaleY = 0.8f
        cardView.visibility = View.VISIBLE

        startCardAnimation(cardView)
    }

    private fun startCardAnimation(cardView: View) {
        val animatorSet = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(cardView, "translationX", 0f).apply {
                    duration = 1000
                    interpolator = OvershootInterpolator(0.8f)
                },
                ObjectAnimator.ofFloat(cardView, "translationY", 0f).apply {
                    duration = 1000
                    interpolator = OvershootInterpolator(0.8f)
                },
                ObjectAnimator.ofFloat(cardView, "rotation", 0f).apply {
                    duration = 1000
                    interpolator = OvershootInterpolator(0.6f)
                },
                ObjectAnimator.ofFloat(cardView, "alpha", 1f).apply {
                    duration = 800
                    interpolator = DecelerateInterpolator()
                },
                ObjectAnimator.ofFloat(cardView, "scaleX", 1f).apply {
                    duration = 1000
                    interpolator = OvershootInterpolator(0.4f)
                },
                ObjectAnimator.ofFloat(cardView, "scaleY", 1f).apply {
                    duration = 1000
                    interpolator = OvershootInterpolator(0.4f)
                }
            )
        }

        val floatingAnimator = ObjectAnimator.ofFloat(cardView, "translationY", 0f, -8f, 0f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = DecelerateInterpolator()
            startDelay = 1200
        }

        animatorSet.start()
        floatingAnimator.start()
        addShadowEffect(cardView)
    }

    private fun addShadowEffect(cardView: View) {
        ObjectAnimator.ofFloat(cardView, "elevation", 4f, 12f, 8f).apply {
            duration = 1000
            interpolator = DecelerateInterpolator()
        }.start()
    }

    private fun animateCopyFeedback() {
        val scaleUp = ObjectAnimator.ofFloat(binding.copyCardIcon, "scaleX", 1f, 1.3f).apply {
            duration = 150
        }
        val scaleDown = ObjectAnimator.ofFloat(binding.copyCardIcon, "scaleX", 1.3f, 1f).apply {
            duration = 150
        }
        val scaleUpY = ObjectAnimator.ofFloat(binding.copyCardIcon, "scaleY", 1f, 1.3f).apply {
            duration = 150
        }
        val scaleDownY = ObjectAnimator.ofFloat(binding.copyCardIcon, "scaleY", 1.3f, 1f).apply {
            duration = 150
        }

        AnimatorSet().apply {
            play(scaleUp).with(scaleUpY)
            play(scaleDown).with(scaleDownY).after(scaleUp)
            start()
        }

        val flashAnimator =
            ObjectAnimator.ofFloat(binding.cardNumberFull, "alpha", 1f, 0.5f, 1f).apply {
                duration = 300
            }
        flashAnimator.start()
    }

    private fun animateCvvToggle() {
        val rotateAnimator =
            ObjectAnimator.ofFloat(binding.cvvVisibilityToggle, "rotation", 0f, 360f).apply {
                duration = 300
                interpolator = DecelerateInterpolator()
            }

        val scaleAnimator =
            ObjectAnimator.ofFloat(binding.cvvVisibilityToggle, "scaleX", 1f, 1.2f, 1f).apply {
                duration = 300
            }
        val scaleYAnimator =
            ObjectAnimator.ofFloat(binding.cvvVisibilityToggle, "scaleY", 1f, 1.2f, 1f).apply {
                duration = 300
            }

        AnimatorSet().apply {
            playTogether(rotateAnimator, scaleAnimator, scaleYAnimator)
            start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.cardVisual.clearAnimation()
    }
}