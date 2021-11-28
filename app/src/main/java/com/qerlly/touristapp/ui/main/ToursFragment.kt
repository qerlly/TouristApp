package com.qerlly.touristapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.qerlly.touristapp.databinding.FragmentToursBinding
import com.qerlly.touristapp.ui.main.adapters.ToursListAdapter
import com.qerlly.touristapp.ui.main.viewmodels.ToursViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
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
        viewModel.getTours()

        binding?.apply {
            recyclerTours.apply {
                layoutManager = LinearLayoutManager(this@ToursFragment.requireContext())
                adapter = this@ToursFragment.adapter
            }
            viewModel.tours.filterNotNull().onEach {
                this@ToursFragment.adapter.differ.submitList(it)
            }.launchIn(lifecycleScope)
        }
        viewModel.getTour()
    }

    override fun onDestroy() {
        binding?.recyclerTours?.adapter = null
        binding = null
        super.onDestroy()
    }
}