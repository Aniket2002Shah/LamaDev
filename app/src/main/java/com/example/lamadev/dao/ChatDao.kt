package com.example.lamadev.dao

import androidx.room.*
import com.example.lamadev.pojo.Chat

@Dao
interface ChatDao {

    //For ChatRoom and Group
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChat(chat: Chat)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChat(chat: List<Chat>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateChat(chat: Chat)

    @Query("delete from chats where id=:id And isGroup=true")
    suspend fun deleteGroupChat(id:Int)

    @Query("select * from chats where id=:id")
    suspend fun getChatById(id:Int):Chat

    @Query("select * from chats")
    suspend fun getChats():List<Chat>
}