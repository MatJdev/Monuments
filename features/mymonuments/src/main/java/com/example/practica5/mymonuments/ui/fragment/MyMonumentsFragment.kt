package com.example.practica5.mymonuments.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.practica5.mymonuments.R
import com.example.practica5.common.R as RCom
import com.example.practica5.common.Resource
import com.example.practica5.model.vo.MonumentVO
import com.example.practica5.commonfeatures.viewmodel.CreateMonumentViewModel
import com.example.practica5.commonfeatures.viewmodel.DetailViewModel
import com.example.practica5.common.util.MonumentsConstant.DEFAULT_LOCATION_LATITUDE
import com.example.practica5.common.util.MonumentsConstant.DEFAULT_LOCATION_LONGITUDE
import com.example.practica5.common.util.MonumentsConstant.MY_MONUMENTS_TITLE
import com.example.practica5.common.util.MonumentsUtil.hideViews
import com.example.practica5.common.util.MonumentsUtil.showViews
import com.example.practica5.commonfeatures.util.ToolbarVisibilityListener
import com.example.practica5.mymonuments.databinding.FragmentMymonumentsBinding
import com.example.practica5.mymonuments.ui.adapter.MyMonumentsAdapter
import com.example.practica5.mymonuments.ui.viewmodel.MyMonumentsViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyMonumentsFragment : Fragment() {

    private val binding by lazy { FragmentMymonumentsBinding.inflate(layoutInflater) }
    private val myMonumentsViewModel: MyMonumentsViewModel by viewModels()
    private val adapter: MyMonumentsAdapter by lazy {
        MyMonumentsAdapter { monument -> onClickItemSelected(monument) }
    }
    private val detailViewModel: DetailViewModel by activityViewModels()
    private val createMonumentViewModel: CreateMonumentViewModel by activityViewModels()
    private var toolbar: Toolbar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ToolbarVisibilityListener) {
            toolbar = context.showToolbar()
        }
    }

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
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar?.show()
            activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner){}
        }
        toolbar?.title = MY_MONUMENTS_TITLE
    }

    private fun setUpRecyclerView() {
        binding.myMonumentsList.adapter = adapter
    }

    private fun onClickItemSelected(monument: MonumentVO) {
        detailViewModel.loadData(monument)
        findNavController().navigate(R.id.action_myMonumentFragment_to_detailFragment)
    }

    private fun initListeners() = with(binding) {
        myMonumentsFab.setOnClickListener {
            createMonumentViewModel.loadLocation(LatLng(DEFAULT_LOCATION_LATITUDE, DEFAULT_LOCATION_LONGITUDE))
            findNavController().navigate(R.id.action_myMonumentsFragment_to_newMonumentFragment)
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
            .setTitle(getString(RCom.string.common__dialog_error))
            .setMessage(errorMessage)
            .setPositiveButton(getString(RCom.string.common__dialog_accept), null)
            .create()
        alertDialog.show()
    }
}