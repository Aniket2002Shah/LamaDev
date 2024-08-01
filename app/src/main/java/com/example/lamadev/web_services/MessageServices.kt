package com.example.lamadev.web_services

import com.example.lamadev.pojo.ApiResponse
import com.example.lamadev.pojo.Message
import com.example.lamadev.pojo.UserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path

interface MessageServices {

    @POST("message/create")
    suspend fun sendMessage(@Body message: Message): Response<Message>

    @GET("message/user/{userId}/chat/{chatId}")
    suspend fun getChatMessages(@Path("userId") userId:Int,@Path("chatId") chatId:Int): Response<List<Message>>

    @GET("message/user/{userId}/latest")
    suspend fun getLatestMessagesOfUser(@Path("userId") userId:Int): Response<List<Message>>

    @HTTP(method = "DELETE",path="message/{messageId}", hasBody = true)
    suspend fun deleteMessages(@Body reqUser:UserRequest,@Path("messageId") messageId:Int): Response<ApiResponse>
}