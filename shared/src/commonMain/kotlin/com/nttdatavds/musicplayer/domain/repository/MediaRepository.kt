package com.nttdatavds.musicplayer.domain.repository

import com.nttdatavds.musicplayer.domain.model.Audio

interface MediaRepository {
    suspend fun getLocalAudios(): List<Audio>
    suspend fun getRemoteAudios() : List<Audio>
}