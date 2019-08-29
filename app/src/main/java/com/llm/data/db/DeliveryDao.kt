package com.llm.data.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.llm.data.models.DeliveryItemDataModel

@Dao
interface DeliveryDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll (items:List<DeliveryItemDataModel>)

    @Query("Select * from DeliveryItemDataModel ORDER BY id ASC")
    fun get(): DataSource.Factory<Int, DeliveryItemDataModel>

    @Query("DELETE FROM DeliveryItemDataModel")
    fun clearTable()


}