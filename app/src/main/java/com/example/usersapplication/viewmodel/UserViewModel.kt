package com.example.usersapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usersapplication.model.User
import com.example.usersapplication.model.UserListResponse
import com.example.usersapplication.model.UserResponse
import com.example.usersapplication.repository.UserRepository
import com.example.usersapplication.util.ResponseResult
import com.example.usersapplication.util.ViewState
import com.example.usersapplication.util.shareWhileObserved
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _getAllUsersFlow = MutableSharedFlow<ViewState<UserListResponse>>()
    val getAllUsersFlow: SharedFlow<ViewState<UserListResponse>> =
        _getAllUsersFlow.shareWhileObserved(viewModelScope)

    private val _getUserFlow = MutableSharedFlow<ViewState<UserResponse>>()
    val getUserFlow: SharedFlow<ViewState<UserResponse>> =
        _getUserFlow.shareWhileObserved(viewModelScope)

    fun fetchUsers(page: Int) {
        viewModelScope.launch {
            _getAllUsersFlow.emit(ViewState.loading())
            val viewState = when (val responseState =
                repository.getUsers(page)) {
                is ResponseResult.Success -> ViewState.success(responseState.data)
                is ResponseResult.Error -> ViewState.failed(responseState.message)
                is ResponseResult.NetworkException -> ViewState.NetworkFailed(responseState.networkError)
                is ResponseResult.ErrorException -> ViewState.exceptionError(responseState.exception)
                else -> ViewState.exceptionError(responseState.toString())
            }
            _getAllUsersFlow.emit(viewState)
        }
    }

    fun fetchUserById(id: Int) {
        viewModelScope.launch {
            _getUserFlow.emit(ViewState.loading())
            val viewState = when (val responseState =
                repository.getUserById(id)) {
                is ResponseResult.Success -> ViewState.success(responseState.data)
                is ResponseResult.Error -> ViewState.failed(responseState.message)
                is ResponseResult.NetworkException -> ViewState.NetworkFailed(responseState.networkError)
                is ResponseResult.ErrorException -> ViewState.exceptionError(responseState.exception)
                else -> ViewState.exceptionError(responseState.toString())
            }
            _getUserFlow.emit(viewState)
        }
    }
}

