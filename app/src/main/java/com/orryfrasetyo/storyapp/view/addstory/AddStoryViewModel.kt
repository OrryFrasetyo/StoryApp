package com.orryfrasetyo.storyapp.view.addstory

import androidx.lifecycle.ViewModel
import com.orryfrasetyo.storyapp.data.UserRepository
import java.io.File

class AddStoryViewModel(private val repository: UserRepository): ViewModel() {

    fun uploadImage(file: File, description: String) = repository.uploadImage(file, description)
}