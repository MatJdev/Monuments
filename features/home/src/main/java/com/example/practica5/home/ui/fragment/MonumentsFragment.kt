package com.example.practica5.home.ui.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.practica5.common.Resource
import com.example.practica5.home.R
import com.example.practica5.common.R as RC
import com.example.practica5.common.util.MonumentsConstant.FIRST_LIST_POSITION
import com.example.practica5.common.util.MonumentsConstant.MONUMENTS_TITLE
import com.example.practica5.common.util.MonumentsConstant.MONUMENT_ID
import com.example.practica5.common.util.MonumentsConstant.MONUMENT_NAME
import com.example.practica5.common.util.MonumentsConstant.SORTED_EAST_WEST
import com.example.practica5.common.util.MonumentsConstant.SORTED_NORTH_SOUTH
import com.example.practica5.home.databinding.CustomFilterCountryDialogBinding
import com.example.practica5.home.databinding.CustomSortDialogBinding
import com.example.practica5.home.databinding.FragmentMonumentsBinding
import com.example.practica5.home.ui.adapter.MonumentsListAdapter
import com.example.practica5.commonfeatures.viewmodel.DetailViewModel
import com.example.practica5.commonfeatures.viewmodel.MonumentsViewModel
import com.example.practica5.model.vo.MonumentVO
import com.example.practica5.home.ui.adapter.CountryAdapter
import com.example.practica5.commonfeatures.util.ToolbarVisibilityListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MonumentsFragment : Fragment() {

    private val binding by lazy { FragmentMonumentsBinding.inflate(layoutInflater) }
    private val monumentsViewModel: MonumentsViewModel by activityViewModels()
    private val adapter: MonumentsListAdapter by lazy {
        MonumentsListAdapter(
            { monument -> onClickItemSelected(monument) },
            { monument -> onFavoriteSelected(monument) }
        )
    }
    private val detailViewModel: DetailViewModel by activityViewModels()

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
        binding.monumentsList.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar?.show()
            activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner) {}
        }
        toolbar?.title = MONUMENTS_TITLE

        monumentsViewModel.getMonumentsList().observe(viewLifecycleOwner) { monuments ->
            adapter.submitList(monuments) { scrollToFirstPosition() }
        }
        initToolbarMenu()
        with(binding) {
            monumentsFab.setOnClickListener {
                findNavController().navigate(R.id.action_nav_monuments_to_mapsFragment)
            }
        }

        val destinationChangedListener = NavController.OnDestinationChangedListener { _, _, _ ->
            monumentsViewModel.getAllMonuments()
        }
        findNavController().addOnDestinationChangedListener(destinationChangedListener)

        monumentsViewModel.getResourceState().observe(viewLifecycleOwner) { resource ->
            with(binding) {
                when (resource) {
                    is Resource.Loading -> monumentsProgressBar.visibility = View.VISIBLE
                    is Resource.Success -> monumentsProgressBar.visibility = View.GONE
                    is Resource.Error -> {
                        monumentsProgressBar.visibility = View.GONE
                        showErrorDialog(resource.error)
                    }
                }
            }
        }
    }

    private fun onFavoriteSelected(monument: MonumentVO) {
        monumentsViewModel.updateFavoriteMonument(monument.id, monument.isFavorite)
    }

    private fun onClickItemSelected(monument: MonumentVO) {
        detailViewModel.loadData(monument)
        findNavController().navigate(R.id.action_monumentsFragment_to_detailFragment)
    }

    private fun scrollToFirstPosition() {
        binding.monumentsList.scrollToPosition(FIRST_LIST_POSITION)
    }

    private fun initToolbarMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.sort_filter_monuments_menu, menu)
                val item = menu.findItem(R.id.action_sort)
                item.isChecked = true
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_sort -> {
                        showSortMonumentDialog()
                        true
                    }

                    R.id.action_filter -> {
                        lifecycleScope.launch {
                            val countries = monumentsViewModel.getUniqueCountries()
                            showCountryDialog(countries)
                        }
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showSortMonumentDialog() {
        val binding = CustomSortDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val radioGroup = binding.sortDialogRadioGroup
        val alertDialog = createAlertDialog(binding)

        binding.dialogBtnAccept.setOnClickListener {
            handleAcceptButtonClicked(alertDialog, radioGroup.checkedRadioButtonId)
        }

        binding.dialogBtnClose.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun createAlertDialog(binding: CustomSortDialogBinding): AlertDialog {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return alertDialog
    }

    private fun handleAcceptButtonClicked(dialog: AlertDialog, selectedRadioButtonId: Int) {
        val radioBtnSelected = dialog.findViewById<RadioButton>(selectedRadioButtonId)
        when (radioBtnSelected?.id) {
            R.id.sortDialogRadioBtnId -> monumentsViewModel.getSortedMonuments(MONUMENT_ID)
            R.id.sortDialogRadioBtnName -> monumentsViewModel.getSortedMonuments(MONUMENT_NAME)
            R.id.sortDialogRadioBtnNtoS -> monumentsViewModel.getSortedMonuments(SORTED_NORTH_SOUTH)
            R.id.sortDialogRadioBtnEtoW -> monumentsViewModel.getSortedMonuments(SORTED_EAST_WEST)
        }
        dialog.dismiss()
    }

    private fun showCountryDialog(countries: List<String>) {
        val binding = CustomFilterCountryDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(binding.root)

        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val countryAdapter = CountryAdapter { country ->
            alertDialog.dismiss()
            monumentsViewModel.updateCountrySelected(country)
            monumentsViewModel.getFilteredMonumentsByCountry(country)
        }
        binding.filterDialogListCounties.adapter = countryAdapter
        countryAdapter.submitList(countries)

        binding.dialogBtnClose.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun showErrorDialog(errorMessage: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(RC.string.common__dialog_error))
            .setMessage(errorMessage)
            .setPositiveButton(getString(RC.string.common__dialog_accept), null)
            .create()
        alertDialog.show()
    }
}