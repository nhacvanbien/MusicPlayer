package com.nttdatavds.musicplayer.data.datasource.local

import com.nttdatavds.musicplayer.data.datasource.local.LocalAudio

expect class LocalAudioDataSource {
    suspend fun getAllAudioFiles() : List<LocalAudio>
}