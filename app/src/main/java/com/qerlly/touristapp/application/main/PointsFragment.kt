package com.qerlly.touristapp.application.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.qerlly.touristapp.R
import com.qerlly.touristapp.application.main.adapters.FaqListAdapter
import com.qerlly.touristapp.application.main.viewmodels.PointsViewModel
import com.qerlly.touristapp.databinding.PointsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import androidx.recyclerview.widget.LinearLayoutManager

@AndroidEntryPoint
class PointsFragment : Fragment() {
    private var binding: PointsFragmentBinding? = null
    private val viewModel: PointsViewModel by viewModels()
    companion object {
        fun newInstance() = PointsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val adapter = FaqListAdapter(viewModel::onCardClicked)
        val manager = LinearLayoutManager(requireContext())
        binding?.points?.setLayoutManager(manager)
        binding?.points?.adapter = adapter
        viewModel.faqState.onEach {
            if (it == null) {
                binding?.pointsProgress?.visibility = View.VISIBLE
                binding?.points?.visibility = View.GONE
            } else {
                binding?.pointsProgress?.visibility = View.GONE
                binding?.points?.visibility = View.VISIBLE
                adapter.submitList(it)
            }
        }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
        return inflater.inflate(R.layout.points_fragment, container, false)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}