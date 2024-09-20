package com.example.usersapplication.network

import com.example.usersapplication.model.User
import com.example.usersapplication.model.UserListResponse
import com.example.usersapplication.model.UserResponse
import com.example.usersapplication.util.ResponseResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("api/users")
    suspend fun getUsers(@Query("page") page: Int): Response<UserListResponse>

    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<UserResponse>
}