package com.example.usersapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.usersapplication.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserViewModelFactory @Inject constructor(private val instance: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(UserRepository::class.java).newInstance(instance)
    }
}