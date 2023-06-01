package com.example.practica5.ui.fragments.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practica5.domain.model.vo.LocationVO
import com.example.practica5.domain.model.vo.MonumentVO
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DetailViewModel : ViewModel(), OnMapReadyCallback {
    private val monumentMutableLiveData = MutableLiveData<MonumentVO>()
    val monumentLiveData: LiveData<MonumentVO> = monumentMutableLiveData

    private lateinit var googleMap: GoogleMap

    fun loadData(monument: MonumentVO) {
        monumentMutableLiveData.value = monument
    }

    private fun addMarker(location: LocationVO) {
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions().position(latLng)
        googleMap.addMarker(markerOptions)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.setAllGesturesEnabled(false)

        val monument = monumentLiveData.value
        monument?.let { addMarker(it.location) }
    }
}