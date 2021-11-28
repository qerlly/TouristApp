package com.qerlly.touristapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.qerlly.touristapp.R
import com.qerlly.touristapp.databinding.FragmentChatBinding
import com.qerlly.touristapp.infrastructure.android.NetworkState
import com.qerlly.touristapp.ui.main.adapters.ChatListAdapter
import com.qerlly.touristapp.ui.main.viewmodels.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var binding: FragmentChatBinding? = null

    private val viewModel: ChatViewModel by viewModels()

    private var adapter: ChatListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ChatListAdapter(viewModel.messages)
        binding?.apply {
            viewModel.openChatChannel()
            notifyNewMessageInserted()
            sendButtonEnabled()
            sendMessages()
        }
    }

    private fun notifyNewMessageInserted() =
        viewModel.notifyNewMessageInserted.onEach {
            adapter?.notifyItemInserted(it)
        }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

    private fun FragmentChatBinding.sendButtonEnabled() {
        viewModel.networkState
            .onEach { state ->
                if (state == NetworkState.ENABLED) {
                    buttonGchatSend.isEnabled = true
                    buttonGchatSend.setBackgroundColor(
                        buttonGchatSend.context.resources.getColor(R.color.purple_500)
                    )
                } else {
                    buttonGchatSend.isEnabled = false
                    buttonGchatSend.setBackgroundColor(
                        buttonGchatSend.context.resources.getColor(R.color.layer_semi_dark)
                    )
                }
            }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    private fun FragmentChatBinding.sendMessages() {
        viewModel.networkState
            .onEach { state ->
                if (state == NetworkState.ENABLED) {
                    buttonGchatSend.setOnClickListener {
                        when {
                            editGchatMessage.text.isEmpty() -> showErrorEmptyMessageToast()
                            else -> {
                                if (viewModel.sendMessage(editGchatMessage.text.toString())) {
                                    editGchatMessage.text.clear()
                                } else showUnknownMessageToast()
                            }
                        }
                    }
                } else { showNoInternetMessageToast() }
            }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    private fun showErrorEmptyMessageToast() = Toast.makeText(
        activity, R.string.empty_message, Toast.LENGTH_SHORT,
    ).show()

    private fun showNoInternetMessageToast() = Toast.makeText(
        activity, R.string.no_internet, Toast.LENGTH_SHORT,
    ).show()

    private fun showUnknownMessageToast() = Toast.makeText(
        activity, R.string.unknown_error, Toast.LENGTH_SHORT,
    ).show()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}