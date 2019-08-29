package com.llm.di.modules

import android.content.Context
import com.llm.di.qualifiers.AppContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(private val context: Context) {

    @Provides
    @Singleton
    @AppContext
    fun getContext(): Context {
        return context
    }

}