package com.example.practica5.ui.fragments.map

import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.practica5.R
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.utils.MonumentsConstant.EMPTY_INFO
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

class MapsViewModel : ViewModel() {

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
        monument: MonumentVO,
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