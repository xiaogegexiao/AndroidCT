package com.android.androidct.injection.modules

import android.app.Application
import com.android.androidct.repo.APIClient
import com.android.androidct.repo.ImageDownloader
import com.android.androidct.repo.LocalPreferences
import com.google.gson.FieldNamingPolicy
import com.google.gson.FieldNamingStrategy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by xiaomei on 23/2/18.
 */
@Module
class AppModule(private val application: Application) {

    companion object {
        private const val DISK_CACHE_SIZE = (100 * 1024 * 1024).toLong()

        private const val CONNECT_TIMEOUT_MILLIS = 15 * 1000 // 15s
        private const val READ_TIMEOUT_MILLIS = 20 * 1000 // 20s
    }

    @Provides
    @Singleton
    fun provideLocalPreferences(gson: Gson): LocalPreferences{
        return LocalPreferences(application, gson)
    }

    @Provides
    @Singleton
    internal fun provideImageDownloader(): ImageDownloader {
        return ImageDownloader(application, DISK_CACHE_SIZE)
    }

    @Provides
    @Singleton
    internal fun provideImageCache(): LruCache {
        return LruCache(application)
    }

    @Provides
    @Singleton
    internal fun providePicasso(downloader: ImageDownloader, cache: LruCache): Picasso {
        val builder = Picasso.Builder(application)
                .downloader(downloader.downloader)
                .memoryCache(cache)
                .listener { picasso, uri, e -> }
        val p = builder.build()
        Picasso.setSingletonInstance(p)
        return p
    }

    @Singleton
    @Provides
    internal fun provideOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder
                .connectTimeout(CONNECT_TIMEOUT_MILLIS.toLong(), TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT_MILLIS.toLong(), TimeUnit.MILLISECONDS)
                .cache(Cache(application.cacheDir, 1024))
        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting()
                .create()
    }

    @Singleton
    @Provides
    fun providesAPIClient(okHttpClient: OkHttpClient,
                          preferences: LocalPreferences,
                          gson: Gson): APIClient{
        return APIClient(okHttpClient, preferences, gson)
    }
}