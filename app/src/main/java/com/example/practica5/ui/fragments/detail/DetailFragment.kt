package com.example.practica5.ui.fragments.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.practica5.R
import com.example.practica5.databinding.FragmentDetailBinding
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.ui.adapter.PhotoAdapter
import com.example.practica5.ui.fragments.dialog.DeleteMonumentDialogFragment
import com.example.practica5.ui.fragments.webMonument.WebMonumentViewModel
import com.example.practica5.utils.MonumentsConstant.DELETE_DIALOG_TAG
import com.example.practica5.utils.MonumentsConstant.MONUMENT_ZOOM
import com.example.practica5.utils.MonumentsUtils.renderMonument
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DetailFragment : Fragment(), OnMapReadyCallback, DeleteMonumentDialogFragment.DeleteMonumentDialogListener {

    private val binding by lazy { FragmentDetailBinding.inflate(layoutInflater) }
    private val detailViewModel: DetailViewModel by activityViewModels()
    private val webMonumentViewModel: WebMonumentViewModel by activityViewModels()
    private lateinit var mapView: MapView
    private val adapter: PhotoAdapter by lazy { PhotoAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        webMonumentViewModel.clearLiveData()

        detailViewModel.getMonument().observe(viewLifecycleOwner) { monument ->
            binding.renderMonument(monument, adapter)
            initListener(monument)
        }

        with(binding) {
            mapView = detailMapViewStatic
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync(this@DetailFragment)
        }

        webMonumentViewModel.getWebMonument().observe(viewLifecycleOwner) { monument ->
            if (monument != null) {
                findNavController().navigate(R.id.action_detailFragment_to_webMonumentFragment)
            }
        }

        detailViewModel.getRemoveComplete().observe(viewLifecycleOwner) { isComplete ->
            if (isComplete) {
                detailViewModel.setDeleteToFalse()
                findNavController().navigate(R.id.action_detailFragment_to_nav_my_monuments)
            }
        }
    }

    private fun initListener(monument: MonumentVO) = with(binding) {
        detailBtnInfoWeb.setOnClickListener {
            webMonumentViewModel.loadMonument(monument)
        }
        detailBtnRemove.setOnClickListener {
            showDeleteMonumentDialog(monument)
        }
    }

    private fun initToolbar() {
        val toolbar = binding.detailToolbar

        toolbar.setNavigationOnClickListener {
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
        map.uiSettings.setAllGesturesEnabled(false)
        val monument = detailViewModel.getMonument().value
        monument?.let {
            val latLng = LatLng(it.location.latitude, it.location.longitude)
            map.addMarker(MarkerOptions().position(latLng))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MONUMENT_ZOOM))
        }
    }

    private fun showDeleteMonumentDialog(monument: MonumentVO) {
        val deleteDialogFragment = DeleteMonumentDialogFragment(monument)
        deleteDialogFragment.listener = this
        deleteDialogFragment.show(requireParentFragment().childFragmentManager, DELETE_DIALOG_TAG)
    }

    override fun onDialogAccept(monument: MonumentVO) {
        detailViewModel.deleteMonument(monument)
    }
}