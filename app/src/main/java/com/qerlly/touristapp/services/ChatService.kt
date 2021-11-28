package com.qerlly.touristapp.services

import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ChatService @Inject constructor() {

    val openChatChannel = flowOf("Hello Mike")
    val sendChatMessage = flowOf<String>()
}