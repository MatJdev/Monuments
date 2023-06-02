package com.example.practica5.ui.fragments.monuments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica5.R
import com.example.practica5.databinding.FragmentMonumentsBinding
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.ui.adapter.CountryAdapter
import com.example.practica5.ui.adapter.MonumentsListAdapter
import com.example.practica5.utils.MonumentsConstant.FIRST_LIST_POSITION
import com.example.practica5.utils.MonumentsConstant.MONUMENT_ID
import com.example.practica5.utils.MonumentsConstant.MONUMENT_NAME
import com.example.practica5.utils.MonumentsConstant.SORTED_EAST_WEST
import com.example.practica5.utils.MonumentsConstant.SORTED_NORTH_SOUTH
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MonumentsFragment : Fragment() {

    private var _binding: FragmentMonumentsBinding? = null
    private val binding get() = _binding
    private val monumentsViewModel: MonumentsViewModel by activityViewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }
    private val adapter: MonumentsListAdapter by lazy {
        MonumentsListAdapter(
            { monument -> onClickItemSelected(monument) },
            { monument -> onFavoriteSelected(monument) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMonumentsBinding.inflate(inflater, container, false)

        binding?.let { binding ->
            with(binding) {
                monumentsList.adapter = adapter
            }
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        monumentsViewModel.monumentsListLiveData.observe(viewLifecycleOwner) { monuments ->
            adapter.submitList(monuments) { scrollToFirstPosition() }
        }
        initToolbarMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onFavoriteSelected(monument: MonumentVO) {
        monumentsViewModel.updateFavoriteMonument(monument.id, monument.isFavorite)
    }

    private fun onClickItemSelected(monument: MonumentVO) {

    }

    private fun scrollToFirstPosition() {
        binding?.monumentsList?.scrollToPosition(FIRST_LIST_POSITION)
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
        val dialogView = createDialogView()
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.sortDialogRadioGroup)
        val alertDialog = createAlertDialog(dialogView)

        dialogView.findViewById<Button>(R.id.dialogBtnAccept)?.setOnClickListener {
            handleAcceptButtonClicked(alertDialog, radioGroup.checkedRadioButtonId)
        }

        dialogView.findViewById<Button>(R.id.dialogBtnClose)?.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    @SuppressLint("InflateParams")
    private fun createDialogView(): View {
        return LayoutInflater.from(requireContext()).inflate(R.layout.custom_sort_dialog, null)
    }

    private fun createAlertDialog(dialogView: View): AlertDialog {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
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
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.custom_filter_country_dialog, null)
        dialogBuilder.setView(dialogView)

        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.filterDialogListCounties)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val countryAdapter = CountryAdapter { country ->
            alertDialog.dismiss()
            monumentsViewModel.getFilteredMonumentsByCountry(country)
        }
        recyclerView.adapter = countryAdapter
        countryAdapter.submitList(countries)

        dialogView.findViewById<Button>(R.id.dialogBtnClose)?.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}