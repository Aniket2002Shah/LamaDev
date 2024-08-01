package com.example.lamadev.view_model_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lamadev.repository.NotificationRepository
import com.example.lamadev.repository.PostRepository
import com.example.lamadev.repository.StoryRepository
import com.example.lamadev.repository.UserRepository
import com.example.lamadev.view_model.HomeViewModel
import com.example.lamadev.view_model.MessageViewModel
import com.example.lamadev.view_model.NotificationViewModel
import javax.inject.Inject

class NotificationViewModelFactory (private val userId:Int, private val notificationRepository: NotificationRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotificationViewModel(userId,notificationRepository) as T
    }
}