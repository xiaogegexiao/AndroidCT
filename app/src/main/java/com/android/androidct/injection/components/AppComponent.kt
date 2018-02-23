package com.android.androidct.injection.components

import com.android.androidct.injection.CTApplication
import com.android.androidct.injection.modules.AppModule
import com.android.androidct.view.DetailActivity
import com.android.androidct.view.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by xiaomei on 23/2/18.
 */
@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {
    fun inject(application: CTApplication)
    fun inject(mainActivity: MainActivity)
    fun inject(detailActivity: DetailActivity)
}