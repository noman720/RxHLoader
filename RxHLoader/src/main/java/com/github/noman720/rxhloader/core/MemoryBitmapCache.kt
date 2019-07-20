package com.github.noman720.rxhloader.core

import android.graphics.Bitmap
import android.util.LruCache

/**
 * Holds objects temporarily — until the app gets killed.
 * The methods of this Cache are thread safe.
 *
 * Created by Abu Noman on 7/20/19.
 */
class MemoryBitmapCache private constructor(): BitmapCache {

    private val mCache = LruCache<String, Bitmap>(CACHE_SIZE_BYTES)

    override val name: String
        get() = "Memory Cache"

    @Synchronized
    override fun containsKey(key: String): Boolean {
        val existingBitmap = get(key)
        return existingBitmap != null
    }

    override fun get(key: String): Bitmap? {
        return mCache.get(key)
    }

    override fun save(key: String, dataToSave: Bitmap) {
        mCache.put(key, dataToSave)
    }

    override fun clear() {
        mCache.evictAll()
    }

    companion object {

        // 4MB cache
        const val CACHE_SIZE_BYTES = 4*1024*1024

        /**
         * This is ~500KB on a Moto G3, good enough for storing 10k Strings.
         */
//        const val CACHE_SIZE_BYTES = (Runtime.getRuntime().maxMemory() / 1024 / 2000).toInt()

        fun getInstance() = MemoryBitmapCache()
    }

}