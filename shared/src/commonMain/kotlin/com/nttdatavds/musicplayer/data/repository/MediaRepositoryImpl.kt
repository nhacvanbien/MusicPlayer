package com.nttdatavds.musicplayer.data.repository

import com.nttdatavds.musicplayer.data.datasource.local.LocalAudioDataSource
import com.nttdatavds.musicplayer.data.datasource.remote.RemoteDataSource
import com.nttdatavds.musicplayer.data.mapper.toAudio
import com.nttdatavds.musicplayer.domain.model.Audio
import com.nttdatavds.musicplayer.domain.repository.MediaRepository

class MediaRepositoryImpl(
    private val localMediaDataSource: LocalAudioDataSource,
    private val remoteDataSource: RemoteDataSource,
) : MediaRepository {
    override suspend fun getLocalAudios(): List<Audio> {
        return localMediaDataSource.getAllAudioFiles().map { it.toAudio() }
    }

    override suspend fun getRemoteAudios(): List<Audio> {
        return remoteDataSource.getAudios()?.map { it.toAudio() } ?: emptyList()
    }
}