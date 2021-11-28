package com.qerlly.touristapp.ui.main.widgets

class MessageItemUI(val content:String,val messageType:Int){
    companion object {
        const val TYPE_MY_MESSAGE = 0
        const val TYPE_FRIEND_MESSAGE = 1
    }
}