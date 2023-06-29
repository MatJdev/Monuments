package com.example.practica5

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.example.practica5.domain.LocationHelper
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

class LocationHelperImpl @Inject constructor(private val context: Context) : LocationHelper {
    override suspend fun getCountryFromLocation(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var country: String? = null

        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                country = addresses[0].countryName
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return country ?: ""
    }

    override suspend fun getCountryCodeFromLocation(latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        if (!addresses.isNullOrEmpty()) {
            return addresses[0].countryCode
        }

        return null
    }
}