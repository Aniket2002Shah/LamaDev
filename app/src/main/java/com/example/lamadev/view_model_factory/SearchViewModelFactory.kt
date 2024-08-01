package com.example.lamadev.view_model_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lamadev.repository.PostRepository
import com.example.lamadev.repository.SearchRepository
import com.example.lamadev.repository.StoryRepository
import com.example.lamadev.repository.UserRepository
import com.example.lamadev.view_model.HomeViewModel
import com.example.lamadev.view_model.MessageViewModel
import com.example.lamadev.view_model.SearchViewModel
import javax.inject.Inject

class SearchViewModelFactory (private val userId:Int, private val searchRepository: SearchRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(userId,searchRepository) as T
    }
}