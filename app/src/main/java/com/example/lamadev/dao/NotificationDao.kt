package com.example.lamadev.dao

import androidx.room.*
import com.example.lamadev.pojo.entities.MessageEntity
import com.example.lamadev.pojo.entities.NotificationEntity

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNotification(notification: List<NotificationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNotification(notification: NotificationEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setIsRead(notification: NotificationEntity)

    @Query("delete from notification")
    suspend fun deleteNotification()

    @Query("select * from notification")
    suspend fun getAllNotification():List<NotificationEntity>

    @Query("select * from notification n where n.unSeen = false")
    suspend fun getUnseenMessages():List<NotificationEntity>
}