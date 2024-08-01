package com.example.lamadev.view_model_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lamadev.repository.*
import com.example.lamadev.view_model.ChatContainerViewModel
import com.example.lamadev.view_model.ChatViewModel
import com.example.lamadev.view_model.HomeViewModel
import com.example.lamadev.view_model.MessageViewModel
import javax.inject.Inject

class ChatContainerViewModelFactory (private val userId:Int, private val  messageRepository: MessageRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatContainerViewModel(userId,messageRepository) as T
    }
}