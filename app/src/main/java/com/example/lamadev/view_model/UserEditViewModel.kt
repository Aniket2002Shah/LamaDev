package com.example.lamadev.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lamadev.pojo.LoginRequest
import com.example.lamadev.pojo.RegisterRequest
import com.example.lamadev.pojo.UpdateUser
import com.example.lamadev.pojo.User
import com.example.lamadev.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class UserEditViewModel  @Inject constructor( private  val userRepository: UserRepository) : ViewModel(){
    val currentUser : LiveData<List<User>> = userRepository.currentUserLiveData

    val updateUserLiveData
    get() = userRepository.userLiveData


    fun updateUser(userId:Int,updateUser: UpdateUser) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.updateUser(userId,updateUser)
    }

    fun updateCoverPhoto(image:MultipartBody.Part,userId:Int) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.updateCoverPhoto(image,userId)
    }

    fun updateProfilePhoto(image:MultipartBody.Part,userId:Int) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.updateProfilePhoto(image,userId)
    }


}