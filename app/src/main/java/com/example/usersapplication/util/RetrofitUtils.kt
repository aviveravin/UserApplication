package com.example.usersapplication.util

import android.util.Log
import retrofit2.Response
import com.example.usersapplication.util.getResponse

inline fun <reified T> Response<T>.getResponse(): T {
    val responseBody = body()
    Log.d("RetrofitUtils", "Global notification impl, #Abhi getResponse: responseBody = $responseBody")
    return if (this.isSuccessful && responseBody != null) {
        responseBody
    } else {
        fromJson<T>(errorBody()!!.string())!!
    }
}