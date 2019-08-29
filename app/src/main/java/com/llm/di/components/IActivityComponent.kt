package com.llm.di.components

import com.llm.ui.DeliveryDetailActivity
import com.llm.ui.DeliveryListActivity
import dagger.Subcomponent
import javax.inject.Singleton

@Subcomponent
interface IActivityComponent {

    fun inject(activity: DeliveryListActivity)
    fun inject(activity: DeliveryDetailActivity)

}