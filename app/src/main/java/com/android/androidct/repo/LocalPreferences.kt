package com.android.androidct.repo

import android.content.Context
import android.preference.PreferenceManager
import com.android.androidct.repo.entities.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.stream.Collectors.mapping
import com.google.gson.JsonElement



/**
 * Created by xiaomei on 23/2/18.
 */
class LocalPreferences(private var context: Context, private var gson: Gson) {
    var cache : String? = null

    var items: List<Item> = emptyList()
        private set
        get() {
            return cache?.let {
                val listType = object : TypeToken<List<Item>>() {}.type
                return Gson().fromJson(it, listType)
            } ?: kotlin.run {
                return emptyList()
            }
        }

    init {
        load()
    }

    private fun load() {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        cache = pref.getString(PREF_CACHE, null)
    }

    public fun clear() {
        cache = null
    }

    public fun save() {
        val edit = PreferenceManager.getDefaultSharedPreferences(context).edit()
        edit.putString(PREF_CACHE, cache)
        edit.apply()
    }

    companion object {
        const val PREF_CACHE = "cache";
    }
}