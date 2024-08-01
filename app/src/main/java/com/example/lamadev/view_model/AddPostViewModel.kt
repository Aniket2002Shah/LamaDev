package com.example.lamadev.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lamadev.pojo.Post2
import com.example.lamadev.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor( private val postRepository: PostRepository) : ViewModel(){

    var stringLiveDataResp =postRepository.stringLiveData
    var postMutableLiveData  = postRepository.postLiveData

    fun createPost(post: Post2, userId:Int) = viewModelScope.launch(Dispatchers.IO) {
         postRepository.createPost(post, userId)
        stringLiveDataResp =postRepository.stringLiveData
    }

    fun uploadPostImg(image: List<MultipartBody.Part>, postId: Int) = viewModelScope.launch(Dispatchers.IO) {
        postRepository.uploadPostImage(image, postId)
        postMutableLiveData  = postRepository.postLiveData
    }
}