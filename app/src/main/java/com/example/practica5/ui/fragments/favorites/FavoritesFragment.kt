package com.example.practica5.ui.fragments.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.practica5.R
import com.example.practica5.databinding.FragmentFavoritesBinding
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.ui.activity.NavigationActivity
import com.example.practica5.ui.adapter.MyMonumentsAdapter
import com.example.practica5.ui.fragments.detail.DetailViewModel
import com.example.practica5.utils.MonumentsConstant.FAVORITES_TITLE
import com.example.practica5.utils.MonumentsUtils.hideViews
import com.example.practica5.utils.MonumentsUtils.showViews

class FavoritesFragment : Fragment() {

    private val binding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }
    private val favoritesViewModel: FavoritesViewModel by activityViewModels()
    private val detailViewModel: DetailViewModel by activityViewModels()
    private val adapter: MyMonumentsAdapter by lazy {
        MyMonumentsAdapter { monument -> onClickItemSelected(monument) }
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

        favoritesViewModel.getFavMonumentsList().observe(viewLifecycleOwner) { monuments ->
            adapter.submitList(monuments)
            if (monuments != null) {
                setVisibilityUi(monuments)
            }
        }

        val destinationChangedListener = NavController.OnDestinationChangedListener { _, _, _ ->
            favoritesViewModel.getFavMonuments()
        }
        findNavController().addOnDestinationChangedListener(destinationChangedListener)
    }

    private fun initToolbar() {
        val mainToolbar = (requireActivity() as NavigationActivity).binding.appBarNavigation.toolbar
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(mainToolbar)
            activity.supportActionBar?.show()
            activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner){}
        }
        mainToolbar.title = FAVORITES_TITLE
    }

    private fun setUpRecyclerView() {
        binding.favoritesList.adapter = adapter
    }

    private fun onClickItemSelected(monument: MonumentVO) {
        detailViewModel.loadData(monument)
        findNavController().navigate(R.id.action_nav_favorites_to_detailFragment)
    }

    private fun setVisibilityUi(monuments: List<MonumentVO>) = with(binding) {
        if (monuments.isEmpty()) {
            showViews(favoritesImgLogo, favoritesLabelWithoutFavorites)

        } else {
            hideViews(favoritesImgLogo, favoritesLabelWithoutFavorites)
        }
    }
}