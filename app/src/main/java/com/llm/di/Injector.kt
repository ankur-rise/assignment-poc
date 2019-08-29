package com.llm.di

import com.llm.MyApplication
import com.llm.di.components.IActivityComponent
import com.llm.di.components.IApplicationComponent

object Injector {


    private var mAppComponent: IApplicationComponent? = null

    fun inject(): IActivityComponent {
        if (mAppComponent == null) {
            mAppComponent = MyApplication.instance!!.getApplicationComponent()
        }
        return mAppComponent!!.plusActivityComponent()
    }
}
