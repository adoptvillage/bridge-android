package com.adoptvillage.bridge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.activity.OnClicked
import com.adoptvillage.bridge.models.ChatModel
import com.adoptvillage.bridge.models.DateHeader
import com.adoptvillage.bridge.models.Message
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_chat_pdf_sent.view.*
import kotlinx.android.synthetic.main.list_item_chat_image_send.view.*
import kotlinx.android.synthetic.main.list_item_chat_received_message.view.*
import kotlinx.android.synthetic.main.list_item_chat_send_message.view.tvMainMessage


class ChatAdapter (private val list: MutableList<ChatModel>,
                   private val currentUid: String,
                   private var onClicked: OnClicked
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
            IMAGE_SENT->{
                ImageViewHolder(inflate(R.layout.list_item_chat_image_send))
            }
            IMAGE_RECEIVED->{
                ImageViewHolder(inflate(R.layout.list_item_chat_image_receive))
            }
            PDF_SENT->{
                PdfViewHolder(inflate(R.layout.item_list_chat_pdf_sent))
            }
            PDF_RECEIVED->{
                PdfViewHolder(inflate(R.layout.item_list_chat_pdf_receive))
            }
            else -> MessageViewHolder(inflate(R.layout.list_item_chat_received_mssg))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is Message -> {
                when (item.type) {
                    "TEXT" -> {
                        holder.itemView.apply {
                            tvMainMessage.text = item.msg
                            if (item.senderId!=currentUid){
                                when (item.senderId) {
                                    DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.donorId!! -> {
                                        tvSenderRole.text="Donor"
                                    }
                                    DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.moderatorId!! -> {
                                        tvSenderRole.text="Moderator"
                                    }
                                    DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.recipientId!! -> {
                                        tvSenderRole.text = "Recipient"
                                    }
                                }
                            }
                        }
                    }
                    "IMAGE" -> {
                        holder.itemView.apply {
                            Picasso.get().load(item.msg).placeholder(R.drawable.ic_baseline_image_24).into(ivCAImage)
                            ivCAImage.setOnClickListener {
                                onClicked.onImageClicked(item.msg,item.msgID)
                            }
                            ibCAImageDownload.setOnClickListener {
                                onClicked.onImageDownloadClicked(item.msg,item.msgID)
                            }
                        }
                    }
                    "PDF" -> {
                        holder.itemView.apply {
                            ivCAPdf.setOnClickListener {
                                onClicked.onPdfClicked(item.msg,item.msgID)
                            }
                            ibCAPdfDownload.setOnClickListener {
                                onClicked.onPdfDownloadClicked(item.msg,item.msgID)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size


    override fun getItemViewType(position: Int): Int {
        return when (val event = list[position]) {
            is Message -> {
                if (event.senderId == currentUid) {
                    when (event.type) {
                        "TEXT" -> {
                            TEXT_MESSAGE_SENT
                        }
                        "IMAGE" -> {
                            IMAGE_SENT
                        }
                        else -> {
                            PDF_SENT
                        }
                    }
                } else {
                    when (event.type) {
                        "TEXT" -> {
                            TEXT_MESSAGE_RECEIVED
                        }
                        "IMAGE" -> {
                            IMAGE_RECEIVED
                        }
                        else -> {
                            PDF_RECEIVED
                        }
                    }
                }
            }
            is DateHeader -> DATE_HEADER
            else -> UNSUPPORTED
        }
    }

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class PdfViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        private const val UNSUPPORTED = -1
        private const val TEXT_MESSAGE_RECEIVED = 0
        private const val TEXT_MESSAGE_SENT = 1
        private const val DATE_HEADER = 2
        private const val IMAGE_SENT=3
        private const val IMAGE_RECEIVED=4
        private const val PDF_SENT=5
        private const val PDF_RECEIVED=6
    }
}

