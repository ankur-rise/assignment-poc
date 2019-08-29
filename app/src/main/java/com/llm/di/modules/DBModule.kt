package com.llm.di.modules

import android.content.Context
import androidx.room.Room
import com.llm.data.db.AppDB
import com.llm.data.db.DB_NAME
import com.llm.di.qualifiers.AppContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DBModule {

    @Provides
    @Singleton
    fun getDatabase(@AppContext context: Context): AppDB {
        return Room.databaseBuilder(
            context, AppDB::class.java,
            DB_NAME
        ).build()
    }

}