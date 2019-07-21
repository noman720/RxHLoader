package com.github.noman720.rxhloader.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * [.loadImage] is where you should start.
 *
 * Created by Abu Noman on 7/20/19.
 */
class NetworkClient private constructor(){

    private val mOkHttpClient: OkHttpClient = OkHttpClient()

    /**
     * Loads an Image from the internet.
     */
    fun <T> loadImage(imageUrl: String, clazz: Class<T>): Observable<T> {
        return Observable.fromCallable {
            val loadRequest = Request.Builder()
                .url(imageUrl)
                .build()

            val response = mOkHttpClient
                .newCall(loadRequest)
                .execute()

            when(clazz){
                Bitmap::class.java -> BitmapFactory.decodeStream(response.body()?.byteStream()) as T
                else -> response.body()?.string() as T
            }
        }
    }

    companion object {
        fun getInstance() = NetworkClient()
    }

}