package com.example.lamadev.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lamadev.dependency_injection.RoomDatabase
import com.example.lamadev.pojo.*
import com.example.lamadev.response_handler.Response
import com.example.lamadev.utils.NetworkConnection
import com.example.lamadev.web_services.ProfileServices
import com.example.lamadev.web_services.UserServices
import okhttp3.MultipartBody
import javax.inject.Inject

class UserRepository @Inject constructor(private val userServices: UserServices,private val profileServices: ProfileServices, private  val roomDatabase: RoomDatabase,private val application: Context) {

    private var userMutableLiveData= MutableLiveData<Response<String>>()

    val userLiveData: LiveData<Response<String>>
        get()= userMutableLiveData

    //Following Dto
    private var followingMutableLiveData= MutableLiveData<Response<FollowerDto>>()
    val followingLiveData: LiveData<Response<FollowerDto>>
        get()= followingMutableLiveData

    val currentUserLiveData = roomDatabase.getUserDao().getUser()

    //registerUser
    suspend fun registerUser(request: RegisterRequest){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                userMutableLiveData.postValue(Response.Processing())
                val result = userServices.registerUser(request)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    userMutableLiveData.postValue(Response.Success("User registered successfully !!"))
                }
                else{
                    userMutableLiveData.postValue(Response.Error("Failed to register user.  Server not Responding !!"))
                }
            }
            catch (e:Exception){
                userMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            userMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }


    //loginUser
    suspend fun loginUser(request: LoginRequest){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                userMutableLiveData.postValue(Response.Processing())
                val result = userServices.loginUser(request)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    roomDatabase.getUserDao().deleteUser()
                    roomDatabase.getUserDao().addUser(result.body()!!)
                    userMutableLiveData.postValue(Response.Success("User login successfully !!"))
                }
                else{
                    userMutableLiveData.postValue(Response.Error("Failed to login user. Server not Responding !!"))
                }
            }
            catch (e:Exception){
                userMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            userMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }

    //fetch user from Sqlite DB
//     fun getUser():LiveData<User>? {
//        try {
//            return roomDatabase.getUserDao().getUser()
//
//        } catch (e: Exception) {
//            userMutableLiveData.postValue(Response.Error(e.message))
//        }
//        return null
//    }

    //update User
    suspend fun updateUser(userId:Int,user: UpdateUser){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                userMutableLiveData.postValue(Response.Processing())
                val result = userServices.updateUser(user,userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    roomDatabase.getUserDao().updateUser(result.body()!!)
                    userMutableLiveData.postValue(Response.Success("User Profile updated successfully !!"))
                }
                else{
                    userMutableLiveData.postValue(Response.Error("Failed to update user Profile. Server not Responding !!"))
                }
            }
            catch (e:Exception){
                userMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            userMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }

    //update ProfilePhoto
    suspend fun updateProfilePhoto(image: MultipartBody.Part, userId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                userMutableLiveData.postValue(Response.Processing())
                val result = userServices.uploadProfilePhoto(image,userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    roomDatabase.getUserDao().updateUser(result.body()!!)
                    userMutableLiveData.postValue(Response.Success("User ProfilePhoto updated successfully !!"))
                }
                else{
                    userMutableLiveData.postValue(Response.Error("Failed to update  user ProfilePhoto . Server not Responding !!"))
                }
            }
            catch (e:Exception){
                userMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            userMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }

    //update CoverPhoto
    suspend fun updateCoverPhoto(image: MultipartBody.Part, userId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                userMutableLiveData.postValue(Response.Processing())
                val result = userServices.uploadCoverPhoto(image,userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    roomDatabase.getUserDao().updateUser(result.body()!!)
                    userMutableLiveData.postValue(Response.Success("User CoverPhoto updated successfully !!"))
                }
                else{
                    userMutableLiveData.postValue(Response.Error("Failed to update  user CoverPhoto . Server not Responding !!"))
                }
            }
            catch (e:Exception){
                userMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            userMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
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



    suspend fun logoutUser(){
        try {
            roomDatabase.getUserDao().deleteUser()
        } catch (e: Exception) {
           Log.d("Logout",e.message.toString())
        }
    }

}