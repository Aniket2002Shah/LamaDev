package com.example.lamadev.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lamadev.pojo.GroupChatRequest
import com.example.lamadev.pojo.Message
import com.example.lamadev.pojo.User
import com.example.lamadev.pojo.UserRequest
import com.example.lamadev.repository.*
import com.example.lamadev.response_handler.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatContainerViewModel @Inject constructor(private  val userId:Int, private val messRepository: MessageRepository) : ViewModel(){

    var msgLiveData = messRepository.msgLiveData

    lateinit var stringLiveData :LiveData<Response<String>>

    init {
        viewModelScope.launch(Dispatchers.IO){
               messRepository.getLatestMessagesOfUser(userId)
        }
    }

    fun getLatestMessages() = viewModelScope.launch(Dispatchers.IO) {
      messRepository.getLatestMessagesOfUser(userId)
        msgLiveData = messRepository.msgLiveData
    }
}