package com.llm.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.llm.data.db.DeliveryDao
import com.llm.data.models.DeliveryItemDataModel
import com.llm.data.network.NetworkState
import com.llm.data.repository.DeliveryRepo.Companion.NETWORK_PAGE_SIZE
import java.util.concurrent.Executor


class CustomBoundaryCallback constructor(
    private val dao: DeliveryDao,
    private val executor: Executor, val getDeliveries: (
        offset: Int, limit: Int, onSuccess: (data: List<DeliveryItemDataModel>) -> Unit,
        onError: (errMsg: String) -> Unit
    ) -> Unit
) : PagedList.BoundaryCallback<DeliveryItemDataModel>() {
    private val TAG: String = CustomBoundaryCallback::class.java.simpleName
    private var isRequestInProgress: Boolean = false
    private var mCurrentOffset:Int=0


    private val _networkState = MutableLiveData<NetworkState>()
    // LiveData of network errors.
    val networkState: LiveData<NetworkState>
        get() = _networkState


    override fun onZeroItemsLoaded() {
        Log.i(TAG, "onZeroItemsLoaded")
        getData(0)

    }


    override fun onItemAtEndLoaded(itemAtEnd: DeliveryItemDataModel) {
        Log.i(TAG, "onItemAtEndLoaded")
        getData(itemAtEnd.id + 1)

        mCurrentOffset = itemAtEnd.id+1

    }

    private fun getData(offset: Int) {
        if (isRequestInProgress) return
        isRequestInProgress = true

        _networkState.value = NetworkState.LOADING

        getDeliveries(offset, NETWORK_PAGE_SIZE, {

            isRequestInProgress = false
            saveDataInDB(it)
            _networkState.value = NetworkState.LOADED

        }, { errMsg ->

            isRequestInProgress = false
            _networkState.value = NetworkState.error(errMsg)

        })
    }

    fun retryFailedReq(){
        getData(mCurrentOffset)
    }

    private fun saveDataInDB(data: List<DeliveryItemDataModel>) {
        executor.execute {
            dao.insertAll(data)
        }
    }

}