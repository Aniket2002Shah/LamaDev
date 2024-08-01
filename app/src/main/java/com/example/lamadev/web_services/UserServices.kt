package com.example.lamadev.web_services

import com.example.lamadev.pojo.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserServices {

    //<----------------------User Repository Start------------------------->
    //-----AuthController start


    // BASE_URL+/auth/register
    @POST("auth/register")
    suspend fun registerUser(@Body request : RegisterRequest): Response<User>


    @POST("auth/login")
    suspend fun loginUser(@Body request : LoginRequest): Response<User>

    //-----AuthController end



    //-----UserController start


    @PUT("user/{userId}")
    suspend fun updateUser(@Body updateUser: UpdateUser,@Path("userId") userId:Int): Response<User>

    @Multipart
    @PUT("user/profilePhoto/upload/{userId}")
    suspend fun uploadProfilePhoto(@Part image:MultipartBody.Part,@Path("userId") userId:Int): Response<User>

    @Multipart
    @PUT("user/coverPhoto/upload/{userId}")
    suspend fun uploadCoverPhoto(@Part image:MultipartBody.Part,@Path("userId") userId:Int): Response<User>

    //GET---- using Glide dowmload via Url


    //<----------------------User Repository End------------------------->

}