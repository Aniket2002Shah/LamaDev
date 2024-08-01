package com.example.lamadev.pojo

data class FollowerDto (
    val followers:List<UserRequest> =ArrayList(),
    val followings:List<UserRequest> = ArrayList()
        )