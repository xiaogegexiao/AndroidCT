package com.android.androidct.repo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.jakewharton.picasso.OkHttp3Downloader
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import java.io.IOException

/**
 * Created by xiaomei on 23/2/18.
 */
class ImageDownloader(context: Context, maxSize: Long) {

    private val mOkHttpClient: OkHttpClient
    val downloader: OkHttp3Downloader
    private val mCacheDir: File
    private val mCache: Cache

    init {
        mCacheDir = defaultCacheDir(context)
        mCache = Cache(mCacheDir, maxSize)
        mOkHttpClient = OkHttpClient.Builder()
                .cache(mCache)
                .build()
        downloader = OkHttp3Downloader(mOkHttpClient)
    }

    fun isCached(url: String): Boolean {
        try {
            val response = downloader.load(Uri.parse(url), 1 shl 2)
            return response.contentLength > 0
        } catch (e: IOException) {
            return false
        }

    }

    fun getCachedBitmap(url: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val response = downloader.load(Uri.parse(url), 1 shl 2)
            bitmap = BitmapFactory.decodeStream(response.inputStream)
        } catch (e: IOException) {
            // Ignore
        }

        return bitmap
    }

    fun clearCache() {
        try {
            mCache.evictAll()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    companion object {
        private const val PICASSO_CACHE = "picasso-cache"

        private fun defaultCacheDir(context: Context): File {
            val cache = File(context.applicationContext.cacheDir, PICASSO_CACHE)
            if (!cache.exists()) {

                cache.mkdirs()
            }
            return cache
        }
    }
}