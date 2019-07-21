package com.github.noman720.rxhloader.sample

import android.app.Application
import timber.log.Timber

/**
 * Created by Abu Noman on 7/21/19.
 */

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}