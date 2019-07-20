package com.github.noman720.rxhloader.core

import android.graphics.Bitmap

/**
 * Interface for a fixed-size local storage.
 * Implemented by [MemoryBitmapCache] and [DiskBitmapCache].
 *
 * Created by Abu Noman on 7/20/19.
 */
interface BitmapCache {

    /**
     * For debugging
     */
    val name: String

    /**
     * Whether any object getInstance <var>key</var> exists
     */
    fun containsKey(key: String): Boolean

    /**
     * Gets the object mapped against <var>key</var>.
     */
    operator fun get(key: String): Bitmap?

    /**
     * Saves <var>bitmapToSave</var> against <var>key</var>.
     */
    fun save(key: String, dataToSave: Bitmap)

    /**
     * Deletes everything in this cache.
     */
    fun clear()

}