package com.llm.di.modules

import com.llm.data.IDeliveryRepo
import com.llm.data.repository.DeliveryRepo
import dagger.Binds
import dagger.Module

@Module
abstract class RepoModule {

    @Binds
    abstract fun getRepo(repo:DeliveryRepo): IDeliveryRepo



}