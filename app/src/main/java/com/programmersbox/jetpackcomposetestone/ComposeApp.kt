package com.programmersbox.jetpackcomposetestone

import android.app.Application
import com.programmersbox.loggingutils.Loged
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import com.tonyodev.fetch2okhttp.OkHttpDownloader
import okhttp3.OkHttpClient

class ComposeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Loged.FILTER_BY_PACKAGE_NAME = "programmersbox"
        val fetch = FetchConfiguration.Builder(this)
            .setHttpDownloader(OkHttpDownloader(OkHttpClient.Builder().build()))
            .setNotificationManager(CustomFetchNotificationManager(this))
            .build()

        Fetch.setDefaultInstanceConfiguration(fetch)
    }
}