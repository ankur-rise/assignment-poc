package com.llm.data.network

import retrofit2.Call
import retrofit2.Callback


const val MAX_RETRY = 3
abstract class CallbackWithRetry<T> : Callback<T> {
    private var  retry:Int = 0
    private val TAG = CallbackWithRetry::class.java.simpleName
    override fun onFailure(call: Call<T>, t: Throwable) {

        if(retry++< MAX_RETRY) {

            call.clone().enqueue(this)
        }
    }

    fun isRetry():Boolean {
        return retry< MAX_RETRY
    }

}