package com.llm.data.repository

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.llm.R
import com.llm.data.CustomBoundaryCallback
import com.llm.data.IDeliveryRepo
import com.llm.data.db.AppDB
import com.llm.data.models.DeliveryItemDataModel
import com.llm.data.models.RepoResult
import com.llm.data.network.Apis
import com.llm.data.network.CallbackWithRetry
import com.llm.di.qualifiers.SingleThreadExecutor
import com.llm.ui.utils.Utils
import org.jetbrains.annotations.NotNull
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class DeliveryRepo @Inject constructor(
    @NotNull private val api: Apis,
    private val db:AppDB,
    private val utils: Utils,
    @SingleThreadExecutor private val executor: Executor
) : IDeliveryRepo {


    override fun getDeliveryItems(): RepoResult {
        val dao = db.deliveryDao()
        val dataSourceFactory = dao.get()
        val callback = CustomBoundaryCallback(dao, executor, :: getDeliveries)
        val networkErr = callback.networkState

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPageSize(DATABASE_PAGE_SIZE)
            .build()

        val pagedList = LivePagedListBuilder(dataSourceFactory, pagedListConfig).setBoundaryCallback(callback).build()
        return RepoResult(pagedList, networkErr, retry = {callback.retryFailedReq()})
    }


    override fun refreshData( onError: (errMsg: String) -> Unit){
        getDeliveries(
            0,
            NETWORK_PAGE_SIZE,
            onSuccess = { data: List<DeliveryItemDataModel> -> refreshDB(data) },
            onError = {
                onError(it)
            })
    }

    override fun getDeliveries(
        offset: Int,
        limit: Int,
        onSuccess: (data: List<DeliveryItemDataModel>) -> Unit,
        onError: (errMsg: String) -> Unit
    ) {
        if (!utils.isConnectedToInternet()) {
            onError(utils.getString(R.string.internet_error))
            return
        }
        val map = HashMap<String, Int>()
        map["offset"] = offset
        map["limit"] = limit
        val call = api.getDeliveries(map)
        call.enqueue(object : CallbackWithRetry<List<DeliveryItemDataModel>>() {
            override fun onResponse(
                call: Call<List<DeliveryItemDataModel>>,
                response: Response<List<DeliveryItemDataModel>>
            ) {
                if (response.isSuccessful) {
                    val dataDelivery: List<DeliveryItemDataModel>? = response.body()
                    if(dataDelivery!=null) {
                        onSuccess(dataDelivery)
                    }
                    else{
                        onError(utils.getString(R.string.empty_data))
                    }

                } else {
                    val msg = getMessage(response.message(), utils.getString(R.string.server_error))
                    onError(msg)
                }

            }

            override fun onFailure(call: Call<List<DeliveryItemDataModel>>, t: Throwable) {

                super.onFailure(call, t)
                if (!isRetry()) {
                    val msg = getMessage(t.message, utils.getString(R.string.netwrok_error))
                    onError(msg)
                }
            }
        })
    }

    private fun getMessage(original:String?, alternate: String): String {
        var msg = original
        if(msg==null || msg.isEmpty())
            msg = alternate
        return msg
    }

    private fun refreshDB(data: List<DeliveryItemDataModel>) {
        executor.execute {
            db.runInTransaction {
                val dao = db.deliveryDao()
                dao.clearTable()
                dao.insertAll(data)
            }

        }
    }


    companion object {
        private const val DATABASE_PAGE_SIZE = 20
        const val INITIAL_LOAD_SIZE_HINT = 20
        const val NETWORK_PAGE_SIZE = 20
    }
}

