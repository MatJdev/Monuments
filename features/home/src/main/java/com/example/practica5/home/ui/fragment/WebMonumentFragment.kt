package com.example.practica5.home.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.practica5.home.R
import com.example.practica5.common.util.MonumentsConstant.EMAIL_SUBJECT
import com.example.practica5.common.util.MonumentsConstant.MONUMENT_TARGET
import com.example.practica5.home.databinding.FragmentWebMonumentBinding
import com.example.practica5.home.ui.viewmodel.WebMonumentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebMonumentFragment : Fragment() {

    private val binding by lazy { FragmentWebMonumentBinding.inflate(layoutInflater) }
    private val webMonumentViewModel: WebMonumentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        webMonumentViewModel.getWebMonument().observe(viewLifecycleOwner) { monument ->
            if (monument != null) {
                initWebView()
                binding.webMonumentToolbar.title = monument.name
                initToolbarMenu(monument)
            }
        }
    }

    private fun initToolbar() {
        binding.webMonumentToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(binding.webMonumentToolbar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        with(binding) {
            webMonumentWebView.settings.javaScriptEnabled = true
            webMonumentWebView.webViewClient = WebViewClient()
            webMonumentWebView.loadUrl(webMonumentViewModel.getWebUrl(webMonumentWebView.context))
        }
    }

    private fun initToolbarMenu(monument: com.example.practica5.model.vo.MonumentVO) {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.google_maps_email_menu, menu)
                val item = menu.findItem(R.id.action_google_maps)
                item.isChecked = true
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_google_maps -> {
                        openGoogleMaps(monument)
                        true
                    }

                    R.id.action_email -> {
                        with(binding) {
                            val message = webMonumentViewModel.getEmailMessage(webMonumentWebView.context)
                            sendEmail(message)
                        }
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun openGoogleMaps(monument: com.example.practica5.model.vo.MonumentVO) {
        val bundle = Bundle().apply {
            putSerializable(MONUMENT_TARGET, monument)
        }
        findNavController().navigate(R.id.action_webMonumentFragment_to_mapsFragment, bundle)
    }

    private fun sendEmail(message: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT)
            putExtra(Intent.EXTRA_TEXT, message)
        }
        startActivity(intent)
    }
}