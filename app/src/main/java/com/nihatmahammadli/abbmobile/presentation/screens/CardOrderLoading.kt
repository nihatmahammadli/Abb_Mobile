package com.nihatmahammadli.abbmobile.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentCardOrderLoadingBinding
import com.nihatmahammadli.abbmobile.presentation.viewmodel.CardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class CardOrderLoading : Fragment() {
    private lateinit var binding: FragmentCardOrderLoadingBinding

    private val viewModel: CardViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCardOrderLoadingBinding.inflate(inflater,container,false)
        binding.progressCircular.show()
        setupObservers()
        viewModel.fetchSingleCardFromApi()
        return binding.root
    }


    private fun setupObservers() {
        viewModel.cards.observe(viewLifecycleOwner) { cards ->
            if (cards.isNotEmpty()) {
                showToast("${cards.size} kart mövcuddur")
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressCircular.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.cardFetchResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                findNavController().navigate(R.id.action_cardOrderLoading_to_homePage)
            } else {
                showToast("Kartınız mövcuddur.")
                findNavController().navigate(R.id.action_cardOrderLoading_to_homePage)
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}