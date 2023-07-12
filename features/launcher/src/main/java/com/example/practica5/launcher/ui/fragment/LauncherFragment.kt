package com.example.practica5.launcher.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.practica5.common.util.MonumentsConstant.DELAY_MILLIS
import com.example.practica5.launcher.R
import com.example.practica5.launcher.databinding.FragmentLauncherBinding
import com.example.practica5.launcher.ui.viewmodel.LauncherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherFragment : Fragment() {

    private val binding by lazy { FragmentLauncherBinding.inflate(layoutInflater) }
    private val launcherViewModel: LauncherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launcherViewModel.navigateToDestinationLiveData.observe(viewLifecycleOwner) { destinationId ->
            destinationId?.let {
                findNavController().navigate(destinationId)
            }
        }
        launcherViewModel.navigateWithDelay(DELAY_MILLIS, R.id.go_to_home)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }
}