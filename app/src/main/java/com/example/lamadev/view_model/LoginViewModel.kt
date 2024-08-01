package com.example.lamadev.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lamadev.pojo.LoginRequest
import com.example.lamadev.pojo.RegisterRequest
import com.example.lamadev.pojo.UpdateUser
import com.example.lamadev.pojo.User
import com.example.lamadev.repository.SearchRepository
import com.example.lamadev.repository.UserRepository
import com.example.lamadev.response_handler.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class LoginViewModel  @Inject constructor(private  val userRepository: UserRepository) : ViewModel(){

    var loginUserLiveData = userRepository.userLiveData


    fun loginUser(request: LoginRequest) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.loginUser(request)
        loginUserLiveData = userRepository.userLiveData
    }
}