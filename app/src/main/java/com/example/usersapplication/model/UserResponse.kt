package com.example.usersapplication.model

// Wrapper class for the response
data class UserResponse(
    val data: User,
    val support: Support
)

// User data class
data class User(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val avatar: String
)

// Support data class (for the 'support' field)
data class Support(
    val url: String,
    val text: String
)
