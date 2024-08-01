package com.example.lamadev.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lamadev.pojo.Notification
import com.example.lamadev.pojo.User
import com.example.lamadev.repository.NotificationRepository
import com.example.lamadev.repository.ProfileRepository
import com.example.lamadev.repository.StoryRepository
import com.example.lamadev.repository.UserRepository
import com.example.lamadev.response_handler.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotificationViewModel @Inject constructor(private val currentUserId:Int, private val notificationRepository: NotificationRepository) : ViewModel(){


    init {
        viewModelScope.launch(Dispatchers.IO){
            notificationRepository.getAllNotification(currentUserId)
        }
    }
    var notifyLiveData = notificationRepository.notifyLiveData

    var updateNotify :LiveData<Response<String>> = notificationRepository.stringLiveData

    var userLivedata:LiveData<Response<String>> = notificationRepository.userLiveData


    fun setIsRead(notificationId:Int) = viewModelScope.launch(Dispatchers.IO) {
        notificationRepository.setIsRead(notificationId,currentUserId)
        updateNotify = notificationRepository.stringLiveData
        loadNotification()
    }

    fun updateNotifyAt(userId: Int) = viewModelScope.launch(Dispatchers.IO) {
       notificationRepository.updateNotifyAt(userId)
        userLivedata = notificationRepository.userLiveData
    }

    fun loadNotification() = viewModelScope.launch(Dispatchers.IO) {
         notificationRepository.getAllNotification(currentUserId)
        notifyLiveData = notificationRepository.notifyLiveData
    }
}