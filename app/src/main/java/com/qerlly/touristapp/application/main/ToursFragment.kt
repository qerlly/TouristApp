package com.qerlly.touristapp.application.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.qerlly.touristapp.application.main.adapters.ToursListAdapter
import com.qerlly.touristapp.application.main.viewmodels.ToursViewModel
import com.qerlly.touristapp.databinding.FragmentToursBinding

class ToursFragment : Fragment() {

    private var binding: FragmentToursBinding? = null

    private val viewModel: ToursViewModel by viewModels()

    private val adapter = ToursListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToursBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*binding?.apply {
            recyclerTours.adapter = this@ToursFragment.adapter
            viewModel.tours
                .onEach {

                }
                .launchIn(lifecycleScope)
        }*/
    }

    override fun onDestroy() {
        binding?.recyclerTours?.adapter = null
        binding = null
        super.onDestroy()
    }
}