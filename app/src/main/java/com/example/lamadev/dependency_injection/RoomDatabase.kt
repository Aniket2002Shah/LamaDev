package com.example.lamadev.dependency_injection

import androidx.room.Database
import androidx.room.TypeConverters
import com.example.lamadev.dao.*
import com.example.lamadev.pojo.*
import com.example.lamadev.pojo.entities.MessageEntity
import com.example.lamadev.pojo.entities.NotificationEntity
import com.example.lamadev.type_converters.CustomConverters

@Database(entities =
[User::class,StoryDtoReq::class,StatusDtoReq::class,NotificationEntity::class,
 Post::class, MessageEntity::class,Chat::class]
 , version = 1)
@TypeConverters(CustomConverters::class)
 abstract class RoomDatabase : androidx.room.RoomDatabase() {

 abstract fun getUserDao(): UserDao
 abstract fun getStoryDao(): StoryDao
 abstract fun getStatusDao(): StatusDao
 abstract fun getPostDao(): PostDao
 abstract fun getNotificationDao(): NotificationDao
 abstract fun getMessageDao(): MessageDao
 abstract fun getChatDao(): ChatDao

}