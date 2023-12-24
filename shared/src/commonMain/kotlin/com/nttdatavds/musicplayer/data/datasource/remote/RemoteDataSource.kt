package com.nttdatavds.musicplayer.data.datasource.remote

import com.nttdatavds.musicplayer.data.datasource.remote.AudioDto

interface RemoteDataSource {
    suspend fun getAudios(): List<AudioDto>?
}