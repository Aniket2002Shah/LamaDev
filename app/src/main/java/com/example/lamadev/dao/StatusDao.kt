package com.example.lamadev.dao

import androidx.room.*
import com.example.lamadev.pojo.StatusDtoReq

@Dao
interface StatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStatus(status: List<StatusDtoReq>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStatus(status: StatusDtoReq)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdateStatus(status: StatusDtoReq)

    @Query("select * from status")
    suspend fun getAllStatus():List<StatusDtoReq>

    @Query("delete from status")
    suspend fun deleteStatus()
}