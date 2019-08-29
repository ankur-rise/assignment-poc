package com.llm.ui.utils

import android.content.Context
import android.net.ConnectivityManager
import com.llm.di.qualifiers.AppContext
import javax.inject.Inject

open class Utils @Inject constructor(@AppContext private val context: Context) {

    open fun isConnectedToInternet():Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    fun getString(id:Int):String {
        return context.getString(id)
    }

}