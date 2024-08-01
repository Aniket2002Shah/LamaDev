package com.example.lamadev.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.HashSet

@Entity(tableName = "posts")
data class Post(

    @PrimaryKey(autoGenerate = false)
     val id:Int,

     val title:String,

     val content:String,

     val img : List<String> = ArrayList<String>(),

     val type: String,

     val view: String,

     val user:UserRequest,

     val likes: Set<UserRequest>  = HashSet<UserRequest>(),

     val comments: Set<Comment> = HashSet<Comment>(),

    val share :Set<UserRequest> = HashSet<UserRequest>(),

    val createdAt:Date,

    val updatedAt:Date
)