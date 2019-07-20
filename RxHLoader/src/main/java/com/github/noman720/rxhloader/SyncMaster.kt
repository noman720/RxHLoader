package com.github.noman720.rxhloader

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.github.noman720.rxhloader.core.ImageLoader
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Abu Noman on 7/20/19.
 */

class SyncMaster private constructor(
    private val imageLoader: ImageLoader
){

    private var observable: Observable<Bitmap>? = null

    fun load(imageUrl: String): SyncMaster{
        this.observable = imageLoader.load(imageUrl)
        return this
    }

    fun into(imageView: ImageView){
        this.observable?.run {
            this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                it?.let {
                    imageView.setImageBitmap(it)
                }
            }
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: SyncMaster? = null

        fun with(context: Context): SyncMaster =
            INSTANCE?: synchronized(this) {
                INSTANCE?: SyncMaster(
                    ImageLoader.getInstance(context)
                )
            }
    }
}