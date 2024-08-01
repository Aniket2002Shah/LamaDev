package com.example.lamadev.web_services

import com.example.lamadev.pojo.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ChatRoomServices {

    @POST("chat/{userId}")
    suspend fun createChatRoom(@Body userRequest: UserRequest, @Path("userId") userId:Int): Response<Chat>

    @GET("chat/{chatRoomId}")
    suspend fun getChatRoomById(@Path("chatRoomId") chatRoomId:Int): Response<Chat>

    @GET("chat/user/{userId}")
    suspend fun getAllChatOfUserById(@Path("userId") userId:Int): Response<List<Chat>>

    @POST("chat/group/createdBy/{userId}")
    suspend fun createGroup(@Body req: GroupChatRequest,@Path("userId") userId:Int): Response<Chat>

    @PUT("chat/{chatRoomId}/updateOnlineAt/user/{userId}")
    suspend fun updateOnlineAtOfUser(@Path("userId") userId:Int,@Path("chatRoomId") chatRoomId:Int): Response<Chat>

    @PUT("chat/{chatRoomId}/add/user/{userId}")
    suspend fun addUserToGroup(@Path("userId") userId:Int,@Path("chatRoomId") chatRoomId:Int,@Body userRequest: UserRequest): Response<Chat>

    @PUT("chat/{chatRoomId}/rename/renamedBy/{userId}")
    suspend fun renameGroup(@Path("chatRoomId") chatRoomId:Int,@Path("userId") userId:Int,@Body groupname: String): Response<Chat>

    @PUT("chat/{chatRoomId}/remove/user/{userId}")
    suspend fun removeUserFromGroup(@Path("chatRoomId") chatRoomId:Int,@Path("userId") userId:Int,@Body userRequest: UserRequest): Response<Chat>

    @DELETE("chat/{chatRoomId}/admin/{userId}")
    suspend fun deleteGroupByUser(@Path("chatRoomId") chatRoomId:Int,@Path("userId") userId:Int): Response<ApiResponse>
}