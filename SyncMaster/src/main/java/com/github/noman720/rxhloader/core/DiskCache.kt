package com.github.noman720.rxhloader.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.*
import java.net.URLEncoder
import java.nio.charset.Charset

/**
 * Persists Bitmaps in files in the cache directory (See [Context.getCacheDir]).
 *
 * Created by Abu Noman on 7/20/19.
 */
class DiskCache private constructor(
    context: Context
) : Cache {

    private val mCacheDirectory: File = context.cacheDir

    override val name: String
        get() = "Disk Cache"

    @Synchronized
    override fun <T> containsKey(key: String, clazz: Class<T>): Boolean {
        val existingBitmap = get(key, clazz)
        return existingBitmap != null
    }

    @Synchronized
    override fun <T> get(key: String, clazz: Class<T>): T? {
        val cacheFileName = encodeKey(key)
        val foundCacheFiles = mCacheDirectory.listFiles { _, filename -> filename == cacheFileName }
        foundCacheFiles?.let {
            if (it.isNotEmpty()) {
                return when(clazz){
                    Bitmap::class.java -> readBitmapFromFile(foundCacheFiles[0]) as T
                    else -> readDataFromFile(foundCacheFiles[0]) as T
                }
            }
        }
        return null
    }

    @Synchronized
    override fun <T> save(key: String, dataToSave: T, clazz: Class<T>) {
        val cacheFileName = encodeKey(key)
        cacheFileName?.apply {
            val cacheFile = File(mCacheDirectory, this)
            try {
                when(clazz){
                    Bitmap::class.java -> saveBitmapToFile(dataToSave as Bitmap, FileOutputStream(cacheFile))
                    else -> saveDataToFile(dataToSave as String, FileOutputStream(cacheFile))
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @Synchronized
    override fun clear() {
        val cachedFiles = mCacheDirectory.listFiles()
        cachedFiles?.forEach {
            it.delete()
        }
        mCacheDirectory.delete()
    }

    // ======== UTILITY ======== //

    /**
     * Escapes characters in a key (which may be a Url) so that it can be
     * safely used as a File name.
     *
     * This is required because otherwise keys having "\\" may be considered
     * as directory path separators.
     */
    private fun encodeKey(toEncodeString: String): String? {
        try {
            return URLEncoder.encode(toEncodeString, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return null
    }

    private fun readBitmapFromFile(foundCacheFile: File): Bitmap? {
        try {
            val fileInputStream = FileInputStream(foundCacheFile)
            return BitmapFactory.decodeStream(fileInputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    private fun readDataFromFile(foundCacheFile: File): String? {
        try {
            val fileInputStream = FileInputStream(foundCacheFile)
            return fileInputStream.bufferedReader().use {
                it.readText()
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    @Throws(IOException::class)
    private fun saveBitmapToFile(bitmapToSave: Bitmap, fileOutputStream: FileOutputStream) {
        // auto close file output stream
        fileOutputStream.use {
            bitmapToSave.compress(Bitmap.CompressFormat.PNG, 100, it)
            Log.i(name, "Saved image successfully!")
        }
    }

    @Throws(IOException::class)
    private fun saveDataToFile(dataToSave: String, fileOutputStream: FileOutputStream) {
        // auto close file output stream
        fileOutputStream.use {
            it.write(dataToSave.toByteArray(Charset.forName("UTF-8")))
            Log.i(name, "Saved data successfully!")
        }
    }

    companion object {

        @Volatile
        private var sDiskCache: DiskCache? = null

        // Nested null check is required if a 2nd thread manages to get
        // queued for this synchronized block while the 1st thread was
        // already executing inside this block, instantiating the object.

        fun getInstance(context: Context): DiskCache =
            sDiskCache?: synchronized(this){
                sDiskCache?: DiskCache(context)
            }
    }

}