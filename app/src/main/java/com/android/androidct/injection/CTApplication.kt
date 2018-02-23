package com.android.androidct.injection

import android.app.Application
import com.android.androidct.injection.components.AppComponent
import com.android.androidct.injection.components.DaggerAppComponent
import com.android.androidct.injection.modules.AppModule

/**
 * Created by xiaomei on 23/2/18.
 */
class CTApplication : Application() {
    companion object {
        @JvmStatic lateinit var graph: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        graph = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        graph.inject(this)
    }
}