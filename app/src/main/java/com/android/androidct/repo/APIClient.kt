package com.android.androidct.repo

import com.android.androidct.repo.entities.Item
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Maybe
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


/**
 * Created by xiaomei on 23/2/18.
 */

class APIClient
/**
 * Constructor
 */
(private val mHttpClient: OkHttpClient,
 private val mPreferences: LocalPreferences,
 private val mGson: Gson) {
    interface Client {

        @get:GET("/bins/b7jwr")
        val getItems: Maybe<List<Item>>
    }

    private lateinit var mClient: Client

    init {
        setupApi(URL)
    }

    private fun setupApi(baseUrl: String) {
        val builder = Retrofit.Builder()

        val restAdapter = builder
                .baseUrl(baseUrl)
                .client(mHttpClient)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()

        mClient = restAdapter.create(Client::class.java)
    }

    /**
     * General API methods
     */
    fun listItems(): Maybe<List<Item>> {
        return mClient.getItems.map(object : Function<List<Item>, List<Item>> {
            override fun apply(t: List<Item>): List<Item> {
                mPreferences.cache = mGson.toJson(t)
                mPreferences.save()
                return t
            }
        })
    }

    companion object {

        private val TAG = APIClient::class.java.simpleName
        private const val URL = "https://api.myjson.com/"
    }
}