package com.nttdatavds.musicplayer.data.mapper

import com.nttdatavds.musicplayer.data.datasource.local.LocalAudio
import com.nttdatavds.musicplayer.data.datasource.remote.AudioDto
import com.nttdatavds.musicplayer.domain.model.Audio

fun LocalAudio.toAudio() = Audio(
    title = title,
    uri = uri,
    artworkUri = artworkUri,
    size = size,
    duration = duration
)

fun AudioDto.toAudio() = Audio(
    title = title ?: "Unknown",
    uri = uri ?: "",
    artworkUri = album?.coverMedium ?: "",
    size = -1,
    duration = 30 * 1000
)