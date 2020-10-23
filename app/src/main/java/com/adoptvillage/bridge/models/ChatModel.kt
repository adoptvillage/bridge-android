package com.adoptvillage.bridge.models

import android.content.Context
import com.adoptvillage.bridge.utils.formatAsHeader
import java.util.*

interface ChatModel{
    val sentAt: Date
}

data class Message(
    val msg:String,
    val senderId:String,
    val msgID:String,
    val type:String,
    val imageNumberId:Int,
    val pdfNumberId:Int,
    override val sentAt: Date = Date()
):ChatModel{
    constructor():this("", "", "", "",0,0, Date())
}

data class DateHeader(
    override val sentAt: Date = Date(), val context: Context
):ChatModel{
    val date:String = sentAt.formatAsHeader(context)
}