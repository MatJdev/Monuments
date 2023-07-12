package com.example.practica5.commonfeatures.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.util.Locale

object NewMonumentUtils {
    fun getCountryCodeFromLocation(context: Context, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        if (!addresses.isNullOrEmpty()) {
            return addresses[0].countryCode
        }

        return null
    }
}