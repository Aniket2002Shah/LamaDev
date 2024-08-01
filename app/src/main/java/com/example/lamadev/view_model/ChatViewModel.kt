package com.example.lamadev.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lamadev.pojo.*
import com.example.lamadev.repository.*
import com.example.lamadev.response_handler.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(private  val userId:Int, private val chatRepository: ChatRepository) : ViewModel(){

    val chatListLiveData
        get() = chatRepository.chatListLiveData

    lateinit var chatLiveData :LiveData<Response<Chat>>

    lateinit var stringLiveData :LiveData<Response<String>>


    init {
        viewModelScope.launch(Dispatchers.IO){
               chatRepository.getAllChatRoom(userId)
        }
    }

    fun createGroup(groupChatRequest: GroupChatRequest) = viewModelScope.launch(Dispatchers.IO) {
        chatRepository.createGroup(groupChatRequest, userId)
        chatLiveData = chatRepository.chatLiveData
    }

    fun createChat(userRequest: UserRequest) = viewModelScope.launch(Dispatchers.IO) {
        chatRepository.createChatRoom(userRequest, userId)
        chatLiveData = chatRepository.chatLiveData
    }
}