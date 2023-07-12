package com.example.practica5.map.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.practica5.map.R
import com.example.practica5.common.util.MonumentsConstant.EMPTY_INFO
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(): ViewModel() {

    fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        return if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            val street = address.thoroughfare ?: EMPTY_INFO
            val number = address.subThoroughfare ?: EMPTY_INFO
            val cityName = address.locality ?: EMPTY_INFO
            val countryName = address.countryName ?: EMPTY_INFO
            "$street $number, $cityName, $countryName"
        } else {
            EMPTY_INFO
        }
    }

    fun getMarkerIcon(context: Context): Bitmap {
        return Glide.with(context)
            .asBitmap()
            .load(R.drawable.ic__monuments_marker_monument)
            .submit()
            .get()
    }

    fun createMarkerOptions(
        monument: com.example.practica5.model.vo.MonumentVO,
        latLng: LatLng,
        markerIcon: Bitmap,
        address: String
    ): MarkerOptions {
        val scaledMarkerIcon = Bitmap.createScaledBitmap(markerIcon, 170, 170, false)
        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(scaledMarkerIcon)
        return MarkerOptions()
            .position(latLng)
            .icon(bitmapDescriptor)
            .title(monument.name)
            .snippet(address)
    }
}