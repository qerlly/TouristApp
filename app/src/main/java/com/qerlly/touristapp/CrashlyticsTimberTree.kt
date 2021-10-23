package com.qerlly.touristapp

import android.util.Log
import timber.log.Timber

class CrashlyticsTimberTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.ERROR) {

        }
    }
}