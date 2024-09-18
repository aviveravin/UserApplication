package com.example.usersapplication.repository

import com.example.usersapplication.model.UserResponse
import com.example.usersapplication.network.ApiService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getUsers(page: Int): Response<UserResponse> {
        return apiService.getUsers(page)
    }
}
