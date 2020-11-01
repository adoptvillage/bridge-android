package com.adoptvillage.bridge.models


interface ChatModel{
}

data class Message(
    val msg:String,
    val senderId:String,
    val msgID:String,
    val type:String,
    val imageNumberId:Int,
    val pdfNumberId:Int,

):ChatModel{
    constructor():this("", "", "", "",0,0)
}
