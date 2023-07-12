package com.example.practica5.map.ui.fragment

import android.Manifest
import android.content.Context
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.practica5.map.R
import com.example.practica5.model.vo.MonumentVO
import com.example.practica5.map.ui.adapter.CustomInfoWindowAdapter
import com.example.practica5.commonfeatures.viewmodel.CreateMonumentViewModel
import com.example.practica5.commonfeatures.viewmodel.DetailViewModel
import com.example.practica5.commonfeatures.viewmodel.MonumentsViewModel
import com.example.practica5.common.util.MonumentsConstant.DENIED_LOCATION_PERMISSIONS
import com.example.practica5.common.util.MonumentsConstant.FASTEST_UPDATE_INTERVAL_MILLIS
import com.example.practica5.common.util.MonumentsConstant.LOCATION_PERMISSION_REQUEST_CODE
import com.example.practica5.common.util.MonumentsConstant.MONUMENTS_TITLE
import com.example.practica5.common.util.MonumentsConstant.MONUMENT_TARGET
import com.example.practica5.common.util.MonumentsConstant.MONUMENT_ZOOM
import com.example.practica5.common.util.MonumentsConstant.PADDING_MAP
import com.example.practica5.common.util.MonumentsConstant.UPDATE_INTERVAL_MILLIS
import com.example.practica5.commonfeatures.util.ToolbarVisibilityListener
import com.example.practica5.map.databinding.FragmentMapsBinding
import com.example.practica5.map.ui.viewmodel.MapsViewModel
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private val monumentsViewModel: MonumentsViewModel by activityViewModels()
    private val detailViewModel: DetailViewModel by activityViewModels()
    private val mapsViewModel: MapsViewModel by viewModels()
    private val binding by lazy { FragmentMapsBinding.inflate(layoutInflater) }
    private val createMonumentViewModel: CreateMonumentViewModel by activityViewModels()

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private var toolbar: Toolbar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ToolbarVisibilityListener) {
            toolbar = context.showToolbar()
        }
    }

    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        setupMarkers()
        setupMapListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = UPDATE_INTERVAL_MILLIS
            fastestInterval = FASTEST_UPDATE_INTERVAL_MILLIS
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
                    Toast.makeText(requireContext(), DENIED_LOCATION_PERMISSIONS, Toast.LENGTH_SHORT).show()
                }
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapContainer) as SupportMapFragment?
        if (mapFragment == null) {
            val supportMapFragment = SupportMapFragment.newInstance()
            childFragmentManager.beginTransaction()
                .replace(R.id.mapContainer, supportMapFragment)
                .commit()
            childFragmentManager.executePendingTransactions()
            supportMapFragment.getMapAsync(callback)
        } else {
            mapFragment.getMapAsync(callback)
        }
        binding.mapsFabList.setOnClickListener {
            findNavController().navigate(R.id.action_mapsFragment_to_nav_monuments)
        }

        toolbar?.title = MONUMENTS_TITLE
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar?.show()
            activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner){}
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        binding.mapsFabCenterMap.setOnClickListener {
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
                with(binding) {
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
        val bounds = getLatLngBounds(monumentsList)
        val padding = PADDING_MAP

        if (monumentTarget != null) {
            val latLngTarget = LatLng(monumentTarget.location.latitude, monumentTarget.location.longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngTarget, MONUMENT_ZOOM))
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
        }
    }

    private fun getMonumentsList(): List<MonumentVO> {
        val monumentsViewModel: MonumentsViewModel by activityViewModels {
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        }
        return monumentsViewModel.getMonumentsList().value.orEmpty()
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

        googleMap.setOnMapLongClickListener { latLng ->
            createMonumentViewModel.loadLocation(latLng)
            findNavController().navigate(R.id.action_mapsFragment_to_createMonumentFragment)
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
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
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
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, MONUMENT_ZOOM))
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