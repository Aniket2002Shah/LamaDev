package com.example.lamadev.pojo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.HashSet

@Entity(tableName = "chats")
@Parcelize
data class Chat(

@PrimaryKey(autoGenerate = false)
val id:Int,

val chat_name:String?,

val chat_image:String?,

val createdAt: Date,

val isGroup: Boolean,

val createdBy: UserRequest,

val users:HashSet<UserRequest> = HashSet<UserRequest>(),

val chatOnline_At:Map<Int,Date> = HashMap<Int,Date>()

):Parcelable