package com.example.lamadev.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lamadev.dependency_injection.RoomDatabase
import com.example.lamadev.pojo.ApiResponse
import com.example.lamadev.pojo.FollowerDto
import com.example.lamadev.pojo.Post
import com.example.lamadev.pojo.User
import com.example.lamadev.response_handler.Response
import com.example.lamadev.utils.NetworkConnection
import com.example.lamadev.web_services.ProfileServices
import com.example.lamadev.web_services.UserServices
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val profileServices: ProfileServices, private  val roomDatabase: RoomDatabase, private val application: Context) {

    //user
    private var profileMutableLiveData= MutableLiveData<Response<User>>()
    val profileLiveData: LiveData<Response<User>>
        get()= profileMutableLiveData


    //list<User>
    private var profileListMutableLiveData= MutableLiveData<Response<List<User>>>()
    val profileListLiveData: LiveData<Response<List<User>>>
        get()= profileListMutableLiveData


    //Following Dto
    private var followingMutableLiveData= MutableLiveData<Response<FollowerDto>>()
    val followingLiveData: LiveData<Response<FollowerDto>>
        get()= followingMutableLiveData

    //Apiresponse
    //Following Dto
    private var apiResponseMutableLiveData= MutableLiveData<Response<String>>()
    val apiResponseLiveData: LiveData<Response<String>>
        get()= apiResponseMutableLiveData


    //postList
    private var postListResponseMutableLiveData= MutableLiveData<Response<List<Post>>>()
    val postListResponseLiveData: LiveData<Response<List<Post>>>
        get()= postListResponseMutableLiveData


    suspend fun getUser(userId:Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                profileMutableLiveData.postValue(Response.Processing())
                val result = profileServices.getUserById(userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                   // quotesDao.deleteQuotes()
                   // quotesDao.addQuotes(result.body()!!.results)
                   profileMutableLiveData.postValue(Response.Success(result.body()))
                }
                else{
                    profileMutableLiveData.postValue(Response.Error("Failed to fetch data. Server not Responding !!"))
                }
            }
            catch (e:Exception){
                profileMutableLiveData.postValue(Response.Error(e.message))
            }
        }
        else{
           profileMutableLiveData.postValue(Response.Error("Check your Internet connection"))
        }
    }


    suspend fun deleteUser(userId:Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                apiResponseMutableLiveData.postValue(Response.Processing())
                val result = profileServices.deleteUser(userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    // quotesDao.deleteQuotes()
                    // quotesDao.addQuotes(result.body()!!.results)
                    apiResponseMutableLiveData.postValue(Response.Success("Account deleted successfully"))
                }
                else{
                    apiResponseMutableLiveData.postValue(Response.Error("Failed to fetch data. Server not Responding !!"))
                }
            }
            catch (e:Exception){
                apiResponseMutableLiveData.postValue(Response.Error(e.message))
            }
        }
        else{
            profileMutableLiveData.postValue(Response.Error("Check your Internet connection"))
        }
    }


    suspend fun getAllUser(){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                profileListMutableLiveData.postValue(Response.Processing())
                val result = profileServices.getAllUsers()
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    // quotesDao.deleteQuotes()
                    // quotesDao.addQuotes(result.body()!!.results)
                    profileListMutableLiveData.postValue(Response.Success(result.body()))
                }
                else{
                    profileListMutableLiveData.postValue(Response.Error("Failed to fetch data. Server not Responding !!"))
                }
            }
            catch (e:Exception){
                profileListMutableLiveData.postValue(Response.Error(e.message))
            }
        }
        else{
            profileMutableLiveData.postValue(Response.Error("Check your Internet connection"))
        }
    }


    suspend fun followUser(userId: Int,followingId:Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                profileMutableLiveData.postValue(Response.Processing())
                val result = profileServices.followUser(userId,followingId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    // quotesDao.deleteQuotes()
                    // quotesDao.addQuotes(result.body()!!.results)
                    profileMutableLiveData.postValue(Response.Success(result.body()))
                }
                else{
                    profileMutableLiveData.postValue(Response.Error("Failed to fetch data. Server not Responding !!"))
                }
            }
            catch (e:Exception){
                profileMutableLiveData.postValue(Response.Error(e.message))
            }
        }
        else{
            profileMutableLiveData.postValue(Response.Error("Check your Internet connection"))
        }
    }


    suspend fun getAllUserFollowers(userId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                followingMutableLiveData.postValue(Response.Processing())
                val result = profileServices.getAllUserFollowers(userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    // quotesDao.deleteQuotes()
                    // quotesDao.addQuotes(result.body()!!.results)
                    followingMutableLiveData.postValue(Response.Success(result.body()))
                }
                else{
                    followingMutableLiveData.postValue(Response.Error("Failed to fetch data. Server not Responding !!"))
                }
            }
            catch (e:Exception){
                followingMutableLiveData.postValue(Response.Error(e.message))
            }
        }
        else{
            followingMutableLiveData.postValue(Response.Error("Check your Internet connection"))
        }
    }


    suspend fun getAllUserFollowings(userId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                followingMutableLiveData.postValue(Response.Processing())
                val result = profileServices.getAllUserFollowings(userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    // quotesDao.deleteQuotes()
                    // quotesDao.addQuotes(result.body()!!.results)
                    followingMutableLiveData.postValue(Response.Success(result.body()))
                }
                else{
                    followingMutableLiveData.postValue(Response.Error("Failed to fetch data. Server not Responding !!"))
                }
            }
            catch (e:Exception){
                followingMutableLiveData.postValue(Response.Error(e.message))
            }
        }
        else{
            followingMutableLiveData.postValue(Response.Error("Check your Internet connection"))
        }
    }


    suspend fun unfollowUser(userId: Int,followingId:Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                apiResponseMutableLiveData.postValue(Response.Processing())
                val result = profileServices.unfollowUser(userId,followingId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    // quotesDao.deleteQuotes()
                    // quotesDao.addQuotes(result.body()!!.results)
                }
                else{
                    apiResponseMutableLiveData.postValue(Response.Error("Failed to fetch data. Server not Responding !!"))
                }
            }
            catch (e:Exception){
                apiResponseMutableLiveData.postValue(Response.Error(e.message))
            }
        }
        else{
            apiResponseMutableLiveData.postValue(Response.Error("Check your Internet connection"))
        }
    }



    suspend fun getUserPost(userId: Int) {
        if (NetworkConnection.isInternetAvailable(application)) {
            try {
                postListResponseMutableLiveData.postValue(Response.Processing())
                val result = profileServices.getPostByUser(userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    postListResponseMutableLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    postListResponseMutableLiveData.postValue(Response.Error("Failed to get timeline.Server not Responding !!"))
                }
            } catch (e: Exception) {
                postListResponseMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            postListResponseMutableLiveData.postValue(Response.Error("Check your Internet connection"))
        }
    }
}