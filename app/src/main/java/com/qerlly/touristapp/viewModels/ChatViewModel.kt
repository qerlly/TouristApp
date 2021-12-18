package com.qerlly.touristapp.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import com.qerlly.touristapp.model.MessageModel
import com.qerlly.touristapp.services.SettingsService
import com.qerlly.touristapp.services.UserAuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    val messages = MutableStateFlow<MutableList<MessageModel>?>(null)

    private var tourId: String? = null

    private var root: DatabaseReference? = null

    init {
        scope.launch {
            tourId = settingsService.getTour().first()

            root = firebaseDatabase.reference.child(tourId!!)

            root!!.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    Timber.tag("ZXC").i("added")
                    appendChatConversation(snapshot)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    appendChatConversation(snapshot)
                    Timber.tag("ZXC").i("changed")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) { appendChatConversation(snapshot) }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { appendChatConversation(snapshot) }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    val photoToUpload: StateFlow<String> =
        settingsService.getPhoto()
            .filter { it.isNotEmpty() }
            .onEach {
                sendMessage(it, true)
                settingsService.setPhoto("")
            }
            .stateIn(scope, SharingStarted.Eagerly, "")

    @SuppressLint("SimpleDateFormat")
    fun sendMessage(message: String, image: Boolean = false) = tourId?.let {
        val map = hashMapOf<String, Any>()
        val key = root!!.push().key
        root!!.updateChildren(map)

        val messageAggregate = "$message :?: ${SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())} :?: $image"
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
            val newList  = mutableListOf<MessageModel>()

            messages.value?.forEach {
                newList.add(it)
            }

            newList.add(
                MessageModel(
                    email = email,
                    message = messageChunks[0],
                    time = messageChunks[1],
                    isImage = messageChunks[2].toBoolean(),
                    own = email == userAuthService.userEmail
                ),
            )

            messages.value = newList
        }
    }
}