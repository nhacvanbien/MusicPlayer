package com.nttdatavds.musicplayer.data.datasource.remote

import com.nttdatavds.musicplayer.data.datasource.remote.AudioDto
import com.nttdatavds.musicplayer.data.datasource.remote.AudioService
import com.nttdatavds.musicplayer.data.datasource.remote.RemoteDataSource

class RemoteDataSourceImpl(private val audioService: AudioService) : RemoteDataSource {
    override suspend fun getAudios(): List<AudioDto>? {
        return audioService.getAudios()
    }
}