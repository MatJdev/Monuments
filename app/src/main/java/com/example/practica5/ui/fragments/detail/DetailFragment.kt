package com.example.practica5.ui.fragments.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.practica5.R
import com.example.practica5.databinding.FragmentDetailBinding
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.ui.adapter.PhotoAdapter
import com.example.practica5.ui.fragments.webMonument.WebMonumentViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback

class DetailFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding

    private val detailViewModel: DetailViewModel by activityViewModels()
    private val webMonumentViewModel: WebMonumentViewModel by activityViewModels()
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        detailViewModel.monumentLiveData.observe(viewLifecycleOwner) { monument ->
            drawMonumentInfo(monument)
            initListener(monument)
        }

        binding?.let {
            with(it) {
                mapView = detailMapViewStatic
                mapView.onCreate(savedInstanceState)

                mapView.getMapAsync(detailViewModel)
            }
        }
    }

    private fun drawMonumentInfo(monument: MonumentVO) {
        binding?.let {
            with(it) {
                detailToolbar.title = monument.name
                val urlList = monument.images
                detailViewPager.adapter = PhotoAdapter(urlList)
                detailLabelInfo.text = monument.description
                detailLabelCity.text = monument.city
                detailLabelName.text = monument.name
                Glide.with(detailViewPager.context).load(monument.countryFlag).into(detailImgFlag)
            }
        }
    }

    private fun initListener(monument: MonumentVO) {
        binding?.detailBtnInfoWeb?.setOnClickListener {
            webMonumentViewModel.init(monument)
            findNavController().navigate(R.id.action_detailFragment_to_webMonumentFragment)
        }
    }

    private fun initToolbar() {
        val toolbar = binding?.detailToolbar

        toolbar?.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        (activity as? AppCompatActivity)?.let { activity ->
            activity.supportActionBar?.hide()
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        detailViewModel.onMapReady(googleMap)
    }
}