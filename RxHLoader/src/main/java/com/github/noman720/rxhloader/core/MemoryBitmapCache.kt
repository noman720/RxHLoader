package com.github.noman720.rxhloader.core

import android.graphics.Bitmap
import android.util.Log
import android.util.LruCache
import java.nio.charset.Charset

/**
 * Holds objects temporarily — until the app gets killed.
 * The methods of this Cache are thread safe.
 *
 * Created by Abu Noman on 7/20/19.
 */
class MemoryBitmapCache private constructor(
    private val bitmapCache: LruCache<String, Bitmap>,
    private val dataCache: LruCache<String, String>
): BitmapCache {

    override val name: String
        get() = "Memory Cache"

    @Synchronized
    override fun <T> containsKey(key: String, clazz: Class<T>): Boolean {
        val existingBitmap = get(key, clazz)
        return existingBitmap != null
    }

    override fun <T> get(key: String, clazz: Class<T>): T? {
        return when(clazz){
            Bitmap::class.java -> bitmapCache.get(key) as T
            else -> dataCache.get(key) as T
        }
    }

    override fun <T> save(key: String, dataToSave: T, clazz: Class<T>) {
        Log.i(name, "Saved ${clazz.canonicalName} successfully!")
        when(clazz){
            Bitmap::class.java -> bitmapCache.put(key, dataToSave as Bitmap)
            else -> dataCache.put(key, dataToSave as String)
        }
    }

    override fun clear() {
        bitmapCache.evictAll()
    }

    companion object {

        // 4MB cache
//        const val CACHE_SIZE_BYTES = 4*1024*1024

        /**
         * This is ~500KB on a Moto G3, good enough for storing 10k Strings.
         */
        private val CACHE_SIZE_BYTES = (Runtime.getRuntime().maxMemory() / 1024 / 8).toInt()

        private val mBitmapCache = object : LruCache<String, Bitmap>(CACHE_SIZE_BYTES) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.byteCount / 1024
            }
        }

        private val mDataCache = object: LruCache<String, String>(CACHE_SIZE_BYTES){
            override fun sizeOf(key: String?, value: String?): Int {
                val size = value?.toByteArray(Charset.forName("UTF-8"))?.size
                return if (size != null) size/1024 else 0
            }
        }

        @Volatile
        private var sMemoryCache: MemoryBitmapCache? = null

        fun getInstance(): MemoryBitmapCache =
            sMemoryCache?: synchronized(this){
                sMemoryCache?: MemoryBitmapCache(mBitmapCache, mDataCache)
            }
    }

}