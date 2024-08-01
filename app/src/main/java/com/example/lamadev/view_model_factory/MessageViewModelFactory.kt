package com.example.lamadev.view_model_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lamadev.repository.ChatRepository
import com.example.lamadev.repository.MessageRepository
import com.example.lamadev.repository.UserRepository
import com.example.lamadev.view_model.MessageViewModel

class MessageViewModelFactory(private val currentUserId:Int,private val chatId:Int,private val messageRepository: MessageRepository,private val chatRepository: ChatRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  MessageViewModel(currentUserId,chatId,messageRepository,chatRepository) as T
    }
}