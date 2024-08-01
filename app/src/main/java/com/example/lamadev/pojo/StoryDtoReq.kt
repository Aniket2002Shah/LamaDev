package com.example.lamadev.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashSet

@Entity(tableName = "story")
data class StoryDtoReq(

    @PrimaryKey(autoGenerate = false)
    val id:Int,

    val content:String,

    val image:String,

    val statusId:Int,

    val likes:Set<UserRequest> =HashSet(),

    val createdAt:Date,

    val updatedAt: Date
)
