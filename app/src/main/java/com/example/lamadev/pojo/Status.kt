package com.example.lamadev.pojo


data class Status(

    val id:Int,

    val user:UserRequest,

    val stories: List<StoryDtoReq>? = ArrayList()

)
