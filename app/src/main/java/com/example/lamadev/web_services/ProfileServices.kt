package com.example.lamadev.web_services

import com.example.lamadev.pojo.ApiResponse
import com.example.lamadev.pojo.FollowerDto
import com.example.lamadev.pojo.Post
import com.example.lamadev.pojo.User
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfileServices {

    @GET("user/{userId}")
    suspend fun getUserById(@Path("userId") userId:Int): Response<User>

    @GET("user/")
    suspend fun getAllUsers(): Response<List<User>>

    @DELETE("user/{userId}")
    suspend fun deleteUser(@Path("userId") userId:Int): Response<ApiResponse>

    @POST("user/follower/{userId}/following/{followingId}")
    suspend fun followUser(@Path("userId") userId:Int, @Path("followingId") followingId:Int): Response<User>

    @GET("user/{userId}/followers")
    suspend fun getAllUserFollowers(@Path("userId") userId:Int): Response<FollowerDto>

    @GET("user/{userId}/followings")
    suspend fun getAllUserFollowings(@Path("userId") userId:Int): Response<FollowerDto>

    @DELETE("user/follower/{userId}/following/{followingId}")
    suspend fun unfollowUser(@Path("userId") userId:Int, @Path("followingId") followingId:Int): Response<ApiResponse>

   // ---------------User posts
   @GET("post/user/{userId}")
   suspend fun getPostByUser(@Path("userId") userId:Int): Response<List<Post>>
}