package com.example.lamadev.dao

import androidx.room.*
import com.example.lamadev.pojo.Post

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPost(postList: List<Post>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPost(postList: Post)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePost(post: Post)

    @Query("select * from posts")
    suspend fun getAllPost():List<Post>

    @Query("delete from posts where id=:postId")
    suspend fun deletePost(postId:Int)

    @Query("delete from posts")
    suspend fun deletePost()
}