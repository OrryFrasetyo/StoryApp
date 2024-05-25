package com.orryfrasetyo.storyapp.data.api

import com.orryfrasetyo.storyapp.data.response.LoginResponse
import com.orryfrasetyo.storyapp.data.response.RegisterResponse
import com.orryfrasetyo.storyapp.data.response.StoryResponse
import com.orryfrasetyo.storyapp.data.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStory(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): UploadResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location: Int = 1,
    ): StoryResponse


}