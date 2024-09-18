package com.example.usersapplication.network

import com.example.usersapplication.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/users")
    suspend fun getUsers(@Query("page") page: Int): Response<UserResponse>
}