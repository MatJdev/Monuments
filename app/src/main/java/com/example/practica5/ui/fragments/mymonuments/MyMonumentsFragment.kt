package com.example.practica5.ui.fragments.mymonuments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practica5.databinding.FragmentMymonumentsBinding

class MyMonumentsFragment : Fragment() {

    private val binding by lazy { FragmentMymonumentsBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }
}