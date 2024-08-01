package com.example.lamadev.web_services

import com.example.lamadev.pojo.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface StoryServices {

    @POST("status/story/user/{userId}")
    suspend fun createStory(@Body story : Story,@Path("userId") userId:Int): Response<Story>

    @Multipart
    @POST("status/storyImage/upload/{storyId}")
    suspend fun uploadStoryImage(@Part image: MultipartBody.Part,@Path("storyId") storyId:Int): Response<Story>

    //GET image ---- By Glide

    @PUT("status/story/{storyId}")
    suspend fun updateStory(@Body story : Story,@Path("storyId") storyId:Int): Response<Story>

    @GET("status/story/{storyId}")
    suspend fun getStoryById(@Path("storyId") storyId:Int): Response<Story>

    @GET("status/user/{userId}")
    suspend fun getAllStatusOfUserAndFollowings(@Path("userId") userId:Int): Response<List<Status>>

    @PUT("status/story/{storyId}/like/{likedUserId}")
    suspend fun likeOrDislikeStory(@Path("storyId") storyId:Int, @Path("likedUserId") likedUserId:Int): Response<Story>

    @DELETE("status/story/{storyId}")
    suspend fun deleteStory(@Path("storyId") storyId:Int): Response<ApiResponse>

}