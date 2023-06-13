package com.example.practica5.ui.fragments.mymonuments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.practica5.R
import com.example.practica5.databinding.FragmentMymonumentsBinding
import com.example.practica5.datasource.Resource
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.ui.activity.NavigationActivity
import com.example.practica5.ui.adapter.MyMonumentsAdapter
import com.example.practica5.ui.fragments.createMonument.CreateMonumentViewModel
import com.example.practica5.ui.fragments.detail.DetailViewModel
import com.example.practica5.utils.MonumentsConstant.DEFAULT_LOCATION_LATITUDE
import com.example.practica5.utils.MonumentsConstant.DEFAULT_LOCATION_LONGITUDE
import com.example.practica5.utils.MonumentsConstant.MY_MONUMENTS_TITLE
import com.example.practica5.utils.MonumentsUtils.hideViews
import com.example.practica5.utils.MonumentsUtils.showViews
import com.google.android.gms.maps.model.LatLng

class MyMonumentsFragment : Fragment() {

    private val binding by lazy { FragmentMymonumentsBinding.inflate(layoutInflater) }
    private val myMonumentsViewModel: MyMonumentsViewModel by activityViewModels()
    private val adapter: MyMonumentsAdapter by lazy {
        MyMonumentsAdapter { monument -> onClickItemSelected(monument) }
    }
    private val detailViewModel: DetailViewModel by activityViewModels()
    private val createMonumentViewModel: CreateMonumentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initToolbar()
        setUpRecyclerView()
        initListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myMonumentsViewModel.getMyMonumentsList().observe(viewLifecycleOwner) { monuments ->
            adapter.submitList(monuments)
            if (monuments != null) {
                setVisibilityUi(monuments)
            }
        }

        val destinationChangedListener = NavController.OnDestinationChangedListener { _, _, _ ->
            myMonumentsViewModel.getMyMonuments()
        }
        findNavController().addOnDestinationChangedListener(destinationChangedListener)

        myMonumentsViewModel.getResourceState().observe(viewLifecycleOwner) { resource ->
            with(binding) {
                when(resource) {
                    is Resource.Loading -> myMonumentsProgressBar.visibility = View.VISIBLE
                    is Resource.Success -> myMonumentsProgressBar.visibility = View.GONE
                    is Resource.Error -> {
                        myMonumentsProgressBar.visibility = View.GONE
                        showErrorDialog(resource.error)
                    }
                }
            }
        }
    }

    private fun initToolbar() {
        val mainToolbar = (requireActivity() as NavigationActivity).binding.appBarNavigation.toolbar
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(mainToolbar)
            activity.supportActionBar?.show()
            activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner){}
        }
        mainToolbar.title = MY_MONUMENTS_TITLE
    }

    private fun setUpRecyclerView() {
        binding.myMonumentsList.adapter = adapter
    }

    private fun onClickItemSelected(monument: MonumentVO) {
        detailViewModel.loadData(monument)
        findNavController().navigate(R.id.action_nav_my_monuments_to_detailFragment)
    }

    private fun initListeners() = with(binding) {
        myMonumentsFab.setOnClickListener {
            createMonumentViewModel.loadLocation(LatLng(DEFAULT_LOCATION_LATITUDE, DEFAULT_LOCATION_LONGITUDE))
            findNavController().navigate(R.id.action_nav_my_monuments_to_createMonumentFragment)
        }
    }

    private fun setVisibilityUi(monuments: List<MonumentVO>) = with(binding) {
        if (monuments.isEmpty()) {
            showViews(myMonumentsImgLogo, myMonumentsLabelWithoutMonuments)

        } else {
            hideViews(myMonumentsImgLogo, myMonumentsLabelWithoutMonuments)
        }
    }

    private fun showErrorDialog(errorMessage: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.common__dialog_error))
            .setMessage(errorMessage)
            .setPositiveButton(getString(R.string.common__dialog_accept), null)
            .create()
        alertDialog.show()
    }
}