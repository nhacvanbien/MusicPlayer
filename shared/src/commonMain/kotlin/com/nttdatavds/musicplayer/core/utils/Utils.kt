package com.nttdatavds.musicplayer.core.utils

fun durationToMinutesSeconds(durationInMillis: Long): Pair<Long, Long> {
    val minutes = durationInMillis / 60_000
    val seconds = (durationInMillis % 60_000) / 1000
    return Pair(minutes, seconds)
}