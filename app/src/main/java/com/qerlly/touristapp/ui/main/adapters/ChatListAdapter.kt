package com.qerlly.touristapp.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.qerlly.touristapp.R
import com.qerlly.touristapp.ui.main.widgets.MessageItemUI
import com.qerlly.touristapp.ui.main.widgets.MessageItemUI.Companion.TYPE_FRIEND_MESSAGE
import com.qerlly.touristapp.ui.main.widgets.MessageItemUI.Companion.TYPE_MY_MESSAGE

class ChatListAdapter(var data: MutableList<MessageItemUI>) : RecyclerView.Adapter<ChatListAdapter.MessageViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder<*> {
        val context = parent.context
        return when (viewType) {
            TYPE_MY_MESSAGE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false)
                MyMessageViewHolder(view)
            }
            TYPE_FRIEND_MESSAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.message_from_item, parent, false)
                FriendMessageViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder<*>, position: Int) {
        val item = data[position]
        when (holder) {
            is MyMessageViewHolder -> holder.bindTo(item)
            is FriendMessageViewHolder -> holder.bindTo(item)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = data[position].messageType

    class MyMessageViewHolder(val view: View) : MessageViewHolder<MessageItemUI>(view) {
        private val messageContent = view.findViewById<TextView>(R.id.message)
        override fun bindTo(item: MessageItemUI) { messageContent.text = item.content }
    }

    class FriendMessageViewHolder(val view: View) : MessageViewHolder<MessageItemUI>(view) {
        private val messageContent = view.findViewById<TextView>(R.id.message)
        override fun bindTo(item: MessageItemUI) { messageContent.text = item.content }
    }

    abstract class MessageViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bindTo(item: T)
    }
}