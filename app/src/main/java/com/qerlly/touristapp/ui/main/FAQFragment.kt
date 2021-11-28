package com.qerlly.touristapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.qerlly.touristapp.ui.main.adapters.FaqListAdapter
import com.qerlly.touristapp.ui.main.viewmodels.FaqViewModel
import com.qerlly.touristapp.databinding.FragmentFaqBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FAQFragment : Fragment() {

    private var binding: FragmentFaqBinding? = null

    private val viewModel: FaqViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFaqBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = FaqListAdapter(viewModel::onCardClicked)
        binding?.faqRecycler?.adapter = adapter
        viewModel.faqState.onEach {
            if (it == null) {
                binding?.faqProgress?.visibility = View.VISIBLE
                binding?.faqRecycler?.visibility = View.GONE
            } else {
                binding?.faqProgress?.visibility = View.GONE
                binding?.faqRecycler?.visibility = View.VISIBLE
                adapter.submitList(it)
            }
        }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}