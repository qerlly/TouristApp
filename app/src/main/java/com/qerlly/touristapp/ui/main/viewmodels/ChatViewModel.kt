package com.qerlly.touristapp.ui.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qerlly.touristapp.ui.main.widgets.MessageItemUI
import com.qerlly.touristapp.infrastructure.android.NetworkReceiver
import com.qerlly.touristapp.infrastructure.android.NetworkState
import com.qerlly.touristapp.services.ChatService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatService: ChatService,
    private val networkReceiver: NetworkReceiver,
) : ViewModel() {

    var messages = mutableListOf<MessageItemUI>()

    var notifyNewMessageInserted: MutableStateFlow<Int> = MutableStateFlow(0)

    val networkState: StateFlow<NetworkState> = networkReceiver.state

    fun openChatChannel() {
        chatService.openChatChannel
            .onEach {
                messages.add(MessageItemUI(it, MessageItemUI.TYPE_FRIEND_MESSAGE))
                notifyNewMessageInserted.value = messages.size
            }
            .launchIn(viewModelScope)
    }


    fun sendMessage(message: String): Boolean {
        messages.add(MessageItemUI(message, MessageItemUI.TYPE_MY_MESSAGE))
        notifyNewMessageInserted.value = messages.size
        return true
       /* chatService.sendChatMessage.on
        sendChatMessageUseCase.getCompletable(SendMessageParams(message)).subscribeBy(onComplete = {
        })*/
    }

    override fun onCleared() {
        super.onCleared()
        networkReceiver.unregisterReceiver()
    }
}