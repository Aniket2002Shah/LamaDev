package com.example.lamadev.dao

import androidx.room.*
import com.example.lamadev.pojo.StatusDtoReq
import com.example.lamadev.pojo.StoryDtoReq

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStory(story: StoryDtoReq)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addStory(story: List<StoryDtoReq>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStory(story: StoryDtoReq)

    @Query("select * from story s where s.statusId=:statusId")
    suspend fun getStoryByStatusId(statusId: Int):List<StoryDtoReq>

    @Query("select * from story")
    suspend fun getAllStory():List<StoryDtoReq>

    @Query("delete from story where id=:id")
    suspend fun deleteStory(id: Int)

    @Query("delete from story")
    suspend fun deleteStory()
}