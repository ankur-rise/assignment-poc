package com.llm

import androidx.paging.DataSource
import com.llm.data.models.DeliveryItemDataModel

class MockDataSourceFactory : DataSource.Factory<Int, DeliveryItemDataModel>() {
    override fun create(): DataSource<Int, DeliveryItemDataModel> {

        val dataSource = MockDataSource()

        return dataSource
    }
}