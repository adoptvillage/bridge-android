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
    val type:String = "TEXT",
    val status:Int = 1,
    val liked:Boolean = false,
    override val sentAt: Date = Date()
):ChatModel{
    constructor():this("", "", "", "", 1,false, Date())
}

data class DateHeader(
    override val sentAt: Date = Date(), val context: Context
):ChatModel{
    val date:String = sentAt.formatAsHeader(context)
}