package com.nttdatavds.musicplayer.android

import KoinInit
import android.app.Application
import com.nttdatavds.musicplayer.android.di.androidModule
import org.koin.android.ext.koin.androidContext

class MusicApp : Application() {
    override fun onCreate() {
        super.onCreate()

        KoinInit().init {
            androidContext(androidContext = this@MusicApp)
            modules(
                listOf(
                    androidModule,
                ),
            )
        }
    }
}