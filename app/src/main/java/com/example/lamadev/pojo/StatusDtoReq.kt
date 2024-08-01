package com.example.lamadev.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "status")
data class StatusDtoReq(

    @PrimaryKey(autoGenerate = false)
    val id: Int,

    val user: UserRequest
)
