package com.llm.data

import com.llm.data.models.DeliveryItemDataModel
import com.llm.data.models.RepoResult

interface IDeliveryRepo {

    fun getDeliveryItems(): RepoResult

    fun getDeliveries(offset: Int, limit: Int, onSuccess: (data:List<DeliveryItemDataModel>)->Unit, onError:(errMsg:String)-> Unit)
    fun refreshData(onError: (errMsg: String) -> Unit)
}