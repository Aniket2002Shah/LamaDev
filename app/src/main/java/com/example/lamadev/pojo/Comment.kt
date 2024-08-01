package com.example.lamadev.pojo

import java.util.*

data class Comment(
    val id:Int,

    val content :String,

    val  user:UserRequest,

    val createAt: Date,

    val  updatedAt:Date
)
