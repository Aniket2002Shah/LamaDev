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
import com.example.lamadev.web_services.SearchServices
import javax.inject.Inject

class SearchRepository @Inject constructor(private val searchServices: SearchServices, private val application: Context) {

    private var searchMutableLiveData = MutableLiveData<Response<List<Search>>>()

    val searchLiveData: LiveData<Response<List<Search>>>
        get() = searchMutableLiveData

    private var userSearchMutableLiveData = MutableLiveData<Response<List<User>>>()

    val userSearchLiveData: LiveData<Response<List<User>>>
        get() = userSearchMutableLiveData

//    private var dataListMutableLiveData = MutableLiveData<Response<List<UserSearchReq>>>()
//
//    val dataListLiveData: LiveData<Response<List<UserSearchReq>>>
//        get() = dataListMutableLiveData


    private var stringMutableLiveData = MutableLiveData<Response<String>>()

    val stringListLiveData: LiveData<Response<String>>
        get() = stringMutableLiveData


    suspend fun getSearchHistory( userId: Int) {

        if (NetworkConnection.isInternetAvailable(application)) {
            try {
                searchMutableLiveData.postValue(Response.Processing())
                val result = searchServices.getAllSearchByUserId(userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    searchMutableLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    searchMutableLiveData.postValue(Response.Error("Failed to fetch search history.Server not Responding !!"))
                }
            } catch (e: Exception) {
                searchMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            searchMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }

    suspend fun searchUser( keyword:String,userId:Int) {

        if (NetworkConnection.isInternetAvailable(application)) {
            try {
               userSearchMutableLiveData.postValue(Response.Processing())
                val result = searchServices.searchUserByNameOrEmail(userId,keyword)
//                val followingResult = profileServices.getAllUserFollowings(userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data = result.body()!!
                    userSearchMutableLiveData.postValue(Response.Success(data))
                } else {
                    userSearchMutableLiveData.postValue(Response.Error("Failed to fetch search history.Server not Responding !!"))
                }
            } catch (e: Exception) {
                userSearchMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            userSearchMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }

    suspend fun deleteSearch( userId: Int, searchId: Int) {

        if (NetworkConnection.isInternetAvailable(application)) {
            try {
                stringMutableLiveData.postValue(Response.Processing())
                val result = searchServices.deleteSearch(userId,searchId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    stringMutableLiveData.postValue(Response.Success("Deleted search item successfully !!"))
                } else {
                    stringMutableLiveData.postValue(Response.Error("Failed to fetch search history.Server not Responding !!"))
                }
            } catch (e: Exception) {
                stringMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            stringMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }

}