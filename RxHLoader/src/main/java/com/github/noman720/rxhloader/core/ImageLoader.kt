package com.github.noman720.rxhloader.core

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Helper class for loading photos.
 * Start at [.load].
 *
 * Created by Abu Noman on 7/20/19.
 */
internal class ImageLoader private constructor(
    private val mDiskCache: BitmapCache,
    private val mMemoryCache: BitmapCache,
    private val mNetworkClient: NetworkClient
){

    /**
     * Loads a photo from one of these 3 sources:
     * 1. Memory cache — if it was downloaded in the current session
     * 2. Disk cache   — if it was ever downloaded
     * 3. Network      — if it was never downloaded
     */
    fun load(imageUrl: String): Observable<Bitmap> {
        return Observable.just(imageUrl).flatMap { url ->
            Log.d(TAG, "Loading image Url: $url")

            // Plan A: Check in memory
            val memoryCacheLoadObs = loadFromCache(url, mMemoryCache)

            // Plan B: Look into files
            // (And save into memory cache)
            val diskImageObservable = loadFromCache(url, mDiskCache)
                .doOnNext(saveToCache(url, mMemoryCache))

            // Plan C: Hit the network
            // (And save into both memory and disk cache for future calls)
            val networkLoadObs = Observable.defer {
                Log.i(TAG, "Downloading from the Internet")
                mNetworkClient
                    .loadImage(url)
                    .doOnNext(saveToCache(url, mMemoryCache))
                    .doOnNext(saveToCache(url, mDiskCache))
            }

            Observable
                .concat(memoryCacheLoadObs, diskImageObservable, networkLoadObs)
                // Calling first() will stop the stream as soon as one item is emitted.
                // This way, the cheapest source (memory) gets to emit first and the
                // most expensive source (network) is only reached when no other source
                // could emit any cached Bitmap.
                .firstOrError()
                .toObservable()
        }
    }

    private fun saveToCache(imageUrl: String, bitmapCache: BitmapCache): Consumer<Bitmap> {
        return Consumer { bitmap ->
            Log.i(TAG, "Saving to: " + bitmapCache.name)
            bitmapCache.save(imageUrl, bitmap, Bitmap::class.java)
        }
    }

    /**
     * Returns a stream of the cached bitmap in <var>whichBitmapCache</var>.
     * The emitted item can be null if this cache source does not have anything to offer.
     */
    private fun loadFromCache(imageUrl: String, whichBitmapCache: BitmapCache): Observable<Bitmap> {
        val imageBitmap = whichBitmapCache[imageUrl, Bitmap::class.java]
        return Observable
            .just(imageBitmap)
//            .compose { observable ->
//                observable.doOnNext {
//                    val cacheName = whichBitmapCache.name
//                    Log.i(TAG, "Checking: $cacheName")
//                    if (it == null) {
//                        Log.i(TAG, "Does not have this Url")
//                    } else {
//                        Log.i(TAG, "Url found in cache!")
//                    }
//                }
//            }
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