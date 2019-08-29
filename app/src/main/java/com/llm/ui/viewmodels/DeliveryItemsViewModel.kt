package com.llm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.llm.data.models.DeliveryItemDataModel
import com.llm.data.models.RepoResult
import com.llm.data.network.NetworkState
import com.llm.data.repository.DeliveryRepo
import javax.inject.Inject

class DeliveryItemsViewModel @Inject constructor(private val deliveryRepo: DeliveryRepo) : ViewModel() {


    private val deliveryData = MutableLiveData<Unit>()
    private val repoResult: LiveData<RepoResult> = Transformations.map(deliveryData) {
        deliveryRepo.getDeliveryItems()
    }


    val resultLiveData: LiveData<PagedList<DeliveryItemDataModel>> =
        Transformations.switchMap(repoResult) { it.deliveryData }
    val networkState: LiveData<NetworkState> = Transformations.switchMap(repoResult) { it.networkState }
    fun loadDelivery() {
        deliveryData.postValue(Unit)
    }


    private var _errRefresh = MutableLiveData<String>()
    val errRefreshLiveData: LiveData<String> = Transformations.map(_errRefresh) {
        it
    }

    fun refreshData() {
        deliveryRepo.refreshData(onError = {

            _errRefresh.postValue(it)

        })
    }

    fun retryFailedReq() {
        repoResult.value?.retry?.invoke()
    }


}

