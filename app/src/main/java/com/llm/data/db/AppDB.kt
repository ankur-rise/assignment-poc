package com.llm.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.llm.data.models.DeliveryItemDataModel

const val DB_NAME = "lalamove_assignment.db"
@Database(entities = arrayOf(DeliveryItemDataModel::class), version = 1)
abstract class AppDB : RoomDatabase() {
    abstract fun deliveryDao(): DeliveryDao
}
