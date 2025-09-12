package com.nihatmahammadli.abbmobile.presentation.screens.viewPagerScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentProfileAndParametersBinding
import com.nihatmahammadli.abbmobile.presentation.adapters.SettingsAdapter
import com.nihatmahammadli.abbmobile.presentation.components.dummyData.SettingsData
import com.nihatmahammadli.abbmobile.presentation.model.SettingsItem

class ProfileAndParameters : Fragment() {

    private var _binding: FragmentProfileAndParametersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileAndParametersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list: List<SettingsItem> = SettingsData.getList(requireContext())

        val adapter = SettingsAdapter(list) { item, position ->
            if (position == 2) {
                findNavController().navigate(R.id.action_more_to_changeLanguage)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
