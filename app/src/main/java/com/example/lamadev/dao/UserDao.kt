package com.example.lamadev.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lamadev.pojo.User

@Dao
interface UserDao {

    //  -------- onLogin
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    //--------updateUser
    //notifyAt, uploadProfilePhoto, uploadCoverPhoto
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user: User)

    //get User
    @Query("select * from user")
    fun getUser():LiveData<List<User>>

    @Query("delete from user")
    suspend fun deleteUser()
}