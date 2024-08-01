package com.example.lamadev.pojo

data class GroupChatRequest(
    val id: Int,

    val userIds:List<Int>,

    val chat_name:String,

    val chat_image:String
)
