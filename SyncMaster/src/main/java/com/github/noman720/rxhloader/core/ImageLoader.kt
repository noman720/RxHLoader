package com.github.noman720.rxhloader.core

import android.content.Context
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Helper class for loading photos.
 * Start at [.load].
 *
 * Created by Abu Noman on 7/20/19.
 */
internal class ImageLoader private constructor(
    private val mMemoryCache: BitmapCache,
    private val mDiskCache: BitmapCache,
    private val mNetworkClient: NetworkClient
){

    /**
     * Loads a photo from one of these 3 sources:
     * 1. Memory cache — if it was downloaded in the current session
     * 2. Disk cache   — if it was ever downloaded
     * 3. Network      — if it was never downloaded
     */
    fun <T> load(imageUrl: String, clazz: Class<T>): Observable<T> {
        return Observable.just(imageUrl).flatMap { url ->
            Log.d(TAG, "Loading ${clazz.canonicalName} Url: $url")

            // Plan A: Check in memory
            val memoryCacheObservable = loadFromCache(url, mMemoryCache, clazz)

            // Plan B: Look into files
            // (And save into memory cache)
            val diskCacheObservable = loadFromCache(url, mDiskCache, clazz)
                .doOnNext(saveToCache(url, mMemoryCache, clazz))

            // Plan C: Hit the network
            // (And save into both memory and disk cache for future calls)
            val networkObservable = Observable.defer {
                Log.i(TAG, "Downloading from the Internet")
                mNetworkClient
                    .loadImage(url, clazz)
                    .doOnNext(saveToCache(url, mMemoryCache, clazz))
                    .doOnNext(saveToCache(url, mDiskCache, clazz))

            }

            Observable
                .concat(memoryCacheObservable, diskCacheObservable, networkObservable)
                // Calling first() will stop the stream as soon as one item is emitted.
                // This way, the cheapest source (memory) gets to emit first and the
                // most expensive source (network) is only reached when no other source
                // could emit any cached Bitmap.
                .firstOrError()
                .toObservable()
        }
    }

    private fun <T> saveToCache(imageUrl: String, bitmapCache: BitmapCache, clazz: Class<T>): Consumer<T> {
        return Consumer { data ->
            Log.i(TAG, "Saving to: " + bitmapCache.name)
            bitmapCache.save(imageUrl, data, clazz)
        }
    }

    /**
     * Returns a stream of the cached bitmap in <var>whichBitmapCache</var>.
     * The emitted item can be null if this cache source does not have anything to offer.
     */
    private fun <T> loadFromCache(imageUrl: String, whichBitmapCache: BitmapCache, clazz: Class<T>): Observable<T> {
        val imageBitmap = whichBitmapCache[imageUrl, clazz]
        val cacheName = whichBitmapCache.name
//        Log.i(TAG, "Checking: $cacheName")
        return if (imageBitmap == null) {
            Log.i(TAG, "Does not have this Url in $cacheName!")
            Completable.complete().toObservable()
        } else Observable
            .just(imageBitmap)
            .compose { observable ->
                observable.doOnNext {
                    Log.i(TAG, "Url found in $cacheName!")
                }
            }
    }

    /**
     * Deletes all cached Bitmaps in both memory and disk cache.
     */
    fun clearCache() {
        mDiskCache.clear()
        mMemoryCache.clear()
    }

    companion object {

        const val TAG = "ImageLoader"

        @Volatile
        private var sImageLoader: ImageLoader? = null

        fun getInstance(context: Context): ImageLoader =
            sImageLoader?: synchronized(this) {
                sImageLoader?: ImageLoader(
                    MemoryBitmapCache.getInstance(),
                    DiskBitmapCache.getInstance(context),
                    NetworkClient.getInstance()
                )
            }
    }

}