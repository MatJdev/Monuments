package com.example.practica5.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.practica5.databinding.CustomInfoWindowMonumentBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter(context: Context) : GoogleMap.InfoWindowAdapter {
    private val binding = CustomInfoWindowMonumentBinding.inflate(LayoutInflater.from(context))

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View = with(binding) {
        monumentName.text = marker.title
        monumentAddress.text = marker.snippet

        return root
    }
}