package com.example.lamadev.pojo.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lamadev.pojo.UserRequest
import java.util.Date

@Entity(tableName = "messages")
data class MessageEntity(

    @PrimaryKey(autoGenerate = false)
    val id:Int,

    val content:String,

    val createdAt:Date,

    val user: UserRequest,

    val chatId:Int,

    val unSeen: Boolean
)
