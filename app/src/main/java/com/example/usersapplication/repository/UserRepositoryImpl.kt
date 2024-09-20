package com.example.usersapplication.repository

import com.example.usersapplication.model.User
import com.example.usersapplication.model.UserListResponse
import com.example.usersapplication.model.UserResponse
import com.example.usersapplication.util.ResponseResult
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface UserRepositoryImpl {
    suspend fun getUsers(page: Int): ResponseResult<UserListResponse>

    suspend fun getUserById(id: Int): ResponseResult<UserResponse>
}