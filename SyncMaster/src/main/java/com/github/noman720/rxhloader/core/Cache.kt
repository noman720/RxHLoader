package com.github.noman720.rxhloader.core

/**
 * Interface for a fixed-size local storage.
 * Implemented by [MemoryCache] and [DiskCache].
 *
 * Created by Abu Noman on 7/20/19.
 */
interface Cache {

    /**
     * For debugging
     */
    val name: String

    /**
     * Whether any object getInstance <var>key</var> exists
     */
    fun <T> containsKey(key: String, clazz: Class<T>): Boolean

    /**
     * Gets the object mapped against <var>key</var>.
     */
    operator fun <T> get(key: String, clazz: Class<T>): T?

    /**
     * Saves <var>bitmapToSave</var> against <var>key</var>.
     */
    fun <T> save(key: String, dataToSave: T, clazz: Class<T>)

    /**
     * Deletes everything in this cache.
     */
    fun clear()

}