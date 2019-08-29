package com.llm.data.models

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.llm.data.network.NetworkState

data class RepoResult(val deliveryData:LiveData<PagedList<DeliveryItemDataModel>>,
                      val networkState:LiveData<NetworkState>, val retry: ()->Unit)