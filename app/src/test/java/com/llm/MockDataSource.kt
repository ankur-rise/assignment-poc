package com.llm

import androidx.paging.PositionalDataSource
import com.llm.data.models.DeliveryItemDataModel

class MockDataSource : PositionalDataSource<DeliveryItemDataModel>(){
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<DeliveryItemDataModel>) {

    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<DeliveryItemDataModel>) {

    }


}