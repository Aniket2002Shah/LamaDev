package com.example.lamadev.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lamadev.pojo.User
import com.example.lamadev.repository.PostRepository
import com.example.lamadev.repository.ProfileRepository
import com.example.lamadev.repository.StoryRepository
import com.example.lamadev.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val userId:Int,private val storyRepository: StoryRepository,private val postRepository: PostRepository,private val userRepository: UserRepository) : ViewModel(){


    init {
        viewModelScope.launch(Dispatchers.IO){
                        storyRepository.getAllStatus(userId)
                        postRepository.getTimeline(userId)
        }
    }

    val statusLiveData
        get() = storyRepository.statusLiveData

    var postLiveData = postRepository.postListLiveData



    fun loadPost() = viewModelScope.launch(Dispatchers.IO) {
         postRepository.getTimeline(userId)
        postLiveData = postRepository.postListLiveData
    }

    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        userRepository.logoutUser()
    }

}