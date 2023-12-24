package com.nttdatavds.musicplayer.di

import com.nttdatavds.musicplayer.data.datasource.local.LocalAudioDataSource
import com.nttdatavds.musicplayer.platform.player.MediaPlayerController
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { MediaPlayerController(applicationContext = get()) }
    single { LocalAudioDataSource(appContext = get()) }
}