package com.example.lamadev.web_services

import com.example.lamadev.pojo.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PostServices {

    @POST("post/user/{userId}")
    suspend fun createPost(@Body post : Post2, @Path("userId") userId:Int): Response<Post>

    @PUT("post/{postId}")
    suspend fun updatePost(@Body post : Post, @Path("postId") postId:Int): Response<Post>

    @GET("post/{postId}")
    suspend fun getPostById(@Path("postId") postId:Int): Response<Post>

    @GET("post/timeline/user/{userId}")
    suspend fun getUserTimeline(@Path("userId") userId:Int): Response<List<Post>>

    @PUT("post/{postId}/like/{likedUserId}")
    suspend fun likeOrDislikePost(@Path("postId") postId:Int,@Path("likedUserId") likedUserId:Int): Response<Post>

    @PUT("post/{postId}/share/{sharedUserId}")
    suspend fun sharePost(@Path("postId") postId:Int,@Path("sharedUserId") sharedUserId:Int): Response<Post>

    @DELETE("post/{postId}")
    suspend fun deletePost(@Path("postId") postId:Int): Response<ApiResponse>

    @Multipart
    @POST("post/image/upload/{postId}")
    suspend fun uploadPostImage(@Part image: List<MultipartBody.Part>,@Path("postId") postId:Int): Response<Post>

    //GET---Post Image via Glide

    //---------------------------------Comment Repository

    @POST("comment/post/{postId}/comments")
    suspend fun createComment(@Body comment: Comment, @Path("postId") postId:Int): Response<Comment>

    @DELETE("comment/{commentId}")
    suspend fun deleteComment(@Path("commentId") commentId:Int): Response<ApiResponse>
}