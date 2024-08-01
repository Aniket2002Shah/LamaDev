package com.example.lamadev.view_model_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lamadev.repository.ChatRepository
import com.example.lamadev.repository.ProfileRepository
import com.example.lamadev.repository.UserRepository
import com.example.lamadev.view_model.MessageViewModel
import com.example.lamadev.view_model.ProfileViewModel

class ProfileViewModelFactory(private val profileUserId:Int,private val userId:Int,private  val repository: ProfileRepository,private val userRepository: UserRepository,private val chatRepository: ChatRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(userId,profileUserId, repository,userRepository,chatRepository) as T
    }
}