package com.example.practica5.favorites.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.practica5.favorites.R
import com.example.practica5.common.R as RCom
import com.example.practica5.common.Resource
import com.example.practica5.mymonuments.ui.adapter.MyMonumentsAdapter
import com.example.practica5.common.util.MonumentsConstant.FAVORITES_TITLE
import com.example.practica5.common.util.MonumentsUtil.hideViews
import com.example.practica5.common.util.MonumentsUtil.showViews
import com.example.practica5.commonfeatures.util.ToolbarVisibilityListener
import com.example.practica5.favorites.databinding.FragmentFavoritesBinding
import com.example.practica5.favorites.ui.viewmodel.FavoritesViewModel
import com.example.practica5.commonfeatures.viewmodel.DetailViewModel
import com.example.practica5.model.vo.MonumentVO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private val binding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private val detailViewModel: DetailViewModel by activityViewModels()
    private val adapter: MyMonumentsAdapter by lazy {
        MyMonumentsAdapter { monument -> onClickItemSelected(monument) }
    }
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val destinationChangedListener = NavController.OnDestinationChangedListener { _, _, _ ->
            favoritesViewModel.getFavMonuments()
        }
        findNavController().addOnDestinationChangedListener(destinationChangedListener)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                favoritesViewModel.getResourceState().collect { resource ->
                    with(binding) {
                        when(resource) {
                            is Resource.Loading -> favoritesProgressBar.isVisible = true
                            is Resource.Success -> {
                                favoritesProgressBar.isVisible = false
                                adapter.submitList(resource.data)
                                setVisibilityUi(resource.data)
                            }
                            is Resource.Error -> {
                                favoritesProgressBar.isVisible = false
                                showErrorDialog(resource.error)
                            }
                        }
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
        toolbar?.title = FAVORITES_TITLE
    }

    private fun setUpRecyclerView() {
        binding.favoritesList.adapter = adapter
    }

    private fun onClickItemSelected(monument: MonumentVO) {
        detailViewModel.loadData(monument)
        findNavController().navigate(R.id.action_favFragment_to_detailFragment)
    }

    private fun setVisibilityUi(monuments: List<MonumentVO>) = with(binding) {
        if (monuments.isEmpty()) {
            showViews(favoritesImgLogo, favoritesLabelWithoutFavorites)

        } else {
            hideViews(favoritesImgLogo, favoritesLabelWithoutFavorites)
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