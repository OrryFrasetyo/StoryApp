package com.orryfrasetyo.storyapp.view.maps

import androidx.lifecycle.ViewModel
import com.orryfrasetyo.storyapp.data.UserRepository

class MapsViewModel(private val repository: UserRepository) : ViewModel() {

    fun getStoriesWithLocation() = repository.getStoriesWithLocation()
}