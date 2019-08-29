package com.llm.data.network

import com.llm.data.models.DeliveryItemDataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface Apis {

    @GET("deliveries")
    fun getDeliveries(@QueryMap map:Map<String, Int>): Call<List<DeliveryItemDataModel>>

}