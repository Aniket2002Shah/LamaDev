package com.example.lamadev.pojo

import java.util.*


data class Notification(

    val id: Int,

    val type: Int,

    val type_id: Int,

    val content: String,

    val user: UserRequest?,

    val users: Set<UserRequest>? = HashSet<UserRequest>(),

    val createdAt: Date,

    val updatedAt: Date,

    val isRead:Boolean
)