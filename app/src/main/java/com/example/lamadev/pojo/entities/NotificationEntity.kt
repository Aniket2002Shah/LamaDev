package com.example.lamadev.pojo.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notification")
data class NotificationEntity(

    @PrimaryKey(autoGenerate = false)
    val id: Int,

    val type: Int,

    val type_id: Int,

    val content: String,

    val createdAt: Date,

    val updatedAt: Date,

    val isRead:Boolean,

    val unSeen:Boolean
)
