package com.nttdatavds.musicplayer.domain.model

data class Audio(
    val title: String,
    val uri: String,
    val artworkUri: String,
    val size: Int,
    val duration: Int,
)