package com.adoptvillage.bridge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.models.ChatModel
import com.adoptvillage.bridge.models.DateHeader
import com.adoptvillage.bridge.models.Message
import com.adoptvillage.bridge.utils.formatAsTime
import kotlinx.android.synthetic.main.list_item_chat_send_message.view.*

class ChatAdapter (private val list: MutableList<ChatModel>,
                   private val currentUid: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = { layout: Int ->
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        }
        return when (viewType) {
            TEXT_MESSAGE_RECEIVED -> {
                MessageViewHolder(inflate(R.layout.list_item_chat_received_message))
            }
            TEXT_MESSAGE_SENT -> {
                MessageViewHolder(inflate(R.layout.list_item_chat_send_message))
            }
            DATE_HEADER -> {
                DateViewHolder(inflate(R.layout.list_item_date_header))
            }
            else -> MessageViewHolder(inflate(R.layout.list_item_chat_received_mssg))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is DateHeader -> {
                holder.itemView.tvMessageTime.text =
                    item.date //Already formatted in DateHeader data class.
            }
            is Message -> {
                holder.itemView.apply {
                    tvMainMessage.text = item.msg
                    tvMessageTime.text = item.sentAt.formatAsTime()
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size


    override fun getItemViewType(position: Int): Int {
        return when (val event = list[position]) {
            is Message -> {
                if (event.senderId == currentUid) {
                    TEXT_MESSAGE_SENT
                } else
                    TEXT_MESSAGE_RECEIVED
            }
            is DateHeader -> DATE_HEADER
            else -> UNSUPPORTED
        }
    }

    class DateViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        private const val UNSUPPORTED = -1
        private const val TEXT_MESSAGE_RECEIVED = 0
        private const val TEXT_MESSAGE_SENT = 1
        private const val DATE_HEADER = 2
    }
}
