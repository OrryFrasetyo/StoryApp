package com.orryfrasetyo.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.orryfrasetyo.storyapp.data.UserRepository
import com.orryfrasetyo.storyapp.data.response.ListStoryItem
import com.orryfrasetyo.storyapp.data.userpref.UserModel

class MainViewModel(private val repository: UserRepository): ViewModel() {

    val stories: LiveData<PagingData<ListStoryItem>> =
        repository.getStories().cachedIn(viewModelScope)

    fun getStory() = repository.getStory()


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    suspend fun logout() {
        repository.logout()
    }
}