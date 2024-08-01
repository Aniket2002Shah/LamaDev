package com.example.lamadev.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lamadev.pojo.Message
import com.example.lamadev.pojo.User
import com.example.lamadev.pojo.UserRequest
import com.example.lamadev.repository.ChatRepository
import com.example.lamadev.repository.MessageRepository
import com.example.lamadev.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class MessageViewModel @Inject constructor(private val currentuserId:Int,private val chatId:Int ,private val messRepository: MessageRepository,private val chatRepository: ChatRepository) : ViewModel(){

    val msgLiveData
        get() = messRepository.msgLiveData

    var stringLiveData = messRepository.stringLiveData

    var newMsgChatLiveData = messRepository.newMsgLiveData

    var  chatLiveData = chatRepository.chatLiveData

    init {
        viewModelScope.launch(Dispatchers.IO){
            messRepository.getAllMessage(currentuserId,chatId)
        }
    }


    fun sendMessage(message: Message) = viewModelScope.launch(Dispatchers.IO) {
        messRepository.sendMessage(message)
        newMsgChatLiveData =messRepository.newMsgLiveData
    }

    fun deleteMessage(messageId:Int) = viewModelScope.launch(Dispatchers.IO) {
        val userRequest = UserRequest(currentuserId,null,null)
        messRepository.deleteMessage(userRequest,messageId)
        stringLiveData = messRepository.stringLiveData
    }

    fun updateOnlineAt() = viewModelScope.launch(Dispatchers.IO) {
        val userRequest = UserRequest(currentuserId,null,null)
        chatRepository.updateOnlineAtOfUser(currentuserId,chatId)
        chatLiveData = chatRepository.chatLiveData
    }
}