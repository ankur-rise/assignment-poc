package com.llm

import com.llm.data.models.DeliveryItemDataModel

interface MockCallback {
    fun onSuccess(data:List<DeliveryItemDataModel>)
    fun onError(errMsg:String)
}