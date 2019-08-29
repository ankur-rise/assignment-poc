package com.llm

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.llm.di.components.DaggerIApplicationComponent
import com.llm.di.components.IApplicationComponent
import com.llm.di.modules.ContextModule

class MyApplication : Application() {
    private var applicationComponent: IApplicationComponent? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
        Fresco.initialize(this)
    }

    fun getApplicationComponent(): IApplicationComponent? {
        if (applicationComponent == null) {
            applicationComponent = DaggerIApplicationComponent.builder().contextModule(ContextModule(this)).build()
        }

        return applicationComponent
    }

    companion object {
        @JvmStatic
        var instance: MyApplication? = null
            private set
    }
}
