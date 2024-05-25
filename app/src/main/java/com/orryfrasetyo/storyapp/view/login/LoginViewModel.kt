package com.orryfrasetyo.storyapp.view.login

import androidx.lifecycle.ViewModel
import com.orryfrasetyo.storyapp.data.UserRepository
import com.orryfrasetyo.storyapp.data.userpref.UserModel


class LoginViewModel(private val repository: UserRepository): ViewModel() {

    fun login(email: String, password: String) = repository.login(email, password)

    suspend fun saveSession(userModel: UserModel) {
        repository.saveSession(userModel)
    }
}