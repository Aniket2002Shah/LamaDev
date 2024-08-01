package com.example.lamadev.dao

import androidx.room.*
import com.example.lamadev.pojo.entities.MessageEntity

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMessages(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMessages(message: List<MessageEntity>)

    @Query("select * from messages m where m.chatId=:chatId")
    suspend fun getMessagesByChatId(chatId: Int):List<MessageEntity>

    @Query("select * from messages m where m.unSeen = false")
    suspend fun getUnseenMessages():List<MessageEntity>

    @Query("delete from messages where id=:messageId")
    suspend fun deleteMessageById(messageId:Int)

    @Query("delete from messages")
    suspend fun deleteMessage()

    @Query("delete from messages  where chatId=:chatId")
    suspend fun deleteMessagesByChatId(chatId: Int)
}