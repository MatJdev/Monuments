package com.example.practica5.ui.fragments.launcher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.practica5.R
import androidx.navigation.fragment.findNavController
import com.example.practica5.databinding.FragmentLauncherBinding
import com.example.practica5.utils.MonumentsConstant.DELAY_MILLIS
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
        launcherViewModel.navigateWithDelay(DELAY_MILLIS, R.id.action_launcherFragment_to_nav_monuments)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }
}