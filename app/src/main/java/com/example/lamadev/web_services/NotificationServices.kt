package com.example.lamadev.web_services

import com.example.lamadev.pojo.ApiResponse
import com.example.lamadev.pojo.Notification
import com.example.lamadev.pojo.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotificationServices {

    @PUT(" user/{userId}/notifyAt")
    suspend fun updateUserNotifyAt(@Path("userId") userId:Int): Response<User>

    @GET("notification/user/{userId}")
    suspend fun getAllNotificationByUserId(@Path("userId") userId:Int): Response<List<Notification>>

    @GET("notification/user/{userId}/latest")
    suspend fun findLatestNotificationByUserId(@Path("userId") userId:Int): Response<List<Notification>>

    @PUT("{notificationId}/setRead/user/{userId}")
    suspend fun setIsRead(@Path("notificationId") notificationId:Int,@Path("userId") userId:Int): Response<Notification>

}