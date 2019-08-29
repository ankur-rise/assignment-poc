package com.llm.di.modules;

import android.content.Context;
import com.llm.di.qualifiers.AppContext;
import com.llm.di.qualifiers.SingleThreadExecutor;
import com.llm.ui.utils.Utils;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Module
public class AppModule {

    @Provides
    @Singleton
    public Utils getNetworkUtil(@AppContext Context context){
        return new Utils(context);
    }

    @Provides
    @Singleton
    @SingleThreadExecutor
    public Executor getThreadPoolExecutor() {
        return Executors.newSingleThreadExecutor();
    }

}
