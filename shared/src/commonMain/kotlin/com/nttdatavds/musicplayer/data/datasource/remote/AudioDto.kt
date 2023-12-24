package com.nttdatavds.musicplayer.data.datasource.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AudioApiResult(
    val data: AudioList
)

@Serializable
data class AudioList(
    val data: List<AudioDto>? = null,
)

@Serializable
data class AudioDto(
    val title: String? = null,

    @SerialName("preview")
    val uri: String? = null,

    val album: Album? = null,
    val artist: Artist? = null,
    val duration: Long,
)

@Serializable
data class Album(
    @SerialName("cover_medium")
    val coverMedium: String? = null
)

@Serializable
data class Artist(
    val name: String? = null,
)