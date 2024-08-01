package com.example.lamadev.pojo

import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashSet

data class Story(

    val id: Int,

    val content:String,

    val image: String,

    val  status: StatusDtoReq,

    val likes:Set<UserRequest> = HashSet(),

    val  createdAt: Date,

    val updatedAt: Date
)
