package com.example.usersapplication.repository

import android.util.Log
import com.example.usersapplication.model.User
import com.example.usersapplication.model.UserListResponse
import com.example.usersapplication.model.UserResponse
import com.example.usersapplication.network.ApiService
import com.example.usersapplication.util.ResponseResult
import retrofit2.HttpException
import com.example.usersapplication.util.getResponse
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService
) : UserRepositoryImpl {
    override suspend fun getUsers(page: Int): ResponseResult<UserListResponse> {
        return try {
            val response = apiService.getUsers(page).getResponse()
            Log.d("TAG", "getAllUsers: check the response $response")
            if (response.data.isNotEmpty()) {
                ResponseResult.success(response)
            } else {
                ResponseResult.error("No data available")
            }
        } catch (e: HttpException) {
            ResponseResult.networkError("Constants.ERROR_MESSAGE")
        } catch (connection: java.net.ConnectException) {
            ResponseResult.networkError(connection.toString())
        } catch (e: Exception) {
            Log.d(
                "UsersRepository",
                "#users initializeObservers getAllUsers: viewState =, Exception e = $e"
            )
            ResponseResult.exception("Constants.ERROR_MESSAGE")
        }
    }

    override suspend fun getUserById(id: Int): ResponseResult<UserResponse> {
        return try {
            val response = apiService.getUserById(id).getResponse()
            Log.d("TAG", "getAllUsers: check the response $response")
            ResponseResult.success(response)
        } catch (e: HttpException) {
            ResponseResult.networkError("Constants.ERROR_MESSAGE")
        } catch (connection: java.net.ConnectException) {
            ResponseResult.networkError(connection.toString())
        } catch (e: Exception) {
            Log.d(
                "UsersRepository",
                "#users initializeObservers getAllUsers: viewState =, Exception e = $e"
            )
            ResponseResult.exception("Constants.ERROR_MESSAGE")
        }
    }
}
