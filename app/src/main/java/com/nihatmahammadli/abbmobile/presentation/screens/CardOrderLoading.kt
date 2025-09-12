package com.nihatmahammadli.abbmobile.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentCardOrderLoadingBinding
import com.nihatmahammadli.abbmobile.presentation.components.dummyData.LoadingTexts
import com.nihatmahammadli.abbmobile.presentation.viewmodel.CardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CardOrderLoading : Fragment() {
    private var _binding: FragmentCardOrderLoadingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardOrderLoadingBinding.inflate(inflater, container, false)
        binding.progressCircular.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        viewModel.fetchSingleCardFromApi()
        changeLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressCircular.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.cardFetchResult.observe(viewLifecycleOwner) { success ->
            viewLifecycleOwner.lifecycleScope.launch {
                if (success) {
                    findNavController().navigate(R.id.action_cardOrderLoading_to_homePage)
                } else {
                    showToast("Kartınız mövcuddur.")
                    findNavController().navigate(R.id.action_cardOrderLoading_to_homePage)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    private fun changeLoading() {
        val texts = LoadingTexts.getTexts(requireContext())
        var i = 0

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.text.text = texts[i % texts.size]
                while (true) {
                    delay(5000)
                    i++
                    binding.text.text = texts[i % texts.size]
                }
            }
        }
    }

}
