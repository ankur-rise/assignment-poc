package com.llm.di.modules

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = arrayOf(AssistedInject_ViewModelAssistedFactoriesModule::class))
abstract class ViewModelAssistedFactoriesModule