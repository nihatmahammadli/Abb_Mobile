package com.nihatmahammadli.abbmobile.presentation.screens

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentServiceNetworkBinding
import com.nihatmahammadli.abbmobile.presentation.components.dummyData.AbbLocationsDummy

class ServiceNetwork : Fragment() {
    private lateinit var binding: FragmentServiceNetworkBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServiceNetworkBinding.inflate(inflater, container, false)
        goBack()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private val callback = OnMapReadyCallback { googleMap ->
        val abbLocations = AbbLocationsDummy.abbLocations
        for (location in abbLocations) {
            googleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(location.lat, location.lng))
                    .title(location.name)
                    .snippet(location.city)
            )
        }
        if (abbLocations.isNotEmpty()) {
            val first = abbLocations[0]
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(first.lat, first.lng),
                    6f
                )
            )
        }
    }

    private fun goBack(){
        binding.leftBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}