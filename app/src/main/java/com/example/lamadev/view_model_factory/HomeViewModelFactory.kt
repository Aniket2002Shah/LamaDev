package com.example.lamadev.view_model_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lamadev.repository.PostRepository
import com.example.lamadev.repository.StoryRepository
import com.example.lamadev.repository.UserRepository
import com.example.lamadev.view_model.HomeViewModel
import com.example.lamadev.view_model.MessageViewModel
import javax.inject.Inject

class HomeViewModelFactory (private val userId:Int,private val storyRepository: StoryRepository, private val  postRepository: PostRepository,private  val userRepository: UserRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(userId,storyRepository,postRepository,userRepository) as T
    }
}