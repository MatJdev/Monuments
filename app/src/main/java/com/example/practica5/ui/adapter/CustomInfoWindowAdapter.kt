package com.example.practica5.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.practica5.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_info_window_monument, null)
        val monumentName = view.findViewById<TextView>(R.id.monumentName)
        val monumentAddress = view.findViewById<TextView>(R.id.monumentAddress)

        monumentName.text = marker.title
        monumentAddress.text = marker.snippet

        return view
    }
}