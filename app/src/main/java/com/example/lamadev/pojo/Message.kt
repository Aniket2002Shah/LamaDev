package com.example.lamadev.pojo

import java.util.Date

data class Message(

    val id: Int?,

    val content:String,

    val createdAt: Date?,

    val user:UserRequest,

    val chat:ChatDetail
)
