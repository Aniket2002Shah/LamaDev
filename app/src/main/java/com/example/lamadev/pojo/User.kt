package com.example.lamadev.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "user")
data class User(

    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val username: String?,
    val password: String,
    val profilePicture: String?,
    val coverPicture: String?,
    val about: String?,
    val address: String?,
    val admin: Boolean?,
    val bio: String?,
    val createdAt: Date,
    val email: String,
    val gender: String?,
    val relationship: String?,
    val updatedAt: Date,
    val work: String?
)