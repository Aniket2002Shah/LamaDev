package com.example.lamadev.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lamadev.pojo.UserRequest
import com.example.lamadev.repository.ChatRepository
import com.example.lamadev.repository.ProfileRepository
import com.example.lamadev.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class ProfileViewModel @Inject constructor(private val currentUserId: Int,private val profileUserId:Int,private  val repository: ProfileRepository,private  val userRepository: UserRepository,private val chatRepository: ChatRepository) : ViewModel(){

    var profileUser = repository.profileLiveData

    var followingLiveDataResp = repository.followingLiveData

    var currentUserFollowingLiveDataResp = userRepository.followingLiveData

    var chatLiveData = chatRepository.chatLiveData

    init {
        viewModelScope.launch(Dispatchers.IO){
            userRepository.getAllUserFollowings(currentUserId)
            repository.getUser(profileUserId)
            repository.getAllUserFollowings(profileUserId)
        }
    }

    fun loadProfile() = viewModelScope.launch(Dispatchers.IO) {
        repository.getUser(profileUserId)
        profileUser = repository.profileLiveData
        repository.getAllUserFollowings(profileUserId)
        followingLiveDataResp = repository.followingLiveData
    }


    fun createChatRoom() = viewModelScope.launch(Dispatchers.IO) {
        val userRequest = UserRequest(currentUserId,null,null)
        chatRepository.createChatRoom(userRequest,profileUserId)
        chatLiveData = chatRepository.chatLiveData
    }

    fun followUser(currentUserId:Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.followUser(currentUserId,profileUserId)
    }

    fun unFollowUser(currentUserId:Int) = viewModelScope.launch(Dispatchers.IO) {
       repository.unfollowUser(currentUserId,profileUserId)
    }
}
