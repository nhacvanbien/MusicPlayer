package com.nttdatavds.musicplayer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform