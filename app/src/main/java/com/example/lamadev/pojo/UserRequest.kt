package com.example.lamadev.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserRequest(

    val id: Int,

    val username: String?,

    val profilePicture: String?,
):Parcelable
