package com.orryfrasetyo.storyapp.di

import android.content.Context
import com.orryfrasetyo.storyapp.data.UserRepository
import com.orryfrasetyo.storyapp.data.api.ApiConfig
import com.orryfrasetyo.storyapp.data.userpref.UserPreferences
import com.orryfrasetyo.storyapp.data.userpref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking {
            pref.getSession().first()
        }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }
}