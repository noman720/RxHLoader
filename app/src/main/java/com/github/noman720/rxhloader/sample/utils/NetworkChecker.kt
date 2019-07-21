package com.github.noman720.rxhloader.sample.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by Abu Noman on 7/11/19.
 */
internal class NetworkChecker {
    companion object {
        fun isConnected(context: Context?): Boolean {
            val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}