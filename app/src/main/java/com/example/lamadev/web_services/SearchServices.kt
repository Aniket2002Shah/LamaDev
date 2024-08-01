package com.example.lamadev.web_services

import com.example.lamadev.pojo.ApiResponse
import com.example.lamadev.pojo.Search
import com.example.lamadev.pojo.User
import retrofit2.Response
import retrofit2.http.*

interface SearchServices {

    //userController
    @GET("user/{userId}/search")
    suspend fun searchUserByNameOrEmail(@Path("userId") userId:Int,@Query("keyword") keyword:String): Response<List<User>>

    //searchController
    @GET("search/user/{userId}")
    suspend fun getAllSearchByUserId(@Path("userId") userId:Int): Response<List<Search>>

    @DELETE("search/{searchId}/user/{userId}")
    suspend fun deleteSearch(@Path("userId") userId:Int,@Path("searchId") searchId:Int): Response<ApiResponse>
}