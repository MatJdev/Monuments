package com.example.practica5.ui.fragments.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.practica5.R
import com.example.practica5.databinding.FragmentMapsBinding
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.ui.adapter.CustomInfoWindowAdapter
import com.example.practica5.ui.fragments.detail.DetailViewModel
import com.example.practica5.ui.fragments.monuments.MonumentsViewModel
import com.example.practica5.utils.MonumentsConstant.DENIED_PERMISSIONS
import com.example.practica5.utils.MonumentsConstant.LOCATION_PERMISSION_REQUEST_CODE
import com.example.practica5.utils.MonumentsConstant.MONUMENTS_TITLE
import com.example.practica5.utils.MonumentsConstant.MONUMENT_TARGET
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsFragment : Fragment() {

    private val monumentsViewModel: MonumentsViewModel by activityViewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }
    private val detailViewModel: DetailViewModel by activityViewModels()
    private val mapsViewModel: MapsViewModel by activityViewModels()

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding

    private lateinit var googleMap: GoogleMap

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        setupMarkers()
        setupMapListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
        }
        locationCallback = object : LocationCallback(){}

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        googleMap.isMyLocationEnabled = true
                        googleMap.uiSettings.isMyLocationButtonEnabled = false
                    }

                } else {
                    Toast.makeText(requireContext(), DENIED_PERMISSIONS, Toast.LENGTH_SHORT).show()
                }
            }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        binding?.mapsFabList?.setOnClickListener {
            findNavController().navigate(R.id.action_mapsFragment_to_nav_monuments)
        }
        val mainToolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        mainToolbar.title = MONUMENTS_TITLE
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(mainToolbar)
            activity.supportActionBar?.show()
            activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner){}
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        binding?.mapsFabCenterMap?.setOnClickListener {
            centerMapOnLocation()
        }
    }

    private fun getLatLngBounds(monumentsList: List<MonumentVO>?): LatLngBounds {
        val builder = LatLngBounds.Builder()

        monumentsList?.forEach { monument ->
            val latLng = LatLng(monument.location.latitude, monument.location.longitude)
            builder.include(latLng)
        }

        return builder.build()
    }

    private fun setupMarkers() {
        val monumentsList = getMonumentsList()
        val monumentTarget = arguments?.getSerializable(MONUMENT_TARGET) as? MonumentVO

        val infoWindowAdapter = CustomInfoWindowAdapter(requireContext())
        googleMap.setInfoWindowAdapter(infoWindowAdapter)

        monumentsList.forEach { monument ->
            lifecycleScope.launch(Dispatchers.IO) {
                binding?.let {
                    with(it) {
                        val latLng = LatLng(monument.location.latitude, monument.location.longitude)
                        val address = mapsViewModel.getAddressFromLocation(mapsFabCenterMap.context,monument.location.latitude, monument.location.longitude)
                        val markerIcon = mapsViewModel.getMarkerIcon(mapsFabCenterMap.context)
                        val markerOptions = mapsViewModel.createMarkerOptions(monument, latLng, markerIcon, address)
                        withContext(Dispatchers.Main) {
                            googleMap.addMarker(markerOptions)
                        }
                    }
                }
            }
        }
        val bounds = getLatLngBounds(monumentsList)
        val padding = 100

        if (monumentTarget != null) {
            val latLngTarget = LatLng(monumentTarget.location.latitude, monumentTarget.location.longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngTarget, 15f))
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
        }
    }

    private fun getMonumentsList(): List<MonumentVO> {
        val monumentsViewModel: MonumentsViewModel by activityViewModels {
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        }
        return monumentsViewModel.monumentsListLiveData.value.orEmpty()
    }

    private fun setupMapListeners() {
        googleMap.setOnMarkerClickListener { marker ->
            val markerPosition = marker.position
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(markerPosition))
            marker.showInfoWindow()
            true
        }

        googleMap.setOnInfoWindowClickListener { marker ->
            val monument = marker.title?.let { monumentsViewModel.getMonumentByTitle(it) }
            if (monument != null) {
                detailViewModel.loadData(monument)
                findNavController().navigate(R.id.action_mapsFragment_to_detailFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationUpdates()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun centerMapOnLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}