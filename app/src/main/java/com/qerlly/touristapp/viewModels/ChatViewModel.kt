package com.qerlly.touristapp.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import com.qerlly.touristapp.model.MessageModel
import com.qerlly.touristapp.services.SettingsService
import com.qerlly.touristapp.services.UserAuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val userAuthService: UserAuthService,
    private val settingsService: SettingsService,
    private val firebaseDatabase: FirebaseDatabase
): ViewModel() {

    private val _messages = MutableStateFlow<MutableList<MessageModel>>(mutableListOf())

    val messages: StateFlow<List<MessageModel>> = _messages

    private var tourId: String? = null

    private var root: DatabaseReference? = null

    init {
        runBlocking {
            tourId = settingsService.getTour().firstOrNull()
        }
        tourId?.let {
            root = firebaseDatabase.reference.child(tourId!!)
        }
        viewModelScope.launch {
            root!!.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    appendChatConversation(snapshot)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    appendChatConversation(snapshot)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {}

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun sendMessage(message: String) = tourId?.let {
        val map = hashMapOf<String, Any>()
        val key = root!!.push().key
        root!!.updateChildren(map)

        val messageAggregate = "$message :?: ${SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())} :?: false"
        val messageRoot: DatabaseReference = root!!.child(key!!)
        val messageMap = hashMapOf<String, Any>(
            "email" to userAuthService.userEmail!!,
            "message" to messageAggregate
        )
        messageRoot.updateChildren(messageMap)
    }

    private fun appendChatConversation(dataSnapshot: DataSnapshot) {
        val iterator = dataSnapshot.children.iterator()
        while (iterator.hasNext()){
            val email: String = (iterator.next() as DataSnapshot).value.toString()
            val message: String = (iterator.next() as DataSnapshot).value.toString()
            val messageChunks = message.split(" :?: ")
            _messages.value.add(
                MessageModel(
                    email = email,
                    message = messageChunks[0],
                    time = messageChunks[1],
                    isImage = messageChunks[2].toBoolean(),
                    own = email == userAuthService.userEmail
                )
            )
        }
    }
}