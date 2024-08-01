package com.example.lamadev.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lamadev.pojo.User
import com.example.lamadev.repository.SearchRepository
import com.example.lamadev.repository.UserRepository
import com.example.lamadev.response_handler.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(private  val userId:Int, private val searchRepository: SearchRepository) : ViewModel(){

    var searchListLiveData = searchRepository.searchLiveData
    var userSearchListLiveData = searchRepository.userSearchLiveData
    lateinit var stringLiveDataResp : LiveData<Response<String>>

    init {
        viewModelScope.launch(Dispatchers.IO){
             searchRepository.getSearchHistory(userId)
        }
    }



    fun search(string: String) = viewModelScope.launch(Dispatchers.IO) {
       searchRepository.searchUser(string,userId)
      userSearchListLiveData = searchRepository.userSearchLiveData
    }

//    fun fetchSearchHistory() = viewModelScope.launch(Dispatchers.IO) {
//        searchRepository.getSearchHistory(userId)
//        searchListLiveData = searchRepository.searchLiveData
//    }

    fun deleteSearch(searchId:Int) = viewModelScope.launch(Dispatchers.IO) {
        searchRepository.deleteSearch(userId,searchId)
        stringLiveDataResp = searchRepository.stringListLiveData
    }
}