package com.orryfrasetyo.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.orryfrasetyo.storyapp.data.api.ApiService
import com.orryfrasetyo.storyapp.data.response.ErrorResponse
import com.orryfrasetyo.storyapp.data.response.ListStoryItem
import com.orryfrasetyo.storyapp.data.response.LoginResponse
import com.orryfrasetyo.storyapp.data.response.RegisterResponse
import com.orryfrasetyo.storyapp.data.response.UploadResponse
import com.orryfrasetyo.storyapp.data.userpref.UserModel
import com.orryfrasetyo.storyapp.data.userpref.UserPreferences
import com.orryfrasetyo.storyapp.view.StoryPagingSource
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository (
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
){

    fun signup(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultState<RegisterResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val body = Gson().fromJson(error, ErrorResponse::class.java)
            emit(ResultState.Error(body.message))
        }
    }

    fun login(
        email: String,
        password: String
    ): LiveData<ResultState<LoginResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.login(email, password)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val body = Gson().fromJson(error, ErrorResponse::class.java)
            emit(ResultState.Error(body.message))
        }
    }

    fun getStory(): LiveData<ResultState<List<ListStoryItem>>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getStory()
            emit(ResultState.Success(response.listStory))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Error"))
        }
    }

    fun uploadImage(
        imageFile: File,
        description: String
    ): LiveData<ResultState<UploadResponse>> = liveData {
        emit(ResultState.Loading)
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        try {
            val successResponse = apiService.uploadImage(multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UploadResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun getStoriesWithLocation(): LiveData<ResultState<List<ListStoryItem>>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getStoriesWithLocation()
            emit(ResultState.Success(response.listStory))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Error"))
        }
    }

    fun getStories() : LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun saveSession(user: UserModel) {
        userPreferences.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }

    suspend fun logout() {
        userPreferences.logout()
    }

    companion object {

        private var INSTANCE: UserRepository? = null

        fun clearInstance() {
            INSTANCE = null
        }

        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): UserRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserRepository(apiService, userPreferences)
            }.also { INSTANCE = it }
    }
}