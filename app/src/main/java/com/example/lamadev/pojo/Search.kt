package com.example.lamadev.pojo

import java.util.*

data class Search(
    val id: Int,

    val user: UserSearchReq,

    val content: String?,

    val createdAt: Date
        )